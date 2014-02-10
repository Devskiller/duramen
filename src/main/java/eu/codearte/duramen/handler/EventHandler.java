package eu.codearte.duramen.handler;

import eu.codearte.duramen.event.Event;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
public interface EventHandler<E extends Event> {
	void onEvent(E event);
}
