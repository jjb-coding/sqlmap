package com.github.jjbcoding.sqlmap.service.adapters;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * An Input Mapper puts an item into a statement that has not yet been executed by index.
 */
public abstract class InputMapper {
    /**
     * Invokes the InputMapper.
     * @param i                 The index of the parameter
     * @param object            The data element
     * @param statement         The callable statement
     * @throws SQLException     if the CallableStatement is misconfigured
     */
    public abstract void invoke(int i, Object object, CallableStatement statement) throws SQLException;
}
