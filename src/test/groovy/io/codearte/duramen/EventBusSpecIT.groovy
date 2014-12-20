package io.codearte.duramen

import io.codearte.duramen.annotation.EnableDuramen
import io.codearte.duramen.datastore.Datastore
import io.codearte.duramen.datastore.InMemory
import io.codearte.duramen.test.EventConsumer
import io.codearte.duramen.test.EventProducer
import io.codearte.duramen.test.TestEvent
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader
import org.kubek2k.springockito.annotations.WrapWithSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.mockito.Matchers.isA
import static org.mockito.Mockito.timeout
import static org.mockito.Mockito.verify

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@ContextConfiguration(classes = SampleConfiguration, loader = SpringockitoAnnotatedContextLoader)
class EventBusSpecIT extends Specification {

	@Autowired
	EventProducer eventProducer

	@Autowired
	@WrapWithSpy
	EventConsumer eventConsumer

	def "should publish and receive event"() {
		when:
			eventProducer.produce()
		then:
			verify(eventConsumer, timeout(500)).onEvent(isA(TestEvent))
	}

	@Configuration
	@ComponentScan(basePackages = "io.codearte.duramen.test")
	@EnableDuramen
	static class SampleConfiguration {

		@Bean
		Datastore datastore() {
			new InMemory();
		}

	}
}