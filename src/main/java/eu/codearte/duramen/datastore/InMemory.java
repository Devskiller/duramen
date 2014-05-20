package eu.codearte.duramen.datastore;

import eu.codearte.duramen.generator.RandomIdGenerator;
import net.openhft.collections.HugeConfig;
import net.openhft.collections.HugeHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
public class InMemory implements Datastore {

	private final RandomIdGenerator randomIdGenerator = new RandomIdGenerator();
	private HugeHashMap<Long, byte[]> hashMap;

	@SuppressWarnings("UnusedDeclaration")
	public InMemory() {
		hashMap = new HugeHashMap<>(HugeConfig.SMALL, Long.class, byte[].class);
	}

	@Override
	public Long saveEvent(byte[] bytes) {
		Long id = randomIdGenerator.getNextId();
		hashMap.put(id, bytes);
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
}
