package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.EventBus;
import eu.codearte.duramen.annotation.EnableDuramen;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.EmbeddedH2;
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
public class H2Benchmark {

	private EventBus eventBus;
	private AnnotationConfigApplicationContext applicationContext;

//	@Benchmark
	public void testH2() {
		eventBus.publish(new SimpleEvent("TEST"));
	}

	@Setup(Level.Iteration)
	public void init() throws IOException {
		applicationContext = new AnnotationConfigApplicationContext(H2Config.class);
		eventBus = applicationContext.getBean(EventBus.class);
	}

	@TearDown(Level.Iteration)
	public void cleanup() throws IOException, InterruptedException {
		applicationContext.close();
		Runtime.getRuntime().exec("rm -rf /tmp/perf_duramen_h2.data.mv.db").waitFor();
		Runtime.getRuntime().exec("rm -rf /tmp/perf_duramen_h2.data.trace.db").waitFor();
	}

	@Configuration
	@EnableDuramen
	static class H2Config {
		@Bean
		public Datastore datastore() {
			return new EmbeddedH2("jdbc:h2:file:/tmp/perf_duramen_h2.data");
		}
	}
}
