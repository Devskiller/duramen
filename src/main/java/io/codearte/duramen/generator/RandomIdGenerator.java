package io.codearte.duramen.generator;

import java.util.Random;

/**
 * @author Jakub Kubrynski
 */
public class RandomIdGenerator implements IdGenerator {

	private Random random = new Random();

	@Override
	public Long getNextId() {
		return System.nanoTime() * 10 + random.nextInt(10);
	}

}
