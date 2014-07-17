package eu.codearte.duramen.datastore;

import com.google.common.base.Strings;
import org.h2.Driver;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation based on embedded H2 database
 *
 * @author Jakub Kubrynski
 */
public class EmbeddedH2 extends RelationalDB {

	private static final String DEFAULT_URL = "jdbc:h2:file:~/duramen.data";

	private String url;

	/**
	 * This constructor creates file database localized in duramen.data file
	 */
	public EmbeddedH2() {
		this(DEFAULT_URL);
	}

	/**
	 * Creates database using provided URL
	 *
	 * @param url full database url, eg. "jdbc:h2:mem:myDatabase";
	 */
	public EmbeddedH2(String url) {
		checkArgument(!Strings.isNullOrEmpty(url));
		this.url = url;
	}

	@Override
	protected AbstractDataSource getDataSource() {
		return new SimpleDriverDataSource(new Driver(), url);
	}

}
