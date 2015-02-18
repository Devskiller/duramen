package test.codearte.duramen;

import io.codearte.duramen.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class EventProducer {

	private final EventBus consumer;

	@Autowired
	public EventProducer(EventBus consumer) {
		this.consumer = consumer;
	}

	public void produce() {
		TestEvent event = new TestEvent("Test string");
		consumer.publish(event);
	}
}
