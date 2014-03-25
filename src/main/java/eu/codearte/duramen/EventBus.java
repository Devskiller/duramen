package eu.codearte.duramen;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.FastOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeMemoryOutput;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.event.Event;
import eu.codearte.duramen.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class EventBus {

	private final Datastore datastore;

	private Multimap<String, EventHandler> handlers = HashMultimap.create();

	private final Kryo kryo;

	@Autowired
	public EventBus(Datastore datastore) {
		this.datastore = datastore;
		kryo = new Kryo();
	}

	public void register(String eventDiscriminator, EventHandler eventHandler) {
		handlers.put(eventDiscriminator, eventHandler);
	}

	@SuppressWarnings("unchecked")
	public void publish(Event event) {
		checkNotNull(event);
		Output output = new FastOutput(4096);
		kryo.writeClassAndObject(output, event);
		Long eventId = datastore.saveEvent(output.toBytes());
		processEvent(eventId, event);
	}

	private void processEvent(Long eventId, Event event) {
		for (EventHandler handler : handlers.get(event.getClass().getCanonicalName())) {
			handler.onEvent(event);
		}
		datastore.deleteEvent(eventId);
	}

	@SuppressWarnings("unchecked")
	public void processSavedEvents() {
		Map<Long, byte[]> events = datastore.getStoredEvents();
		for (Long eventId : events.keySet()) {
			Input input = new Input(events.get(eventId));
			processEvent(eventId, (Event) kryo.readClassAndObject(input));
		}
	}
}
