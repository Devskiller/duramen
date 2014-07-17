package eu.codearte.duramen.benchmark.config;

import eu.codearte.duramen.benchmark.event.ComplexEvent;
import eu.codearte.duramen.handler.EventHandler;
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
