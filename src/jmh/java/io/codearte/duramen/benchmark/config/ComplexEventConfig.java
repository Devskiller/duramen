package io.codearte.duramen.benchmark.config;

import io.codearte.duramen.handler.EventHandler;
import io.codearte.duramen.benchmark.event.ComplexEvent;
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

	@Bean
	public EventHandler<ComplexEvent> eventHandler() {
		return new EventHandler<ComplexEvent>() {
			@Override
			public void onEvent(ComplexEvent event) {
			}
		};
	}
}
