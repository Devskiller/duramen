package eu.codearte.duramen.config;

import eu.codearte.duramen.DuramenPackageMarker;
import eu.codearte.duramen.internal.EventJsonSerializer;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.FileData;
import eu.codearte.duramen.handler.ExceptionHandler;
import eu.codearte.duramen.internal.LoggingExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main configuration class. Handles all optional dependencies which allows
 * user to override default settings
 *
 * @author Jakub Kubrynski
 */
@SuppressWarnings("FieldCanBeLocal")
@Configuration
@ComponentScan(basePackageClasses = DuramenPackageMarker.class)
public class DuramenConfiguration {

	@Autowired(required = false)
	@Qualifier("maxMessageSize")
	private Integer maxMessageSize = 4096;

	@Autowired(required = false)
	@Qualifier("maxProcessingThreads")
	private Integer maxProcessingThreads = 1;

	@Autowired(required = false)
	@Qualifier("useDaemonThreads")
	private Boolean useDaemonThreads = true;

	@Autowired(required = false)
	private Datastore datastore;

	@Autowired(required = false)
	@Qualifier("duramenExecutorService")
	private ExecutorService executorService;

	@Autowired(required = false)
	private ExceptionHandler exceptionHandler;

	@Autowired
	private EventJsonSerializer eventJsonSerializer;

	@Bean
	public EvenBusContext evenBusProperties() throws IOException {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(maxProcessingThreads, buildThreadFactory());
		}
		if (datastore == null) {
			datastore = new FileData();
		}
		if (exceptionHandler == null) {
			exceptionHandler = new LoggingExceptionHandler(eventJsonSerializer);
		}
		return new EvenBusContext(maxMessageSize, executorService, datastore, eventJsonSerializer, exceptionHandler);
	}

	private ThreadFactory buildThreadFactory() {
		final AtomicInteger threadNumerator = new AtomicInteger(0);

		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(useDaemonThreads);
				thread.setName("DuramenProcessingThread-" + threadNumerator.incrementAndGet());
				return thread;
			}
		};
	}
}
