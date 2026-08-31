package com.github.jblair.sqlmap.service.providers.defaults;

import com.github.jblair.sqlmap.service.providers.INameProvider;

/**
 * A generically-formatted APIInput -> Stored Procedure
 * name mapping facility.
 */
public class DefaultNameProvider
    implements INameProvider {
    // ----- NESTED
    public enum Arrangement {
        SessionThenName,
        NameThenSession,
    }
    // ----- DYNAMIC
    // *** FIELDS
    String prefix, sessionTrue, sessionFalse, infix, postfix;
    Arrangement format;

    // *** CONSTRUCTORS
    /**
     * Constructs a DefaultNameProvider. By default, output is:
     *    [name]
     * where
     *    [name] is the name of the record class.
     */
    public DefaultNameProvider() {
        prefix = sessionTrue = sessionFalse = infix = postfix = "";
        format = Arrangement.SessionThenName;
    }

    /**
     * Constructs a DefaultNameProvider. The arrangement determines format:
     *    SessionThenName := prefix + [session] + infix + [name] + postfix
     *    NameThenSession := prefix + [name] + infix + [session] + postfix
     * where
     *    [session] is sessionTrue if expects Session strings, otherwise sessionFalse;
     *    [name] is the name of the record class.
     * @param prefix            The prefix string
     * @param sessionTrue       If it expects Session
     * @param sessionFalse      If it does not expect Session
     * @param infix             The string between the two sections
     * @param postfix           The postfix string
     * @param format            The arrangement
     */
    public DefaultNameProvider(
            String prefix,
            String sessionTrue,
            String sessionFalse,
            String infix,
            String postfix,
            Arrangement format
    ) {
        this.prefix = prefix;
        this.sessionTrue = sessionTrue;
        this.sessionFalse = sessionFalse;
        this.infix = infix;
        this.postfix = postfix;
        this.format = format;
    }

    // *** METHODS
    // ** PUBLIC
    // * Setters
    /**
     * Sets the prefix.
     * @param prefix    The prefix
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public DefaultNameProvider setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the text if it expects Session.
     * @param sessionTrue    The text
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public DefaultNameProvider setSessionTrue(String sessionTrue) {
        this.sessionTrue = sessionTrue;
        return this;
    }

    /**
     * Sets the text if it does not expect Session.
     * @param sessionFalse    The text
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public DefaultNameProvider setSessionFalse(String sessionFalse) {
        this.sessionFalse = sessionFalse;
        return this;
    }

    /**
     * Sets the infix.
     * @param infix     The infix
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public DefaultNameProvider setInfix(String infix) {
        this.infix = infix;
        return this;
    }

    /**
     * Sets the postfix.
     * @param postfix   The postfix
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public DefaultNameProvider setPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }

    // *** INTERFACE IMPLEMENTATION
    // * [ INameProvider ]
    @Override
    public String getName(String recordName, boolean session) {
        return (format == Arrangement.SessionThenName)
                ? (prefix + (session ? sessionTrue : sessionFalse) + infix + recordName + postfix)
                : (prefix + recordName + infix + (session ? sessionTrue : sessionFalse) + postfix);
    }
}
