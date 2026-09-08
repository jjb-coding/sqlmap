package com.github.jjbcoding.sqlmap.service.providers.defaults;

import com.github.jjbcoding.sqlmap.service.providers.IRule;

import java.util.ArrayList;
import java.util.List;

/**
 * A rule which validates successfully if any sub-rule validates.
 */
public class RuleAny
    implements IRule {
    // ----- DYNAMIC
    // *** FIELDS
    List<Rule> rules;

    // *** CONSTRUCTORS
    /**
     * Constructs a RuleAny instance.
     * @param rules     The list of rules
     */
    @SuppressWarnings("unused")
    public RuleAny(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Constructs a RuleAny instance.
     */
    public RuleAny() {
        rules = new ArrayList<>();
    }

    // *** METHODS
    /**
     * Adds a rule.
     * @param rule  The rule to add
     * @return      Self-returning
     */
    public RuleAny add(Rule rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Adds many rules.
     * @param rules The rules to add
     * @return      Self-returning
     */
    public RuleAny add(Iterable<Rule> rules) {
        for (Rule rule : rules)
            this.rules.add(rule);
        return this;
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IValidationProvider ]
    @Override
    public boolean isValid(Iterable<Class<?>> discoveredClasses) {
        for (Rule rule : rules)
            if (rule.isValid(discoveredClasses))
                return true;
        return false;
    }
}
