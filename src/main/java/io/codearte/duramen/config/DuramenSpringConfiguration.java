package io.codearte.duramen.config;

import io.codearte.duramen.DuramenPackageMarker;
import io.codearte.duramen.datastore.Datastore;
import io.codearte.duramen.datastore.FileData;
import io.codearte.duramen.handler.ExceptionHandler;
import io.codearte.duramen.internal.EventJsonSerializer;
import io.codearte.duramen.internal.LoggingExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
@ComponentScan(basePackageClasses = DuramenPackageMarker.class, excludeFilters = @ComponentScan.Filter(Configuration.class))
public class DuramenSpringConfiguration {

	@Autowired(required = false)
	private DuramenConfiguration duramenConfiguration;

	@Autowired(required = false)
	private Datastore datastore;

	@Autowired(required = false)
	@Qualifier("duramenExecutorService")
	private ScheduledExecutorService executorService;

	@Autowired(required = false)
	private ExceptionHandler exceptionHandler;

	@Autowired
	private EventJsonSerializer eventJsonSerializer;

	@Bean
	public EvenBusContext evenBusProperties() throws IOException {
		if (duramenConfiguration == null) {
			duramenConfiguration = DuramenConfiguration.builder().build();
		}
		if (executorService == null) {
			executorService = Executors.newScheduledThreadPool(duramenConfiguration.getMaxProcessingThreads(), buildThreadFactory());
		}
		if (datastore == null) {
			datastore = new FileData(FileData.DEFAULT_FILENAME, duramenConfiguration.getMaxMessageCount(), duramenConfiguration.getMaxMessageSize());
		}
		if (exceptionHandler == null) {
			exceptionHandler = new LoggingExceptionHandler(eventJsonSerializer);
		}
		return new EvenBusContext(duramenConfiguration, executorService, datastore, eventJsonSerializer, exceptionHandler);
	}

	private ThreadFactory buildThreadFactory() {
		final AtomicInteger threadNumerator = new AtomicInteger(0);

		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(duramenConfiguration.getUseDaemonThreads());
				thread.setName("DuramenProcessingThread-" + threadNumerator.incrementAndGet());
				return thread;
			}
		};
	}
}
