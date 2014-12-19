package eu.codearte.duramen.config;

import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.handler.ExceptionHandler;
import eu.codearte.duramen.internal.EventJsonSerializer;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Class grouping all dependencies needed to processing events.
 *
 * @author Jakub Kubrynski
 */
public class EvenBusContext {

	private final DuramenConfiguration duramenConfiguration;
	private final ScheduledExecutorService executorService;
	private final Datastore datastore;
	private final EventJsonSerializer eventJsonSerializer;
	private final ExceptionHandler exceptionHandler;

	public EvenBusContext(DuramenConfiguration duramenConfiguration, ScheduledExecutorService executorService,
	                      Datastore datastore, EventJsonSerializer eventJsonSerializer,
	                      ExceptionHandler exceptionHandler) {
		this.duramenConfiguration = duramenConfiguration;
		this.executorService = executorService;
		this.datastore = datastore;
		this.eventJsonSerializer = eventJsonSerializer;
		this.exceptionHandler = exceptionHandler;
	}

	public Integer getMaxMessageSize() {
		return duramenConfiguration.getMaxMessageSize();
	}

	public Integer getMaxMessageCount() {
		return duramenConfiguration.getMaxMessageCount();
	}

	public Integer getRetryDelayInSeconds() {
		return duramenConfiguration.getRetryDelayInSeconds();
	}

	public Integer getRetryCount() {
		return duramenConfiguration.getRetryCount();
	}

	public ScheduledExecutorService getExecutorService() {
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

	public Set<Class<? extends Throwable>> getRetryableExceptions() {
		return duramenConfiguration.getRetryableExceptions();
	}
}
