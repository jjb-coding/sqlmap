package com.github.jjbcoding.sqlmap.service.adapters.enums;

import com.github.jjbcoding.sqlmap.service.adapters.OutputMapper;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OutputMapperEnum
    extends OutputMapper {
    Map<String, Object> nameToConstantMap;

    public OutputMapperEnum(Object[] constants) {
        nameToConstantMap = new HashMap<>();
        for (Object constant : constants)
            nameToConstantMap.put(constant.toString(), constant);
    }

    @Override
    public Object invoke(int i, CallableStatement statement) throws SQLException {
        String name = statement.getString(i);

        return nameToConstantMap.get(name);
    }
}
