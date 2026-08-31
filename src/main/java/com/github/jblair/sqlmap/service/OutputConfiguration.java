package com.github.jblair.sqlmap.service;

import com.github.jblair.sqlmap.api.interfaces.APIOutput;
import com.github.jblair.sqlmap.api.annotations.ResultSet;
import com.github.jblair.sqlmap.exceptions.BuilderException;
import com.github.jblair.sqlmap.exceptions.ExecutionException;
import com.github.jblair.sqlmap.service.adapters.OutputMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the information retrieved from scanning an output record.
 */
class OutputConfiguration {
    // ----- DYNAMIC
    // *** FIELDS
    // An array of configurers that are used to prepare the statement to receive output.
    public Integer[] configurers;
    // An array that is a mixture of
    //		mappers that take a callable statement, and return an object;
    // 	and	OutputResultsConfiguration objects.
    public Object[] mappers;
    // The declared constructor of appropriate type
    public Constructor<?> constructor;
    // The number of OUT parameters
    public int size;
    // The parameter at which output starts
    public int offset;

    // *** CONSTRUCTORS
    /**
     * Scans an output record and builds a configuration object around it.
     *
     * @param cls The record to scan.
     */
    @SuppressWarnings("RedundantCast")
    public OutputConfiguration(APIService _apiService, Class<?> cls, int offset) {
        // Register output
        _apiService.discoverOutput(cls);

        // Capture offset
        this.offset = offset + 1;

        // VALIDATE: is it a record?
        if (!cls.isRecord())
            throw new RuntimeException("API:init: Output [" + cls.getSimpleName() + "]: Is not a record class.");

        // Get components & scan
        RecordComponent[] fields = cls.getRecordComponents();
        ArrayList<Object> mappersList = new ArrayList<>();
        ArrayList<Integer> configurersList = new ArrayList<>();
        size = 0;
        for (RecordComponent field : fields) {
            // Get data about field
            Class<?> fieldCls = field.getType();
            String fieldName = field.getName();

            // Determine if the field represents is a result set or a variable output
            if (field.isAnnotationPresent(ResultSet.class)) {
                // A result set field
                // VALIDATE: is it a list?
                if (!List.class.isAssignableFrom(fieldCls))
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": Is marked ResultSet but not a list type.");

                // VALIDATE: is it a raw type?
                Type parameterGeneric = field.getGenericType();
                if (!(parameterGeneric instanceof ParameterizedType))
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List is raw.");

                // VALIDATE: is there only 1 parameter?
                Type[] parameters = ((ParameterizedType)parameterGeneric).getActualTypeArguments();
                if (parameters.length != 1)
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List must only have 1 type parameter.");

                // VALIDATE: must be a class type
                Type parameterType = parameters[0];
                if (!(parameterType instanceof Class<?> parameter))
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List is parametrised by a type that is not a class.");

                // VALIDATE:
                if (!(parameter.isRecord()))
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List is parametrised by a type that is not a record.");

                // Add to mappers list
                mappersList.add(new OutputResultsConfiguration(_apiService, parameter));
            } else {
                // A variable field
                size++;

                // VALIDATE: is mapper recognised?
                boolean isEnum = fieldCls.isEnum();

                OutputMapper mapper;
                if (isEnum)
                    mapper = _apiService.getOrCreateOutputMapperEnum(fieldCls);
                else
                    mapper = _apiService.classToOutputMapper.get(fieldCls);

                if (mapper == null)
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": Mapper: Field is of unsupported type " + fieldCls.getSimpleName() + ".");

                // Add to mappers list
                mappersList.add(mapper);

                // VALIDATE: is configurer recognised?
                Integer configurer;
                if (isEnum)
                    configurer = _apiService.classToOutputConfigurer.get(String.class);
                else
                    configurer = _apiService.classToOutputConfigurer.get(fieldCls);

                if (configurer == null)
                    throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: " + fieldName + ": Configurer: Field is of unsupported type " + fieldCls.getSimpleName() + ".");

                // Add to configurers list
                configurersList.add(configurer);
            }
        }

        // Finalise mappers & results
        mappers = mappersList.toArray(Object[]::new);
        configurers = configurersList.toArray(Integer[]::new);

        // Get constructor
        Constructor<?>[] constructors = (Constructor<?>[]) cls.getDeclaredConstructors();

        // VALIDATE: is there only 1 constructor?
        if (constructors.length != 1)
            throw new BuilderException("API:init:output[" + cls.getSimpleName() + "]: Has 0 or more than 1 constructors.");
        constructor = constructors[0];
    }

    // *** PUBLIC METHODS
    // ** PUBLIC
    /**
     * Executes this configuration.
     *
     * @param statement The executed statement.
     * @return An output record, with associated lists of output results records if appropriate.
     */
    public APIOutput execute(CallableStatement statement) {
        // Map values out of the statement and into an Object[] array
        Object[] values = new Object[mappers.length];
        for (int i = 0; i < mappers.length; i++) {
            Object mapper = mappers[i];
            if (mapper instanceof OutputMapper)
                try {
                    values[i] = ((OutputMapper) mapper).invoke(i + offset, statement);
                } catch (Exception e) {
                    throw new BuilderException("API:exec:output[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not invoke output mapper.");
                }
            else if (mapper instanceof OutputResultsConfiguration)
                values[i] = ((OutputResultsConfiguration) mapper).execute(statement);
        }

        // Construct the output record & return
        Object output;
        try {
            output = constructor.newInstance(values);
        } catch (Exception e) {
            throw new BuilderException("API:exec:output[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not construct output record.");
        }
        return (APIOutput) output;
    }

    // ** PACKAGE-PRIVATE
    /**
     * Configures a statement according to its output elements.
     *
     * @param statement The statement.
     */
    void configureStatement(CallableStatement statement) {
        int i = offset;
        for (Integer type : configurers) {
            try {
                statement.registerOutParameter(i, type);
            } catch (SQLException e) {
                throw new ExecutionException("API:exec:output: Failed to configure the callable statement.");
            }
            i++;
        }
    }
}
