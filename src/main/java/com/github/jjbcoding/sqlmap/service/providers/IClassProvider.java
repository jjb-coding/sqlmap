package com.github.jjbcoding.sqlmap.service.providers;

import java.util.List;

/**
 * Interface for class providers.
 * A class provider produces a list of classes on request.
 */
public interface IClassProvider {
    /**
     * Gets the list of classes.
     * @return  The list of classes
     */
    List<Class<?>> getClasses();
}
