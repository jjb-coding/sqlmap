package com.github.jjbcoding.sqlmap.service.adapters;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An Output Results mapper retrieves an item from a row of a result set by name.
 */
public abstract class OutputResultsMapper {
    /**
     * Invokes the OutputResultsMapper.
     * @param name              The name of the column
     * @param resultSet         The result set
     * @throws SQLException     if the ResultSet is misconfigured
     */
    public abstract Object invoke(String name, ResultSet resultSet) throws SQLException;
}
