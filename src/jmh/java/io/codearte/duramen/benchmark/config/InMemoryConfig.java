package io.codearte.duramen.benchmark.config;

import io.codearte.duramen.annotation.EnableDuramen;
import io.codearte.duramen.datastore.Datastore;
import io.codearte.duramen.datastore.InMemory;
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
