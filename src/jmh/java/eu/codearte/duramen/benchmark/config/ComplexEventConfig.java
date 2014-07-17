package eu.codearte.duramen.benchmark.config;

import org.springframework.context.annotation.Bean;

/**
 * @author Jakub Kubrynski
 */
public class ComplexEventConfig {

	@Bean
	public Integer maxMessageSize() {
		return 1536;
	}

	@Bean
	public Integer maxMessageCount() {
		return 1024;
	}
}
