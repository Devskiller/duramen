package eu.codearte.duramen.config;

import eu.codearte.duramen.datastore.Datastore;

import java.util.concurrent.ExecutorService;

/**
 * Created by Jakub Kubrynski / 2014-03-26
 */
public class EvenBusContext {

	private final Integer maxMessageSize;
	private final ExecutorService executorService;
	private final Datastore datastore;

	public EvenBusContext(Integer maxMessageSize, ExecutorService executorService, Datastore datastore) {
		this.maxMessageSize = maxMessageSize;
		this.executorService = executorService;
		this.datastore = datastore;
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
}
