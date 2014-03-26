package eu.codearte.duramen.datastore;

import java.util.Map;

/**
 * Created by Jakub Kubrynski / 2014-03-25
 */
public interface Datastore {

	Long saveEvent(byte[] bytes);

	void deleteEvent(Long eventId);

	Map<Long, byte[]> getStoredEvents();
}
