package com.github.jblair.sqlmap.service.providers;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for connection providers.
 * A connection provider produces a java.sql.Connection object on
 * request; this may be integrated into existing systems by the user.
 */
public interface IConnectionProvider {
    /**
     * Gets a Connection object.
     * @return  The Connection object
     */
    Connection getConnection()
        throws SQLException;
}
