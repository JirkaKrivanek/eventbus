package com.kk.bus;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation: Token.
 * <p>
 * It can annotate the method of the event which must return the int type value which, in turn, can be used to filter
 * the subscribers using the {@link Subscribe#token()} value.
 *
 * @author Jiri Krivanek
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Token {}
