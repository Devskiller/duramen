package io.codearte.duramen.benchmark.config;

import eu.codearte.duramen.annotation.EnableDuramen;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.InMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Jakub Kubrynski
 */
@Configuration
@EnableDuramen
class InMemoryConfig {

	@Bean
	public Datastore datastore() throws IOException {
		return new InMemory();
	}
}
