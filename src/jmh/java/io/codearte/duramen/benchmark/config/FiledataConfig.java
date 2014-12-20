package io.codearte.duramen.benchmark.config;

import io.codearte.duramen.annotation.EnableDuramen;
import io.codearte.duramen.datastore.Datastore;
import io.codearte.duramen.datastore.FileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Jakub Kubrynski
 */
@Configuration
@EnableDuramen
class FiledataConfig {

	@Autowired
	private Integer maxMessageSize;

	@Autowired
	private Integer maxMessageCount;

	@Bean
	public Datastore datastore() throws IOException {
		return new FileData("/tmp/perf_duramen_" + UUID.randomUUID().toString(), maxMessageCount, maxMessageSize);
	}
}
