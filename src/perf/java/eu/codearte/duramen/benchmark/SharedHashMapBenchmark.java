package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.datastore.FileData;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
@State(Scope.Benchmark)
public class SharedHashMapBenchmark {

	private FileData fileData;

	@Setup
	public void init() throws IOException {
		fileData = new FileData();
	}

	@GenerateMicroBenchmark
	public long testSharedHashMap() {
		Long id = fileData.saveEvent("TEST".getBytes());
		fileData.deleteEvent(id);
		return id;
	}
}
