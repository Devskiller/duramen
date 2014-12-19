package io.codearte.duramen.benchmark.config;

import eu.codearte.duramen.annotation.EnableDuramen;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.EmbeddedH2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jakub Kubrynski
 */
@Configuration
@EnableDuramen
class H2Config {

	@Bean
	public Datastore datastore() {
		return new EmbeddedH2("jdbc:h2:file:/tmp/perf_duramen_h2.data");
	}
}
