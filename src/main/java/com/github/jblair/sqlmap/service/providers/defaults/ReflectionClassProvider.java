package com.github.jblair.sqlmap.service.providers.defaults;

import com.github.jblair.sqlmap.exceptions.BuilderException;
import com.github.jblair.sqlmap.util.internals.ReflectionHelper;
import com.github.jblair.sqlmap.service.providers.IClassProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A class provider that locates all classes in a package.
 */
public class ReflectionClassProvider
    implements IClassProvider {
    // ----- DYNAMIC
    // *** FIELDS
    ArrayList<Class<?>> inputs;
    String inputPackage;

    // *** CONSTRUCTORS
    public ReflectionClassProvider(String packageString) {
        if (packageString.equals(""))
            throw new BuilderException("API:init:configuration: Package string was be empty.");
        this.inputPackage = packageString;
    }

    // *** METHODS
    @Override
    public List<Class<?>> getClasses() {
        if (inputs == null) {
            try {
                inputs = ReflectionHelper.getClasses(inputPackage);
            } catch (Exception e) {
                throw new BuilderException("API:init: Couldn't get classes from input package.");
            }
        }
        return inputs;
    }
}
