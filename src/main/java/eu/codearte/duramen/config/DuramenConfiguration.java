package eu.codearte.duramen.config;

import eu.codearte.duramen.DuramenPackageMarker;
import eu.codearte.duramen.datastore.Datastore;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Configuration
@ComponentScan(basePackageClasses = DuramenPackageMarker.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Datastore.class))
public class DuramenConfiguration {

}
