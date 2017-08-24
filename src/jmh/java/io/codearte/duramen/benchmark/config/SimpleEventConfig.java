package io.codearte.duramen.benchmark.config;

import io.codearte.duramen.config.DuramenConfiguration;
import io.codearte.duramen.handler.EventHandler;
import io.codearte.duramen.benchmark.event.SimpleEvent;
import org.springframework.context.annotation.Bean;

/**
 * @author Jakub Kubrynski
 */
public class SimpleEventConfig {

	@Bean
	public DuramenConfiguration duramenConfiguration() {
		return DuramenConfiguration.builder()
				.maxMessageCount(1024)
				.maxMessageSize(256)
				.maxProcessingThreads(1)
				.build();
	}
	@Bean
	public EventHandler<SimpleEvent> eventHandler() {
		return new EventHandler<SimpleEvent>() {
			@Override
			public void onEvent(SimpleEvent event) {
			}
		};
	}
}
