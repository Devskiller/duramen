package io.codearte.duramen.benchmark.config;

import io.codearte.duramen.annotation.EnableDuramen;
import io.codearte.duramen.datastore.Datastore;
import io.codearte.duramen.datastore.EmbeddedH2;
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
