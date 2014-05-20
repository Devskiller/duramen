package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.generator.RandomIdGenerator;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
@State(Scope.Benchmark)
public class IdGeneratorBenchmark {

	private RandomIdGenerator randomIdGenerator;

	@Setup
	public void init() {
		randomIdGenerator = new RandomIdGenerator();
	}

	@GenerateMicroBenchmark
	public long generateId() {
		return randomIdGenerator.getNextId();
	}
}
