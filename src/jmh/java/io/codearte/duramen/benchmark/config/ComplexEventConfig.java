package io.codearte.duramen.benchmark.config;

import io.codearte.duramen.config.DuramenConfiguration;
import io.codearte.duramen.handler.EventHandler;
import io.codearte.duramen.benchmark.event.ComplexEvent;
import org.springframework.context.annotation.Bean;

/**
 * @author Jakub Kubrynski
 */
public class ComplexEventConfig {

	@Bean
	public DuramenConfiguration duramenConfiguration() {
		return DuramenConfiguration.builder()
				.maxMessageCount(1024)
				.maxMessageSize(1536)
				.maxProcessingThreads(1)
				.build();
	}

	@Bean
	public EventHandler<ComplexEvent> eventHandler() {
		return new EventHandler<ComplexEvent>() {
			@Override
			public void onEvent(ComplexEvent event) {
			}
		};
	}
}
