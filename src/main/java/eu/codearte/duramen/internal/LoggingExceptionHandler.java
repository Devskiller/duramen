package eu.codearte.duramen.internal;

import eu.codearte.duramen.event.Event;
import eu.codearte.duramen.handler.EventHandler;
import eu.codearte.duramen.handler.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Jakub Kubrynski
 */
public class LoggingExceptionHandler implements ExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final EventJsonSerializer eventJsonSerializer;

	public LoggingExceptionHandler(EventJsonSerializer eventJsonSerializer) {
		this.eventJsonSerializer = eventJsonSerializer;
	}

	@Override
	public void handleException(Event event, Throwable e, EventHandler handler) {
		LOG.error("Error during processing event [{}] in handler {}",
				eventJsonSerializer.serializeToJson(event), handler.getClass().getSimpleName(), e);
	}
}
