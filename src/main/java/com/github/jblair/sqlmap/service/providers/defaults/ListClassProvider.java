package com.github.jblair.sqlmap.service.providers.defaults;

import com.github.jblair.sqlmap.service.providers.IClassProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic list-based class provider.
 */
public class ListClassProvider
    implements IClassProvider {
    // ----- DYNAMIC
    // *** FIELDS
    List<Class<?>> classes;

    // *** CONSTRUCTORS
    /**
     * Constructs a ListClassProvider instance.
     * @param classes   The classes
     */
    public ListClassProvider(List<Class<?>> classes) {
        this.classes = classes;
    }

    /**
     * Constructs a ListClassProvider instance.
     */
    public ListClassProvider() {
        this.classes = new ArrayList<>();
    }

    // *** METHODS
    // ** PUBLIC
    // * Addition
    /**
     * Adds a class to the list.
     * @param cls       The class
     */
    public void add(Class<?> cls) {
        classes.add(cls);
    }

    /**
     * Adds multiple classes to the list.
     * @param classes   The classes
     */
    public void add(Iterable<Class<?>> classes) {
        for (Class<?> cls : classes)
            this.classes.add(cls);
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IClassProvider ]
    @Override
    public List<Class<?>> getClasses() {
        return classes;
    }
}
