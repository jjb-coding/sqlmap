package com.github.jblair.sqlmap.service.adapters;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * An Output Mapper retrieves an item from an executed statement by index.
 */
public abstract class OutputMapper {
    public abstract Object invoke(int i, CallableStatement statement) throws SQLException;
}
