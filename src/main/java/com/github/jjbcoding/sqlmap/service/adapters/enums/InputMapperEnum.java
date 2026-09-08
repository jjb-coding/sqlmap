package com.github.jjbcoding.sqlmap.service.adapters.enums;

import com.github.jjbcoding.sqlmap.service.adapters.InputMapper;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InputMapperEnum
    extends InputMapper {
    Map<Object,String> objectToStringMap;

    public InputMapperEnum(Object[] constants) {
        objectToStringMap = new HashMap<>();
        for (Object constant : constants)
            objectToStringMap.put(constant, constant.toString());
    }

    @Override
    public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
        statement.setLong(i, (Long)object);
    }
}
