package eu.codearte.duramen.config;

import eu.codearte.duramen.event.EventJsonSerializer;
import eu.codearte.duramen.handler.ExceptionHandler;
import eu.codearte.duramen.datastore.Datastore;

import java.util.concurrent.ExecutorService;

/**
 * Created by Jakub Kubrynski / 2014-03-26
 */
public class EvenBusContext {

	private final Integer maxMessageSize;
	private final ExecutorService executorService;
	private final Datastore datastore;
	private final EventJsonSerializer eventJsonSerializer;
	private final ExceptionHandler exceptionHandler;

	public EvenBusContext(Integer maxMessageSize, ExecutorService executorService,
												Datastore datastore, EventJsonSerializer eventJsonSerializer, ExceptionHandler exceptionHandler) {
		this.maxMessageSize = maxMessageSize;
		this.executorService = executorService;
		this.datastore = datastore;
		this.eventJsonSerializer = eventJsonSerializer;
		this.exceptionHandler = exceptionHandler;
	}

	public Integer getMaxMessageSize() {
		return maxMessageSize;
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
