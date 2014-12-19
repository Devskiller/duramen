package io.codearte.duramen.handler;

import io.codearte.duramen.event.Event;

/**
 * @author Jakub Kubrynski
 */
public interface ExceptionHandler {

	void handleException(Event event, Throwable e, EventHandler handler);
}
