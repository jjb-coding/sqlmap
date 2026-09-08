package com.github.jjbcoding.sqlmap.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intended to be applied to any parameter in an APIOutput subclass record that
 * should be filled by a result set.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT})
public @interface ResultSet {}