package io.codearte.duramen.datastore;

import java.util.Map;

/**
 * Generic datastore interface. Can be implemented to handle custom
 * data repositories.
 *
 * @author Jakub Kubrynski
 */
public interface Datastore {

	/**
	 * Persists event under unique id
	 *
	 * @param eventAsBytes serialized event
	 * @return generated event unique identifier
	 */
	Long saveEvent(byte[] eventAsBytes);

	/**
	 * Deletes event from persistent store
	 *
	 * @param eventId event identifier
	 */
	void deleteEvent(Long eventId);

	/**
	 * Returns all persisted events. Used after starting event bus to process
	 * events persisted before closing context and not yet processed
	 *
	 * @return events Map&lt;eventId, eventAsBytes&gt;
	 */
	Map<Long, byte[]> getStoredEvents();

	long size();
}
