package eu.codearte.duramen;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eu.codearte.duramen.event.Event;
import eu.codearte.duramen.handler.EventHandler;
import org.springframework.stereotype.Component;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class EventBus {

	private Multimap<String, EventHandler> handlers = HashMultimap.create();

	@SuppressWarnings("unchecked")
	public void publish(Event event) {
		Preconditions.checkNotNull(event);
		for (EventHandler handler : handlers.get(event.getClass().getCanonicalName())) {
			handler.onEvent(event);
		}
	}

	public void register(String eventDiscriminator, EventHandler eventHandler) {
		handlers.put(eventDiscriminator, eventHandler);
	}
}
