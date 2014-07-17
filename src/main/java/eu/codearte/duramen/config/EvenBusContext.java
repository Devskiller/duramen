package eu.codearte.duramen.config;

import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.handler.ExceptionHandler;
import eu.codearte.duramen.internal.EventJsonSerializer;

import java.util.concurrent.ExecutorService;

/**
 * Class grouping all dependencies needed to processing events.
 *
 * @author Jakub Kubrynski
 */
public class EvenBusContext {

	private final Integer maxMessageSize;
	private final Integer maxMessageCount;
	private final ExecutorService executorService;
	private final Datastore datastore;
	private final EventJsonSerializer eventJsonSerializer;
	private final ExceptionHandler exceptionHandler;

	public EvenBusContext(Integer maxMessageSize, Integer maxMessageCount, ExecutorService executorService,
												Datastore datastore, EventJsonSerializer eventJsonSerializer, ExceptionHandler exceptionHandler) {
		this.maxMessageSize = maxMessageSize;
		this.maxMessageCount = maxMessageCount;
		this.executorService = executorService;
		this.datastore = datastore;
		this.eventJsonSerializer = eventJsonSerializer;
		this.exceptionHandler = exceptionHandler;
	}

	public Integer getMaxMessageSize() {
		return maxMessageSize;
	}

	public Integer getMaxMessageCount() {
		return maxMessageCount;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public EventJsonSerializer getEventJsonSerializer() {
		return eventJsonSerializer;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

}
