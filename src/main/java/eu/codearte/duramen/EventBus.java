package eu.codearte.duramen;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.FastOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eu.codearte.duramen.config.EvenBusContext;
import eu.codearte.duramen.event.Event;
import eu.codearte.duramen.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class EventBus {

	private final EvenBusContext evenBusContext;

	private final Multimap<String, EventHandler> handlers;

	private final Kryo kryo;

	@Autowired
	public EventBus(EvenBusContext evenBusContext) {
		this.evenBusContext = evenBusContext;
		handlers = HashMultimap.create();
		kryo = new Kryo();
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
		evenBusContext.getExecutorService().submit(new Runnable() {
			@Override
			public void run() {
				processEvent(eventId, event);
			}
		});
	}

	private void processEvent(Long eventId, Event event) {
		for (EventHandler handler : handlers.get(event.getClass().getCanonicalName())) {
			handler.onEvent(event);
		}
		evenBusContext.getDatastore().deleteEvent(eventId);
	}

	@SuppressWarnings("unchecked")
	public void processSavedEvents() {
		Map<Long, byte[]> events = evenBusContext.getDatastore().getStoredEvents();
		for (Long eventId : events.keySet()) {
			Input input = new Input(events.get(eventId));
			processEvent(eventId, (Event) kryo.readClassAndObject(input));
		}
	}
}
