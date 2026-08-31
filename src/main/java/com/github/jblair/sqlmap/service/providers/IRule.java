package com.github.jblair.sqlmap.service.providers;

/**
 * Interface for validation rules for the APIService build process.
 */
public interface IRule {
    /**
     * Applies the rule.
     * @param discoveredClasses The set of discovered classes attached to the rule
     * @return                  If it validates
     */
    boolean isValid(Iterable<Class<?>> discoveredClasses);
}
