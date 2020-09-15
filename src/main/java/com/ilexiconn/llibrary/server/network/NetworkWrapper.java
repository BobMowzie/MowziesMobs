package com.ilexiconn.llibrary.server.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Assign a new network wrapper instance to the field. The channel name will be the mod id.
 *
 * @author iLexiconn
 * @since 1.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NetworkWrapper {
    /**
     * @return an array of classes of abstract messages to be registered
     */
    Class<? extends AbstractMessage<?>>[] value() default {};
}
