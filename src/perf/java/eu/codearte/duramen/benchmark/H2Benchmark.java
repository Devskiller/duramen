package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.config.DuramenConfiguration;
import eu.codearte.duramen.config.EvenBusContext;
import eu.codearte.duramen.datastore.Datastore;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
@State(Scope.Benchmark)
public class H2Benchmark {

	private Datastore embeddedH2;

	@Setup
	public void init() throws IOException {
		AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext(DuramenConfiguration.class);
		embeddedH2 = annotationConfigApplicationContext.getBean(EvenBusContext.class).getDatastore();
	}

	@GenerateMicroBenchmark
	public long testH2() {
		Long id = embeddedH2.saveEvent("TEST".getBytes());
		embeddedH2.deleteEvent(id);
		return id;
	}
}
