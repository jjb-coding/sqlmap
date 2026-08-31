package com.github.jblair.sqlmap.api.annotations;

import java.lang.annotation.*;

/**
 * Intended to be applied to a subclass of APIInput. Defines the output
 * class, which should be a subclass of APIOutput. This cannot be measured
 * from the type parameter of the APIInput class due to runtime type erasure,
 * but both the value of this annotation, and the type parameter should match precisely.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Output {
	Class<?> value();
}
