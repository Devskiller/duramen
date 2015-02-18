package test.codearte.duramen;

import io.codearte.duramen.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class RetryableEventProducer {

	private final EventBus consumer;

	@Autowired
	public RetryableEventProducer(EventBus consumer) {
		this.consumer = consumer;
	}

	public void produce() {
		TestRetryableEvent event = new TestRetryableEvent("Test string");
		consumer.publish(event);
	}
}
