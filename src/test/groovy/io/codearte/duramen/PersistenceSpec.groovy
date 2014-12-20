package io.codearte.duramen

import io.codearte.duramen.annotation.EnableDuramen
import io.codearte.duramen.handler.EventHandler
import io.codearte.duramen.test.EventProducer
import io.codearte.duramen.test.TestEvent
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.LockSupport

import static com.jayway.awaitility.Awaitility.await
import static com.jayway.awaitility.Duration.TWO_SECONDS
import static org.hamcrest.core.IsEqual.equalTo

/**
 * @author Jakub Kubrynski
 */
class PersistenceSpec extends Specification {

	static AtomicBoolean wrongHandlerInvoked = new AtomicBoolean(false)
	static AtomicBoolean goodHandlerInvoked = new AtomicBoolean(false)

	def "should process event after crash"() {
		given:
			TestUtil.cleanupDatastore()
			def context = new AnnotationConfigApplicationContext(SampleConfigurationWrongConsumer)
			def eventProducer = context.getBean(EventProducer)
		when:
			eventProducer.produce()
			await().atMost(TWO_SECONDS).untilAtomic(wrongHandlerInvoked, equalTo(true))
			context.close()
		and:
			new AnnotationConfigApplicationContext(SampleConfigurationGoodConsumer)
		then:
			await().atMost(TWO_SECONDS).untilAtomic(goodHandlerInvoked, equalTo(true))
	}

	@ComponentScan(basePackages = "io.codearte.duramen.test")
	@EnableDuramen
	static class SampleConfigurationWrongConsumer {
		@Bean
		EventHandler eventHandler() {
			return new EventHandler<TestEvent>() {
				@Override
				void onEvent(TestEvent event) {
					wrongHandlerInvoked.set(true)
					LockSupport.park()
				}
			}
		}
	}

	@ComponentScan(basePackages = "io.codearte.duramen.test")
	@EnableDuramen
	static class SampleConfigurationGoodConsumer {
		@Bean
		EventHandler eventHandler() {
			return new EventHandler<TestEvent>() {
				@Override
				void onEvent(TestEvent event) {
					goodHandlerInvoked.set(true)
				}
			}
		}
	}


}