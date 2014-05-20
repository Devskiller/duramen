package eu.codearte.duramen.handler;

import eu.codearte.duramen.event.Event;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
public interface ExceptionHandler {

	void handleException(Event event, Throwable e);
}
