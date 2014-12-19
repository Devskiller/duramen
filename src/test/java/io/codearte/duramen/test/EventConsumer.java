package io.codearte.duramen.test;

import io.codearte.duramen.handler.EventHandler;
import org.springframework.stereotype.Component;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class EventConsumer implements EventHandler<TestEvent> {

	@Override
	public void onEvent(TestEvent event) {
	}

}
