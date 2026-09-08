package com.github.jjbcoding.sqlmap.service.providers.defaults;

import com.github.jjbcoding.sqlmap.service.providers.IClassProvider;
import com.github.jjbcoding.sqlmap.service.providers.IRule;

import java.util.HashSet;
import java.util.List;

/**
 * A validation rule that compares a discovered set of classes
 * against a provided one.
 */
public class Rule
    implements IRule {
    // ----- NESTED
    /**
     * Constants are:
     *    InProvider    := D is a subset of P;
     *    CoverProvider := P is a subset of D;
     *    EqualProvider := D == P
     * where
     *    D is the discovered set of classes
     *    P is the provided set of classes
     */
    public enum Type {
        InProvider,
        CoverProvider,
        EqualProvider
    }
    // ----- DYNAMIC
    // *** FIELDS
    IClassProvider classProvider;
    Type type;

    // *** CONSTRUCTORS
    /**
     * Constructs a rule.
     * @param classProvider     The provided classes
     * @param type              The type of rule
     */
    public Rule(IClassProvider classProvider, Type type) {
        this.classProvider = classProvider;
        this.type = type;
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IValidationProvider ]
    @Override
    public boolean isValid(Iterable<Class<?>> discoveredClasses) {
        // Get
        List<Class<?>> containerClasses = classProvider.getClasses();

        // Validate
        if (type == Type.InProvider || type == Type.EqualProvider) {
            HashSet<Class<?>> containerSet = new HashSet<>(containerClasses);
            for (Class<?> discoveredClass : discoveredClasses)
                if (!containerSet.contains(discoveredClass))
                    return false;
        }
        if (type == Type.CoverProvider || type == Type.EqualProvider) {
            HashSet<Class<?>> discoveredSet = new HashSet<>();
            for (Class<?> cls : discoveredClasses)
                discoveredSet.add(cls);

            for (Class<?> containerClass : containerClasses)
                if (!discoveredSet.contains(containerClass))
                    return false;
        }

        // Return
        return true;
    }
}
