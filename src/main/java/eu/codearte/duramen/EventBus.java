package eu.codearte.duramen;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.FastOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eu.codearte.duramen.config.EvenBusContext;
import eu.codearte.duramen.event.Event;
import eu.codearte.duramen.handler.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class EventBus {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final EvenBusContext evenBusContext;

	private final Multimap<String, EventHandler> handlers;
	private final Kryo kryo;
	private final ObjectMapper objectMapper;

	@Autowired
	public EventBus(EvenBusContext evenBusContext) {
		this.evenBusContext = evenBusContext;
		handlers = HashMultimap.create();
		kryo = new Kryo();
		objectMapper = new ObjectMapper();
	}

	public void register(String eventDiscriminator, EventHandler eventHandler) {
		handlers.put(eventDiscriminator, eventHandler);
	}

	@SuppressWarnings("unchecked")
	public void publish(final Event event) {
		checkNotNull(event);
		Output output = new FastOutput(evenBusContext.getMaxMessageSize());
		kryo.writeClassAndObject(output, event);
		final Long eventId = evenBusContext.getDatastore().saveEvent(output.toBytes());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Publishing event [id = {}, body={}]", eventId, serializeToJson(event));
		}

		evenBusContext.getExecutorService().submit(new Runnable() {
			@Override
			public void run() {
				processEvent(eventId, event);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void processEvent(Long eventId, Event event) {
		Collection<EventHandler> eventHandlers = handlers.get(event.getClass().getCanonicalName());
		if (LOG.isDebugEnabled()) {
			LOG.debug("Processing event id = {} - found {} valid handlers", eventId, eventHandlers.size());
		}
		for (EventHandler handler : eventHandlers) {
			try {
				handler.onEvent(event);
			} catch (Throwable e) {
				LOG.error("Error during processing event [{}]", serializeToJson(event), e);
			}
		}
		evenBusContext.getDatastore().deleteEvent(eventId);
	}

	private String serializeToJson(Event event) {
		String json;
		try {
			json = objectMapper.writeValueAsString(event);
		} catch (JsonProcessingException jsonException) {
			LOG.error("Error during serializing event to json", jsonException);
			json = "ERROR";
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	public void processSavedEvents() {
		Map<Long, byte[]> events = evenBusContext.getDatastore().getStoredEvents();
		LOG.info("Processing stored events. Found {} events to process", events.size());
		for (Long eventId : events.keySet()) {
			Input input = new Input(events.get(eventId));
			processEvent(eventId, (Event) kryo.readClassAndObject(input));
		}
	}
}
