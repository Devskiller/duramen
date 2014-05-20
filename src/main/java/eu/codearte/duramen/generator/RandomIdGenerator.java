package eu.codearte.duramen.generator;

import java.util.Random;

/**
 * Created by jkubrynski@gmail.com / 2014-05-19
 */
public class RandomIdGenerator {

	private Random random = new Random();

	public Long getNextId() {
		return System.nanoTime() * 10 + random.nextInt(10);
	}

}
