package com.github.jjbcoding.sqlmap.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intended to be applied to a subclass of APIInput. Represents whether the request
 * type carries session data or not.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExpectsSession {}
