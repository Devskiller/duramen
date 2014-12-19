package io.codearte.duramen.annotation;

import io.codearte.duramen.config.DuramenSpringConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Root Duramen configuration annotation
 * Annotate your @Configuration class to enable Duramen in your project
 *
 * @author Jakub Kubrynski
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DuramenSpringConfiguration.class)
public @interface EnableDuramen {

}
