package com.github.jjbcoding.sqlmap.service.adapters.enums;

import com.github.jjbcoding.sqlmap.service.adapters.OutputMapper;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * An OutputMapperEnum maps a string in a response into an enum in an Output record.
 * This relationship is computed by invoking the {@link Object#toString()} method on
 * each constant.
 */
public class OutputMapperEnum
    extends OutputMapper {
    // ----- DYNAMIC
    // *** FIELDS
    Map<String, Object> nameToConstantMap;

    // *** CONSTRUCTORS
    /**
     * Constructs an OutputMapperEnum instance.
     * @param constants     The list of constants
     */
    public OutputMapperEnum(Object[] constants) {
        nameToConstantMap = new HashMap<>();
        for (Object constant : constants)
            nameToConstantMap.put(constant.toString(), constant);
    }

    // ----- INTERFACE IMPLEMENTATIONS
    // * [ Base ]
    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(int i, CallableStatement statement) throws SQLException {
        String name = statement.getString(i);

        return nameToConstantMap.get(name);
    }
}
