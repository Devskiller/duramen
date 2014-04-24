package eu.codearte.duramen.test

import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader
import org.kubek2k.springockito.annotations.WrapWithSpy
import org.springframework.beans.factory.annotation.Autowired
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
}