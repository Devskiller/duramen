package eu.codearte.duramen.datastore;

import com.google.common.base.Strings;
import org.h2.Driver;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Jakub Kubrynski / 2014-03-25
 */
@Component
public class EmbeddedH2 implements Datastore, ResourceLoaderAware {

	private static final String DEFAULT_FILENAME = "duramen.data";

	private ResourceLoader resourceLoader;

	private String filename;

	private JdbcTemplate jdbcTemplate;
	private SimpleDriverDataSource dataSource;

	@SuppressWarnings("UnusedDeclaration")
	public EmbeddedH2() {
		this(DEFAULT_FILENAME);
	}

	public EmbeddedH2(String filename) {
		checkArgument(!Strings.isNullOrEmpty(filename));
		this.filename = filename;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@PostConstruct
	public void init() throws SQLException {
		dataSource = new SimpleDriverDataSource(new Driver(), "jdbc:h2:file:" + filename);
		DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
		ResultSet events = metaData.getTables(null, null, "EVENTS", null);
		if (!events.next()) {
			ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
			databasePopulator.addScript(resourceLoader.getResource("datastore.sql"));
			databasePopulator.populate(dataSource.getConnection());
		}
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Long saveEvent(final byte[] bytes) {
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement =
						dataSource.getConnection().prepareStatement("insert into EVENTS(event) values (?)");
				preparedStatement.setBytes(1, bytes);
				return preparedStatement;
			}
		}, generatedKeyHolder);
		return generatedKeyHolder.getKey().longValue();
	}

	@Override
	public void deleteEvent(Long eventId) {
		jdbcTemplate.update("delete from EVENTS where id = ?", eventId);
	}

	@Override
	public Map<Long, byte[]> getStoredEvents() {
		return jdbcTemplate.query("select id, event from EVENTS", new ResultSetExtractor<Map<Long, byte[]>>() {
			@Override
			public Map<Long, byte[]> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, byte[]> result = new HashMap<>();
				while (rs.next()) {
					result.put(rs.getLong("id"), rs.getBytes("event"));
				}
				return result;
			}
		});
	}
}
