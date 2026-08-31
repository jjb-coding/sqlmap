package com.github.jblair.sqlmap.service;

import com.github.jblair.sqlmap.api.interfaces.APIOutputResult;
import com.github.jblair.sqlmap.service.adapters.OutputResultsMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Contains the information retrieved from scanning an output results record.
 */
class OutputResultsConfiguration {
    // ----- DYNAMIC
    // *** FIELDS
    // An array of mappers that take a result set row, and return an object.
    public OutputResultsMapper[] mappers;
    // Names
    public String[] names;
    // The declared constructor of appropriate type
    public Constructor<?> constructor;
    // The number of parameters to pass to the constructor
    public int size;

    // *** CONSTRUCTORS
    /**
     * Scans an output results record and builds a configuration object around it.
     *
     * @param cls The record to scan.
     */
    public OutputResultsConfiguration(APIService _apiService, Class<?> cls) {
        // VALIDATE: is it a record?
        if (!cls.isRecord())
            throw new RuntimeException("API:init:outputResults[" + cls.getSimpleName() + "]: Is not a record class.");

        // Get components & scan
        RecordComponent[] fields = cls.getRecordComponents();
        size = fields.length;
        mappers = new OutputResultsMapper[size];
        names = new String[size];

        for (int i = 0; i < size; i++) {
            // Get data about field
            RecordComponent field = fields[i];
            Class<?> fieldCls = field.getType();
            String fieldName = field.getName();

            // Put into array
            names[i] = fieldName;
            // VALIDATE: is mapper recognised?
            mappers[i] = _apiService.classToOutputResultsMapper.get(fieldCls);
            if (mappers[i] == null)
                throw new RuntimeException("API:init:outputResults[" + cls.getSimpleName() + "]: " + fieldName + ": Field is of unsupported type " + fieldCls.getSimpleName() + ".");
        }

        // Get constructor
        Constructor<?>[] constructors = (Constructor<?>[]) cls.getDeclaredConstructors();

        // VALIDATE: is there only 1 constructor?
        if (constructors.length != 1)
            throw new RuntimeException("API:init:outputResults[" + cls.getSimpleName() + "]: Has 0 or more than 1 constructors.");
        constructor = constructors[0];
    }

    // *** METHODS
    // ** PUBLIC
    /**
     * Executes this configuration.
     *
     * @param statement The executed statement.
     * @return An array list of output result records, one for each row.
     */
    public ArrayList<APIOutputResult> execute(CallableStatement statement) {
        // Get a result set
        ResultSet resultSet;
        try {
            resultSet = statement.getResultSet();
        } catch (Exception e) {
            throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Couldn't get result set.");
        }

        // Make an ArrayList
        ArrayList<APIOutputResult> list = new ArrayList<>();

        // For each result
        try {
            while (resultSet.next()) {
                // Map values out of the result set and into an Object[] array
                Object[] values = new Object[names.length];
                for (int i = 0; i < names.length; i++)
                    try {
                        values[i] = mappers[i].invoke(names[i], resultSet);
                    } catch (Exception e) {
                        throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not invoke outputResults mapper.");
                    }

                // Construct the output record & add to list
                try {
                    list.add((APIOutputResult) constructor.newInstance(values));
                } catch (Exception e) {
                    throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not construct a row record.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Error retrieving result set.");
        }

        // Return list
        return list;
    }
}
