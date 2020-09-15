package com.ilexiconn.llibrary.server.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Assign a new config instance to the wrappedField and register it.
 *
 * @author iLexiconn
 * @since 1.2.0
 * @deprecated Use Forge config
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Config {

}
