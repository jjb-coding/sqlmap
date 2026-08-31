package com.github.jblair.sqlmap.service;

import com.github.jblair.sqlmap.exceptions.BuilderException;
import com.github.jblair.sqlmap.service.adapters.InputMapper;
import com.github.jblair.sqlmap.service.adapters.OutputMapper;
import com.github.jblair.sqlmap.service.adapters.OutputResultsMapper;
import com.github.jblair.sqlmap.service.providers.*;
import com.github.jblair.sqlmap.service.providers.defaults.DefaultNameProvider;
import com.github.jblair.sqlmap.service.providers.defaults.ListClassProvider;
import com.github.jblair.sqlmap.service.providers.defaults.ReflectionClassProvider;
import com.github.jblair.sqlmap.service.providers.defaults.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to configure an APIService instance. An APIService will
 * not be usable until a configuration object has been passed to
 * its build() method.
 * Registers custom mappers & configurers, and creates and configures
 * providers and validation rules.
 */
public class APIServiceConfigurationBuilder {
    // ----- NESTED
    public enum Domain {
        Output,
        OutputResults,
        Both
    }

    // ----- STATIC
    /**
     * Constructs a DefaultNameProvider object.
     * @return              The object
     */
    @SuppressWarnings("unused")
    public static DefaultNameProvider createDefaultNameProvider() {
        return new DefaultNameProvider();
    }

    // ----- DYNAMIC
    // *** FIELDS
    // Packages
    private String base;
    // Providers
    IClassProvider inputProvider;
    ISessionProvider sessionProvider;
    IConnectionProvider connectionProvider;
    INameProvider nameProvider;
    // Rules
    IRule outputRule, outputResultsRule, bothRule;
    // Custom Mappers & Configurers
    Map<Class<?>, InputMapper> classToInputMapper;
    Map<Class<?>, Integer> classToOutputConfigurer;
    Map<Class<?>, OutputMapper> classToOutputMapper;
    Map<Class<?>, OutputResultsMapper> classToOutputResultsMapper;

    // *** CONSTRUCTORS
    /**
     * Constructs an API Service Configuration Builder.
     */
    public APIServiceConfigurationBuilder() {
        // Packages
        base = "";
        // Providers
        inputProvider = null;
        sessionProvider = null;
        connectionProvider = null;
        nameProvider = null;
        // Rules
        outputRule = outputResultsRule = bothRule = null;
        // * Initialise
        // Custom Mappers & Configurers
        classToInputMapper = new HashMap<>();
        classToOutputConfigurer = new HashMap<>();
        classToOutputMapper = new HashMap<>();
        classToOutputResultsMapper = new HashMap<>();
    }

    // *** METHODS
    // ** PUBLIC

    // * Mappers & Configurers
    /**
     * Registers an input mapper against a class.
     * Will throw a BuilderException if already associated with a class.
     * @param fieldCls		The class
     * @param mapper		The mapper
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder addInputMapper(Class<?> fieldCls, InputMapper mapper) {
        if (classToInputMapper.put(fieldCls, mapper) != null)
            throw new BuilderException("API:configuration: Class was already associated with an input mapper");
        return this;
    }

    /**
     * Registers an SQL type constant against a class.
     * Will throw a BuilderException if already associated with a type constant.
     * @param fieldCls		The class
     * @param type			The SQL type constant
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder addOutputConfigurer(Class<?> fieldCls, int type) {
        if (classToOutputConfigurer.put(fieldCls, type) != null)
            throw new BuilderException("API:configuration: Class already associated with a type constant");
        return this;
    }

    /**
     * Registers an output mapper against a class.
     * Will throw a BuilderException if already associated with a class.
     * @param fieldCls		The class
     * @param mapper		The mapper
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder addOutputMapper(Class<?> fieldCls, OutputMapper mapper) {
        if (classToOutputMapper.put(fieldCls, mapper) != null)
            throw new BuilderException("API:configuration: Class was already associated with an output mapper");
        return this;
    }

    /**
     * Registers an outputResults mapper against a class.
     * Will throw a BuilderException if already associated with a class.
     * @param fieldCls		The class
     * @param mapper		The mapper
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder addOutputResultsMapper(Class<?> fieldCls, OutputResultsMapper mapper) {
        if (classToOutputResultsMapper.put(fieldCls, mapper) != null)
            throw new BuilderException("API:configuration: Class was already associated with an outputResults mapper");
        return this;
    }

    // * Builder / Rules / Package
    /**
     * Determines if discovered records are in a specific package.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsIn(Domain domain, String packageString) {
        domainToRule(domain, new Rule(new ReflectionClassProvider(prepend(packageString)), Rule.Type.InProvider));
        return this;
    }

    /**
     * Determines if a specific package is solely composed of discovered records.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsCover(Domain domain, String packageString) {
        domainToRule(domain, new Rule(new ReflectionClassProvider(prepend(packageString)), Rule.Type.CoverProvider));
        return this;
    }

    /**
     * Determines if discovered records are identical to the contents of a specific package.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsEqual(Domain domain, String packageString) {
        domainToRule(domain, new Rule(new ReflectionClassProvider(prepend(packageString)), Rule.Type.EqualProvider));
        return this;
    }

    // * Builder / Rules / Classes
    /**
     * Determines if discovered records are in a specific list of classes.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param classes           The list of classes
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsIn(Domain domain, List<Class<?>> classes) {
        domainToRule(domain, new Rule(new ListClassProvider(classes), Rule.Type.InProvider));
        return this;
    }

    /**
     * Determines if a specific list of classes is solely composed of discovered records.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param classes           The list of classes
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsCover(Domain domain, List<Class<?>> classes) {
        domainToRule(domain, new Rule(new ListClassProvider(classes), Rule.Type.CoverProvider));
        return this;
    }

    /**
     * Determines if discovered records are identical to the contents of a specific list of classes.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param classes           The list of classes
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsEqual(Domain domain, List<Class<?>> classes) {
        domainToRule(domain, new Rule(new ListClassProvider(classes), Rule.Type.EqualProvider));
        return this;
    }

    // * Builder / Rules / Custom Classes Provider
    /**
     * Determines if discovered records are in a specific class provider.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param classes           The class provider
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsIn(Domain domain, IClassProvider classes) {
        domainToRule(domain, new Rule(classes, Rule.Type.InProvider));
        return this;
    }

    /**
     * Determines if a specific class provider is solely composed of discovered records.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param classes           The class provider
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsCover(Domain domain, IClassProvider classes) {
        domainToRule(domain, new Rule(classes, Rule.Type.CoverProvider));
        return this;
    }

    /**
     * Determines if discovered records are identical to the contents of a specific class provider.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param classes           The class provider
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsEqual(Domain domain, IClassProvider classes) {
        domainToRule(domain, new Rule(classes, Rule.Type.EqualProvider));
        return this;
    }

    // * Builder / Rules / Custom Rule
    /**
     * Holds a set of discovered records to a rule.
     * @param domain            Whether the rule validates Output, OutputResults, or both
     * @param rule              The rule
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder validateRecordsWith(Domain domain, IRule rule) {
        domainToRule(domain, rule);
        return this;
    }

    // * Builder / Input Provider
    /**
     * Sets the prefix for all packages used by class providers created
     * through this builder. Can be described in 'a.b', 'a/b' or 'a\b' formats.
     * This must be set before packages are specified.
     * @param base              The prefix
     */
    @SuppressWarnings("unused")
    public void setBasePackage(String base) {
        this.base = base;
    }

    /**
     * Configures the input records to come from a package.
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setInputFromPackage(String packageString) {
        inputProvider = new ReflectionClassProvider(prepend(packageString));
        return this;
    }

    /**
     * Configures the input records to come from a list.
     * @param inputsList        The list of classes
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setInputsFromList(List<Class<?>> inputsList) {
        inputProvider = new ListClassProvider(inputsList);
        return this;
    }

    /**
     * Configures the input records to come from a custom class provider.
     * @param inputProvider     The custom class provider
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setCustomInputProvider(IClassProvider inputProvider) {
        this.inputProvider = inputProvider;
        return this;
    }

    // * Builder / Name Provider
    /**
     * Sets the name provider to the default implementation.
     * @param prefix            The prefix string
     * @param sessionTrue       Included when it is a Session input
     * @param sessionFalse      Included when it is not a Session input
     * @param infix             The string between the two sections
     * @param postfix           The postfix string
     * @param format            The arrangement
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setDefaultNameProvider(
            String prefix,
            String sessionTrue,
            String sessionFalse,
            String infix,
            String postfix,
            DefaultNameProvider.Arrangement format
    ) {
        this.nameProvider = new DefaultNameProvider(prefix, sessionTrue, sessionFalse, infix, postfix, format);
        return this;
    }

    /**
     * Sets the name provider to the default implementation.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setDefaultNameProvider() {
        this.nameProvider = new DefaultNameProvider();
        return this;
    }

    /**
     * Sets the name provider. If not set, defaults to
     * the DefaultNameProvider implementation.
     * @param nameProvider      The name provider
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setNameProvider(INameProvider nameProvider) {
        this.nameProvider = nameProvider;
        return this;
    }

    // * Builder / Providers
    /**
     * Sets the provider of session strings. Not required.
     * @param sessionProvider   The session provider
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setSessionProvider(ISessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
        return this;
    }

    /**
     * Sets the connection provider. Required.
     * @param connectionProvider    The connection provider
     * @return                      Self-returning
     */
    @SuppressWarnings("unused")
    public APIServiceConfigurationBuilder setConnectionProvider(IConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    // ** PACKAGE-PRIVATE
    // * Validators
    void finalise() {
        // Validate
        if (inputProvider == null)
            throw new BuilderException("API:init:configuration: No provider of input records was configured.");
        if (connectionProvider == null)
            throw new BuilderException("API:init:configuration: No SQL connection provider was configured.");

        // Default
        if (nameProvider == null)
            nameProvider = new DefaultNameProvider();
    }

    // ** PRIVATE
    // * String / Packages
    private String prepend(String append) {
        if (append == null)
            append = "";
        append = format(append);
        if (base == null || base == "")
            return append;
        else
            return base + "." + append;
    }

    private String format(String ns) {
        String formattedNS = ns
                .replace('/','.')
                .replace('\\','.')
                .replace(':','.')
                .replaceAll("^\\.+|\\.+$", "");
        return formattedNS;
    }

    // * Helpers
    private void domainToRule(Domain domain, IRule rule) {
        if (domain == Domain.Output)
            outputRule = rule;
        else if (domain == Domain.OutputResults)
            outputResultsRule = rule;
        else if (domain == Domain.Both)
            bothRule = rule;
    }
}
