package com.github.jjbcoding.sqlmap.service.adapters.enums;

import com.github.jjbcoding.sqlmap.service.adapters.OutputResultsMapper;

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
    @SuppressWarnings("RedundantCast")
    public Object invoke(String name, ResultSet resultSet) throws SQLException {
        return (Object)resultSet.getString(name);
    }
}
