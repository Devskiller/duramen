package eu.codearte.duramen.handler;

import eu.codearte.duramen.event.EventJsonSerializer;
import eu.codearte.duramen.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
public class LoggingExceptionHandler implements ExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final EventJsonSerializer eventJsonSerializer;

	public LoggingExceptionHandler(EventJsonSerializer eventJsonSerializer) {
		this.eventJsonSerializer = eventJsonSerializer;
	}

	@Override
	public void handleException(Event event, Throwable e) {
		LOG.error("Error during processing event [{}]", eventJsonSerializer.serializeToJson(event), e);
	}
}
