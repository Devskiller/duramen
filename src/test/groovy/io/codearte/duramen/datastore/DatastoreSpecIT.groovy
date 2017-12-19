package io.codearte.duramen.datastore

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
			datastore.size() == 1
			datastore.deleteEvent(eventId)
			datastore.size() == 0
		where:
			datastore << [new InMemory(), new FileData(FILENAME, 1024, 4096)]
	}

	def cleanupSpec() {
		new File(FILENAME).delete()
	}

}
