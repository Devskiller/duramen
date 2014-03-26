package eu.codearte.duramen.test;

import eu.codearte.duramen.annotation.EnableDuramen;
import eu.codearte.duramen.datastore.Datastore;
import eu.codearte.duramen.datastore.EmbeddedH2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Configuration
@ComponentScan
@EnableDuramen
public class SampleConfiguration {

}