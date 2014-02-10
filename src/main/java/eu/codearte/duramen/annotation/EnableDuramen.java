package eu.codearte.duramen.annotation;

import eu.codearte.duramen.config.DuramenConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DuramenConfiguration.class)
public @interface EnableDuramen {

}
