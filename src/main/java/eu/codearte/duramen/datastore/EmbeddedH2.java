package eu.codearte.duramen.datastore;

import com.google.common.base.Strings;
import org.h2.Driver;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
public class EmbeddedH2 extends RelationalDB {

	private static final String DEFAULT_FILENAME = "jdbc:h2:file:duramen.data";

	private String filename;

	public EmbeddedH2() {
		this(DEFAULT_FILENAME);
	}

	public EmbeddedH2(String filename) {
		checkArgument(!Strings.isNullOrEmpty(filename));
		this.filename = filename;
	}

	@Override
	protected AbstractDataSource getDataSource() {
		return new SimpleDriverDataSource(new Driver(), filename);
	}

}
