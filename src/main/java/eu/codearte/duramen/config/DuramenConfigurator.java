package eu.codearte.duramen.config;

import eu.codearte.duramen.EventBus;
import eu.codearte.duramen.handler.EventHandler;
import org.h2.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.GenericTypeResolver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Component
public class DuramenConfigurator implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();

		EventBus eventBus;

		try {
			eventBus = applicationContext.getBean(EventBus.class);
		} catch (NoUniqueBeanDefinitionException e) {
			LOG.error("More than one EventBus bean found!");
			return;
		} catch (NoSuchBeanDefinitionException e) {
			LOG.error("No EventBus bean has been found");
			return;
		}

		Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		Collection<EventHandler> eventHandlers = beans.values();

		if (eventHandlers.isEmpty()) {
			LOG.warn("No event handler beans have been found");
			return;
		}

		for (EventHandler eventHandler : eventHandlers) {
			Class<?> generic = GenericTypeResolver.resolveTypeArgument(eventHandler.getClass(), EventHandler.class);
			LOG.debug("Registering {} handler for processing {} events",
					eventHandler.getClass().getSimpleName(),
					generic.getSimpleName());
			eventBus.register(generic.getCanonicalName(), eventHandler);
		}

		SimpleDriverDataSource dataSource = new SimpleDriverDataSource(new Driver(), "jdbc:h2:file:/tmp/test/db.h2");
		try {
			Connection connection = dataSource.getConnection();
			connection.prepareStatement("CREATE TABLE EVENTS (NAME VARCHAR(255))").executeUpdate();
			connection.prepareStatement("INSERT INTO TEST VALUES('Hello')");
			connection.prepareStatement("select * from test").executeQuery();
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
}
