package com.github.jjbcoding.sqlmap.service;

import com.github.jjbcoding.sqlmap.api.interfaces.APIInput;

/**
 * Locates an APIService instance. This can be extended by the library implementer
 * and registers with APIServiceLocatorSingleton, allowing for {@link APIInput#execute()}
 * to be invoked without needing to provide an APIService in each call.
 */
public abstract class APIServiceLocator {
    // ----- ABSTRACT
    // *** METHODS
    public abstract APIService getAPIService();
}
