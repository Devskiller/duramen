package eu.codearte.duramen.datastore;

import com.google.common.primitives.Longs;
import net.openhft.collections.SharedHashMapBuilder;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jkubrynski@gmail.com / 2014-05-19
 */
public class SharedHashMap implements Datastore {

	private static final String DEFAULT_FILENAME = "duramen.data";
	private static final long ID_KEY = 0l;

	private final Map<Long, byte[]> sharedHashMap;

	@SuppressWarnings("UnusedDeclaration")
	public SharedHashMap() throws IOException {
		this(DEFAULT_FILENAME);
	}

	public SharedHashMap(String filename) throws IOException {
		sharedHashMap = new SharedHashMapBuilder().create(new File(filename), Long.class, byte[].class);
		// in case of new or empty map resetting id counter
		if (!sharedHashMap.containsKey(ID_KEY) || sharedHashMap.size() == 1) {
			sharedHashMap.put(ID_KEY, Longs.toByteArray(1l));
		}
	}

	@Override
	public synchronized Long saveEvent(byte[] bytes) {
		long id = Longs.fromByteArray(sharedHashMap.get(ID_KEY)) + 1;
		sharedHashMap.put(id, bytes);
		sharedHashMap.put(ID_KEY, Longs.toByteArray(id));
		return id;
	}

	@Override
	public void deleteEvent(Long eventId) {
		sharedHashMap.remove(eventId);
	}

	@Override
	public Map<Long, byte[]> getStoredEvents() {
		HashMap<Long, byte[]> longHashMap = new HashMap<>(sharedHashMap);
		longHashMap.remove(ID_KEY);
		return longHashMap;
	}

	@PreDestroy
	public void close() throws IOException {
		((Closeable) sharedHashMap).close();
	}
}
