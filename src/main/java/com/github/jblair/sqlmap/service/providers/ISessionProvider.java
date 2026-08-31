package com.github.jblair.sqlmap.service.providers;

/**
 * Interface for session providers.
 * A session provider supplies a set of strings to be prepended to
 * any APIInput annotated with ExpectsSession.
 */
public interface ISessionProvider {
    /**
     * If the session does not exist, and an
     * API call is made that expects Session,
     * APIService will throw an ExecutionException.
     * @return  If the session exists
     */
    boolean sessionExists();

    /**
     * Gets the session strings. Should not be null.
     * @return  The session strings
     */
    String[] getStrings();

    /**
     * Gets the number of session strings. This is used
     * during the APIService build process, and can
     * supply count before any session strings are
     * actually available.
     * @return  The number of session strings
     */
    int getNumberOfStrings();
}
