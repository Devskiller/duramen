package io.codearte.duramen.benchmark.config;

import eu.codearte.duramen.handler.EventHandler;
import io.codearte.duramen.benchmark.event.SimpleEvent;
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

	@Bean
	public EventHandler<SimpleEvent> eventHandler() {
		return new EventHandler<SimpleEvent>() {
			@Override
			public void onEvent(SimpleEvent event) {
			}
		};
	}
}
