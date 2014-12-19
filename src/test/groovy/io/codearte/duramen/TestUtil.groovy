package io.codearte.duramen

import io.codearte.duramen.datastore.FileData

import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Jakub Kubrynski
 */
class TestUtil {
	static void cleanupDatastore() {
		if (new File(FileData.DEFAULT_FILENAME).exists()) {
			Files.delete(Paths.get(FileData.DEFAULT_FILENAME))
		}
	}
}
