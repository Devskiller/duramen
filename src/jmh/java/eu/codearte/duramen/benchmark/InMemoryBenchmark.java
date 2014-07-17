package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.EventBus;
import eu.codearte.duramen.annotation.EnableDuramen;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.InMemory;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
@State(Scope.Benchmark)
public class InMemoryBenchmark {

	private EventBus eventBus;
	private AnnotationConfigApplicationContext applicationContext;

//	@Benchmark
	public void testSimpleEvent() {
		eventBus.publish(new SimpleEvent("TEST"));
	}

	@Setup(Level.Iteration)
	public void init() throws IOException {
		applicationContext = new AnnotationConfigApplicationContext(FiledataConfig.class);
		eventBus = applicationContext.getBean(EventBus.class);
	}

	@TearDown(Level.Iteration)
	public void cleanup() throws IOException, InterruptedException {
		applicationContext.close();
	}

	@Configuration
	@EnableDuramen
	static class FiledataConfig {
		@Bean
		public Integer maxMessageSize() {
			return 256;
		}

		@Bean
		public Integer maxMessageCount() {
			return 1024;
		}

		@Bean
		public Datastore datastore() throws IOException {
			return new InMemory();
		}
	}
}
