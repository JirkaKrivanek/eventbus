package com.kk.bus;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation: Subscribe.
 *
 * @author Jiri Krivanek
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * The token which can be used to filter the subscribers in conjunction with the {@link Token} annotation. The
     * {@link Bus#BROADCAST_TOKEN} value (default) is reserved meaning the subscriber handles ALL the events of
     * given event class (type).
     */
    int token() default Bus.BROADCAST_TOKEN;
}
