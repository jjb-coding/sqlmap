package com.github.jjbcoding.sqlmap.service.adapters.enums;

import com.github.jjbcoding.sqlmap.service.adapters.InputMapper;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * An InputMapperEnum maps an enum in an Input record into a string representation
 * that can be sent to the procedure. This relationship is computed by invoking the
 * {@link Object#toString()} method on each constant.
 */
public class InputMapperEnum
    extends InputMapper {
    // ----- DYNAMIC
    // *** FIELDS
    Map<Object,String> objectToStringMap;

    // *** CONSTRUCTORS
    /**
     * Constructs an InputMapperEnum instance.
     * @param constants     The list of constants
     */
    public InputMapperEnum(Object[] constants) {
        objectToStringMap = new HashMap<>();
        for (Object constant : constants)
            objectToStringMap.put(constant, constant.toString());
    }

    // ----- INTERFACE IMPLEMENTATIONS
    // * [ Base ]
    /**
     * {@inheritDoc}
     */
    @Override
    public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
        statement.setLong(i, (Long)object);
    }
}
