package io.codearte.duramen

import io.codearte.duramen.annotation.EnableDuramen
import io.codearte.duramen.datastore.Datastore
import io.codearte.duramen.datastore.InMemory
import org.mockito.Mockito
import spock.lang.Specification
import test.codearte.duramen.EventConsumer
import test.codearte.duramen.EventProducer
import test.codearte.duramen.TestEvent

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration

import static org.mockito.Matchers.isA
import static org.mockito.Mockito.timeout
import static org.mockito.Mockito.verify

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@ContextConfiguration(classes = SampleConfiguration)
class EventBusSpecIT extends Specification {

	@Autowired
	EventProducer eventProducer

	@Autowired
	EventConsumer eventConsumer

	def "should publish and receive event"() {
		when:
			eventProducer.produce()
		then:
			verify(eventConsumer, timeout(500)).onEvent(isA(TestEvent))
	}

	@Configuration
	@ComponentScan(basePackages = "test.codearte.duramen")
	@EnableDuramen
	static class SampleConfiguration {

		@Bean
		Datastore datastore() {
			new InMemory()
		}

		@Bean
		EventConsumer eventConsumer() {
			return Mockito.mock(EventConsumer)
		}

	}
}