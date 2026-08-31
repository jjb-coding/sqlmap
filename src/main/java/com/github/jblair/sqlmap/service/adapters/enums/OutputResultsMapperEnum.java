package com.github.jblair.sqlmap.service.adapters.enums;

import com.github.jblair.sqlmap.service.adapters.OutputResultsMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OutputResultsMapperEnum
    extends OutputResultsMapper {
    Map<String, Object> nameToConstantMap;

    public OutputResultsMapperEnum(Object[] constants) {
        nameToConstantMap = new HashMap<>();
        for (Object constant : constants)
            nameToConstantMap.put(constant.toString(), constant);
    }

    @Override
    public Object invoke(String name, ResultSet resultSet) throws SQLException {
        return (Object)resultSet.getString(name);
    }
}
