package io.codearte.duramen.datastore;

import io.codearte.duramen.generator.IdGenerator;
import io.codearte.duramen.generator.RandomIdGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation mainly for testing purposes. Does not persist events
 *
 * @author Jakub Kubrynski
 */
public class InMemory implements Datastore {

	private final IdGenerator randomIdGenerator = new RandomIdGenerator();
	private Map<Long, byte[]> hashMap;

	@SuppressWarnings("UnusedDeclaration")
	public InMemory() {
		hashMap = new ConcurrentHashMap<>();
	}

	@Override
	public Long saveEvent(byte[] eventAsBytes) {
		Long id = randomIdGenerator.getNextId();
		hashMap.put(id, eventAsBytes);
		return id;
	}

	@Override
	public void deleteEvent(Long eventId) {
		hashMap.remove(eventId);
	}

	@Override
	public Map<Long, byte[]> getStoredEvents() {
		return new HashMap<>(hashMap);
	}

	@Override
	public long size() {
		return hashMap.size();
	}
}
