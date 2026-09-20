package com.github.jjbcoding.sqlmap.service.adapters.enums;

import com.github.jjbcoding.sqlmap.service.adapters.OutputResultsMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * An OutputResultsMapperEnum maps a string in a result set into an enum in an OutputResults
 * record. This relationship is computed by invoking the {@link Object#toString()} method on
 * each constant.
 */
public class OutputResultsMapperEnum
    extends OutputResultsMapper {
    // ----- DYNAMIC
    // *** FIELDS
    Map<String, Object> nameToConstantMap;

    // *** CONSTRUCTORS

    /**
     * Constructs an OutputResultsMapperEnum instance.
     * @param constants     The list of constants
     */
    public OutputResultsMapperEnum(Object[] constants) {
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
    public Object invoke(String name, ResultSet resultSet) throws SQLException {
        return resultSet.getString(name);
    }
}
