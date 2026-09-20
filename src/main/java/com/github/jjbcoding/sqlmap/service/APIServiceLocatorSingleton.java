package com.github.jjbcoding.sqlmap.service;

/**
 * Hosts a Singleton instance of the APIService.
 */
public enum APIServiceLocatorSingleton {
    // ----- MEMBERS
    INSTANCE
    ;

    // ----- STATIC
    // *** METHODS
    // ** PUBLIC
    /**
     * Retrieves the APIService instance.
     * @return  The APIService
     */
    public static APIService getAPIService() {
        return INSTANCE.wrapper.locator.getAPIService();
    }
    /**
     * Registers an APIService locator instance.
     * @param apiServiceLocator    The APIService locator
     */
    @SuppressWarnings("unused")
    public static void register(APIServiceLocator apiServiceLocator) {
        INSTANCE.wrapper.locator = apiServiceLocator;
    }

    /**
     * Unregisters an APIService locator instance.
     */
    @SuppressWarnings("unused")
    public static void unregister() {
        INSTANCE.wrapper.locator = null;
    }

    // ----- NESTED
    static class APIServiceLocatorWrapper {
        APIServiceLocator locator;
    }

    // ----- DYNAMIC
    // *** FIELDS
    final APIServiceLocatorWrapper wrapper;

    // *** CONSTRUCTORS
    APIServiceLocatorSingleton() {
        wrapper = new APIServiceLocatorWrapper();
    }
}
