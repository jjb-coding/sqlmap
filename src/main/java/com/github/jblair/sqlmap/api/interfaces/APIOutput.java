package com.github.jblair.sqlmap.api.interfaces;

/**
 * Classifies the return parameters of an API call. Must be implemented by all records to be scanned
 * in the configured output package. The parameters can include ArrayList<T extends OutputResult>
 * fields for one or more result sets.
 */
public interface APIOutput {}