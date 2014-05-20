package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.datastore.InMemory;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
@State(Scope.Benchmark)
public class InMemoryBenchmark {

	private InMemory inMemory;

	@Setup
	public void init() throws IOException {
		inMemory = new InMemory();
	}

	@GenerateMicroBenchmark
	public long testInMemory() {
		Long id = inMemory.saveEvent("TEST".getBytes());
		inMemory.deleteEvent(id);
		return id;
	}
}
