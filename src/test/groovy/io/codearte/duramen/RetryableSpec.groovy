package io.codearte.duramen

import io.codearte.duramen.annotation.EnableDuramen
import io.codearte.duramen.config.DuramenConfiguration
import io.codearte.duramen.datastore.Datastore
import io.codearte.duramen.datastore.InMemory
import io.codearte.duramen.handler.EventHandler
import io.codearte.duramen.test.RetryableEventProducer
import io.codearte.duramen.test.TestRetryableEvent
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean

import static com.jayway.awaitility.Awaitility.await
import static com.jayway.awaitility.Duration.FIVE_SECONDS
import static org.hamcrest.core.IsEqual.equalTo

/**
 * @author Jakub Kubrynski
 */
class RetryableSpec extends Specification {

	static AtomicBoolean goodHandlerInvoked

	void setup() {
		TestUtil.cleanupDatastore()
		goodHandlerInvoked = new AtomicBoolean(false)
	}

	def "should retry event processing"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, MultipleRetryConfiguration)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(FIVE_SECONDS).untilAtomic(goodHandlerInvoked, equalTo(true))
	}

	def "should retry only limited number of times"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, SingleRetryConfiguration)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(FIVE_SECONDS).untilAtomic(goodHandlerInvoked, equalTo(false))
	}

	def "should not retry incorrect exception"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, SingleRetryConfigurationWithIncorrectException)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(FIVE_SECONDS).untilAtomic(goodHandlerInvoked, equalTo(false))
	}

	@ComponentScan(basePackages = "eu.codearte.duramen.test")
	@EnableDuramen
	static class SampleConfiguration {

		@Bean
		Datastore datastore() {
			return new InMemory()
		}
	}

	@Configuration
	static class MultipleRetryConfiguration {
		@Bean
		DuramenConfiguration duramenConfiguration() {
			return DuramenConfiguration.builder().retryDelayInSeconds(1).build()
		}

		@Bean
		EventHandler eventHandler() {
			return new RetryableEventHandler(1)
		}
	}

	@Configuration
	static class SingleRetryConfiguration {

		@Bean
		DuramenConfiguration duramenConfiguration() {
			return DuramenConfiguration.builder().retryDelayInSeconds(1).retryCount(1).build()
		}

		@Bean
		EventHandler eventHandler() {
			return new RetryableEventHandler(1)
		}
	}

	@Configuration
	static class SingleRetryConfigurationWithIncorrectException {

		@Bean
		DuramenConfiguration duramenConfiguration() {
			return DuramenConfiguration.builder().retryDelayInSeconds(1).retryCount(2)
					.retryableExceptions(NullPointerException.class).build()
		}

		@Bean
		EventHandler eventHandler() {
			return new RetryableEventHandler(1)
		}
	}

	static class RetryableEventHandler implements EventHandler<TestRetryableEvent> {

		int counter = 0;
		int max = 0;

		RetryableEventHandler(int max) {
			this.max = max
		}

		@Override
		void onEvent(TestRetryableEvent event) {
			if (counter < max) {
				counter++;
				throw new RuntimeException();
			}
			goodHandlerInvoked.set(true)
		}
	}


}