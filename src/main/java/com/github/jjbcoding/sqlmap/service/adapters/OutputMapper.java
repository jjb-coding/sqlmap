package com.github.jjbcoding.sqlmap.service.adapters;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * An Output Mapper retrieves an item from an executed statement by index.
 */
public abstract class OutputMapper {
    /**
     * Invokes the OutputMapper.
     * @param i                 The index of the parameter
     * @param statement         The callable statement
     * @throws SQLException     if the CallableStatement is misconfigured
     */
    public abstract Object invoke(int i, CallableStatement statement) throws SQLException;
}
