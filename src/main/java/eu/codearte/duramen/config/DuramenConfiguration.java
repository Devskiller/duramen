package eu.codearte.duramen.config;

import eu.codearte.duramen.DuramenPackageMarker;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.EmbeddedH2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@SuppressWarnings("FieldCanBeLocal")
@Configuration
@ComponentScan(basePackageClasses = DuramenPackageMarker.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Datastore.class))
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

	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Bean
	public EvenBusContext evenBusProperties() {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(maxProcessingThreads, buildThreadFactory());
		}
		if (datastore == null) {
			datastore = new EmbeddedH2("jdbc:h2:file:/tmp/duramen.data");
			autowireCapableBeanFactory.initializeBean(datastore, "duramenDatastoreBean");
		}
		return new EvenBusContext(maxMessageSize, executorService, datastore);
	}

	protected ThreadFactory buildThreadFactory() {
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
