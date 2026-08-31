package com.github.jblair.sqlmap.service.providers;

/**
 * Interface for name providers.
 * A name provider maps the name of an APIInput record, and whether it
 * expects Session strings, into the name of a corresponding Stored Procedure
 * in the SQL server according to a user-defined or default schema.
 */
public interface INameProvider {
    /**
     * Gets the name of the Stored Procedure.
     * @param recordName    The record name
     * @param session       Whether it expects Session strings
     * @return              The Stored Procedure name
     */
    String getName(String recordName, boolean session);
}
