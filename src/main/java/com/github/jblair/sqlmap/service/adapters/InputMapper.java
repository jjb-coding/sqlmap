package com.github.jblair.sqlmap.service.adapters;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * An Input Mapper puts an item into a statement that has not yet been executed by index.
 */
public abstract class InputMapper {
    public abstract void invoke(int i, Object object, CallableStatement statement) throws SQLException;
}
