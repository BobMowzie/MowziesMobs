package com.ilexiconn.llibrary.server.nbt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pau101
 * @since 1.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NBTMutatorProperty {
    Class<?> type();

    String name() default "";

    String setter() default "";

    String getter() default "";
}
