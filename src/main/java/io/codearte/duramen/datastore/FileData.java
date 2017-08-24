package io.codearte.duramen.datastore;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import io.codearte.duramen.generator.IdGenerator;
import io.codearte.duramen.generator.RandomIdGenerator;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Fast implementation based on SharedHashMap from HugeCollections
 *
 * @author Jakub Kubrynski
 */
public class FileData implements Datastore {

	public static final String DEFAULT_FILENAME = "/tmp/duramen.data";

	private final IdGenerator randomIdGenerator = new RandomIdGenerator();

	private final Map<Long, byte[]> sharedHashMap;

	/**
	 * Most detailed constructor allowing creation of fully customized map
	 *
	 * @param path      full path including file name
	 * @param entries   maximum number of events persisted
	 * @param entrySize maximum size of single event
	 * @throws IOException when creating/opening file fails
	 */
	public FileData(String path, int entries, int entrySize) throws IOException {
		checkArgument(!Strings.isNullOrEmpty(path));
		checkArgument(entries > 0);
		checkArgument(entrySize > 0);

		sharedHashMap = ChronicleMapBuilder.of(Long.class, byte[].class)
				.entries(entries)
				.averageValueSize(entrySize)
				.createPersistedTo(new File(path));
	}

	@Override
	public Long saveEvent(byte[] eventAsBytes) {
		long id = randomIdGenerator.getNextId();
		sharedHashMap.put(id, eventAsBytes);
		return id;
	}

	@Override
	public void deleteEvent(Long eventId) {
		sharedHashMap.remove(eventId);
	}

	@Override
	public Map<Long, byte[]> getStoredEvents() {
		return new HashMap<>(sharedHashMap);
	}

	/**
	 * Closes map before destroying application context
	 *
	 * @throws IOException inherited from {@link java.io.Closeable}. Will not be thrown
	 */
	@PreDestroy
	public void close() throws IOException {
		((Closeable) sharedHashMap).close();
	}
}
