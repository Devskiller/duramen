package eu.codearte.duramen.benchmark.config;

import org.springframework.context.annotation.Bean;

/**
 * @author Jakub Kubrynski
 */
public class SimpleEventConfig {

	@Bean
	public Integer maxMessageSize() {
		return 256;
	}

	@Bean
	public Integer maxMessageCount() {
		return 1024;
	}
}
