package eu.codearte.duramen.datastore;

import com.google.common.base.Strings;
import eu.codearte.duramen.generator.RandomIdGenerator;
import net.openhft.collections.SharedHashMapBuilder;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by jkubrynski@gmail.com / 2014-05-19
 */
public class FileData implements Datastore {

	public static final int DEFAULT_ENTRY_SIZE = 4096;
	public static final String DEFAULT_FILENAME = "duramen.data";
	public static final int DEFAULT_ENTRIES = 1000;

	private final RandomIdGenerator randomIdGenerator = new RandomIdGenerator();

	private final Map<Long, byte[]> sharedHashMap;

	@SuppressWarnings("UnusedDeclaration")
	public FileData() throws IOException {
		this(DEFAULT_FILENAME);
	}

	public FileData(String filename) throws IOException {
		this(filename, DEFAULT_ENTRIES, DEFAULT_ENTRY_SIZE);
	}

	public FileData(String filename, int entries, int entrySize) throws IOException {
		checkArgument(!Strings.isNullOrEmpty(filename));
		checkArgument(entries > 0);
		checkArgument(entrySize > 0);

		sharedHashMap = new SharedHashMapBuilder()
				.entries(entries)
				.entrySize(entrySize)
				.create(new File(filename), Long.class, byte[].class);
	}

	@Override
	public Long saveEvent(byte[] bytes) {
		long id = randomIdGenerator.getNextId();
		sharedHashMap.put(id, bytes);
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

	@PreDestroy
	public void close() throws IOException {
		((Closeable) sharedHashMap).close();
	}
}
