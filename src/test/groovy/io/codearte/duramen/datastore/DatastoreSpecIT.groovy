package io.codearte.duramen.datastore

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
class DatastoreSpecIT extends Specification {

	private static final String FILENAME = "duramen.test"

	@Unroll
	def "should persist event in datastore"() {
		given:
			def eventBytes = "Test".getBytes()
		when:
			def eventId = datastore.saveEvent(eventBytes)
		then:
			eventId
			def eventsMap = datastore.getStoredEvents()
			eventsMap.containsKey(eventId)
			eventsMap.get(eventId) == eventBytes
		where:
			datastore << [new InMemory(), new FileData(FILENAME, 1024, 4096), getEmbeddedH2()]
	}

	def cleanupSpec() {
		new File(FILENAME).delete()
	}

	private EmbeddedH2 getEmbeddedH2() {
		new AnnotationConfigApplicationContext(H2Config).getBean(EmbeddedH2)
	}

	@Configuration
	private static class H2Config {

		@Bean
		EmbeddedH2 embeddedH2() {
			new EmbeddedH2("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
		}

	}
}
