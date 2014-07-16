package eu.codearte.duramen.handler;

import eu.codearte.duramen.event.Event;

/**
 * @author Jakub Kubrynski
 */
public interface ExceptionHandler {

	void handleException(Event event, Throwable e, EventHandler handler);
}
