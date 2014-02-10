package eu.codearte.duramen.config;

import eu.codearte.duramen.DuramenPackageMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Configuration
@ComponentScan(basePackageClasses = DuramenPackageMarker.class)
public class DuramenConfiguration {

}
