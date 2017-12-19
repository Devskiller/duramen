package io.codearte.duramen

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import io.codearte.duramen.annotation.EnableDuramen
import io.codearte.duramen.config.DuramenConfiguration
import io.codearte.duramen.datastore.Datastore
import io.codearte.duramen.datastore.InMemory
import io.codearte.duramen.handler.EventHandler
import spock.lang.Specification
import test.codearte.duramen.RetryableEventProducer
import test.codearte.duramen.TestRetryableEvent

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

import static org.awaitility.Awaitility.await
import static org.hamcrest.core.IsEqual.equalTo

/**
 * @author Jakub Kubrynski
 */
class RetryableSpec extends Specification {

	static AtomicInteger goodHandlerInvoked

	void setup() {
		TestUtil.cleanupDatastore()
		// it's set to 1 due to https://github.com/awaitility/awaitility/issues/99
		goodHandlerInvoked = new AtomicInteger(1)
	}

	def "should retry event processing"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, MultipleRetryConfiguration)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(5, TimeUnit.SECONDS).untilAtomic(goodHandlerInvoked, equalTo(2))
	}

	def "should retry single event after failing in two handlers"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, MultipleRetryManyHandlersConfiguration)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(5, TimeUnit.SECONDS).untilAtomic(goodHandlerInvoked, equalTo(3))
	}

	def "should retry only limited number of times"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, SingleRetryConfiguration)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(5, TimeUnit.SECONDS).untilAtomic(goodHandlerInvoked, equalTo(1))
	}

	def "should not retry incorrect exception"() {
		given:
			def context = new AnnotationConfigApplicationContext(SampleConfiguration, SingleRetryConfigurationWithIncorrectException)
			def eventProducer = context.getBean(RetryableEventProducer)
		when:
			eventProducer.produce()
		then:
			await().atMost(5, TimeUnit.SECONDS).untilAtomic(goodHandlerInvoked, equalTo(1))
	}

	@ComponentScan(basePackages = "test.codearte.duramen")
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
	static class MultipleRetryManyHandlersConfiguration {
		@Bean
		DuramenConfiguration duramenConfiguration() {
			return DuramenConfiguration.builder().retryDelayInSeconds(1).build()
		}

		@Bean
		EventHandler eventHandlerA() {
			return new RetryableEventHandler(1)
		}

		@Bean
		EventHandler eventHandlerB() {
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
			return new RetryableEventHandler(2)
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

		int counter = 0
		int max = 0

		RetryableEventHandler(int max) {
			this.max = max
		}

		@Override
		void onEvent(TestRetryableEvent event) {
			if (counter < max) {
				counter++
				throw new RuntimeException()
			}
			goodHandlerInvoked.incrementAndGet()
		}
	}


}