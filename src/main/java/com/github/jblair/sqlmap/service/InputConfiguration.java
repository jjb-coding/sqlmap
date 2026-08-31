package com.github.jblair.sqlmap.service;

import com.github.jblair.sqlmap.api.interfaces.APIInput;
import com.github.jblair.sqlmap.api.annotations.ExpectsSession;
import com.github.jblair.sqlmap.api.annotations.Output;
import com.github.jblair.sqlmap.exceptions.BuilderException;
import com.github.jblair.sqlmap.exceptions.ExecutionException;
import com.github.jblair.sqlmap.exceptions.DatabaseException;
import com.github.jblair.sqlmap.service.adapters.InputMapper;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Contains the information retrieved from scanning an input record.
 */
class InputConfiguration {
    // ----- STATIC
    // *** METHODS
    // ** PRIVATE
    private static boolean expectsSession(Class<?> cls) {
        return cls.isAnnotationPresent(ExpectsSession.class);
    }

    // ----- DYNAMIC
    // *** FIELDS
    // * Services
    APIService _apiService;

    // * Other
    // An array of mappers that take an object, cast it, and put it into a callable statement.
    public Method[] accessors;
    // An array of accessors that can be invoked on an object, in order.
    public InputMapper[] mappers;
    // The configured output record.
    public OutputConfiguration outputConfiguration;
    // String to build callable statement
    String statementString;
    // Whether the call has session fields
    boolean expectsSession;

    // *** CONSTRUCTORS
    /**
     * Scans an input record and builds a configuration object around it.
     * @param _apiService   The API service.
     * @param cls           The record to scan.
     */
    public InputConfiguration(APIService _apiService, Class<?> cls) {
        // * Bank
        this._apiService = _apiService;

        // VALIDATE: is it a record?
        if (!cls.isRecord())
            throw new RuntimeException("API:init:input[" + cls.getSimpleName() + "]: Is not a record class.");

        // Get components
        RecordComponent[] fields = cls.getRecordComponents();

        // Has session
        expectsSession = expectsSession(cls);
        if (!_apiService.canExposeSession() && expectsSession)
            throw new BuilderException("API:init:input[" + cls.getSimpleName() + "]: Endpoint expects session data but service was not configured with a session provider.");

        // Construct
        accessors = new Method[fields.length];
        mappers = new InputMapper[fields.length];

        // Scan components
        for (int i = 0; i < fields.length; i++) {
            RecordComponent field = fields[i];
            Class<?> fieldCls = field.getType();
            accessors[i] = field.getAccessor();

            // Get mapper
            if (fieldCls.isEnum())
                mappers[i] = _apiService.getOrCreateInputMapperEnum(fieldCls);
            else
                mappers[i] = _apiService.classToInputMapper.get(fieldCls);

            if (mappers[i] == null)
                throw new RuntimeException("API:init:input[" + cls.getSimpleName() + "]: " + field.getName() + ": Field is of unsupported type " + fieldCls.getSimpleName() + ".");
        }

        // Get output
        Class<?> output;
        try {
            Output annotation = cls.getAnnotation(Output.class);
            if (annotation == null)
                throw new BuilderException("API:init:input[" + cls.getSimpleName() + "]: No Output annotation was present.");
            output = annotation.value();
        } catch (NullPointerException e) {
            throw new BuilderException("API:init:input[" + cls.getSimpleName() + "]: Output annotation was misconfigured.");
        }
        int offset = mappers.length + (expectsSession ? _apiService.getSessionStringsCount() : 0);

        // Construct OutputConfiguration
        outputConfiguration = new OutputConfiguration(_apiService, output, offset);

        // Configure call string & build initial statement
        int n = offset + outputConfiguration.size;
        statementString =
                "{call " +
                        _apiService.getName(cls.getSimpleName(), expectsSession) +
                        " (" +
                        ((n == 0) ? "" : String.join(", ", Collections.nCopies(n, "?")))
                        + ")}";
        buildCallableStatement();
    }

    // *** METHODS
    // ** PUBLIC
    /**
     * Calls an API endpoint over an input record object.
     * @param object The input record object.
     * @return The output record object.
     */
    public Object execute(APIInput<?> object) {
        // Validate configuration
        if (!_apiService.isConfigured())
            throw new ExecutionException("API:exec:input: Attempted to execute API request before service was configured.");

        // Get connection
        Connection connection = _apiService.getConnection();

        // Build & execute statement
        CallableStatement statement = buildCallableStatement();
        mapToCallableStatement(object, statement);
        try {
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseException("API:exec:input: Couldn't execute procedure.", e);
        }
        return outputConfiguration.execute(statement);
    }

    // ** PRIVATE METHODS
    // * Callable Statements
    private CallableStatement buildCallableStatement() {
        // Get connection
        Connection connection = _apiService.getConnection();

        // Get a statement
        CallableStatement statement;
        try {
            statement = connection.prepareCall(statementString);
        } catch (SQLException e) {
            throw new DatabaseException("API:exec:input: Failed to prepare the callable statement.", e);
        }

        // Configure the statement
        outputConfiguration.configureStatement(statement);

        // Return
        return statement;
    }

    private void mapToCallableStatement(Object object, CallableStatement statement) {
        // Handle session
        int offset = 1;
        if (expectsSession) {
            String[] strings = _apiService.getSessionStrings();
            try {
                for (int i = 0; i < strings.length; i++)
                    statement.setString(i + offset, strings[i]);
            } catch (SQLException e) {
                throw new DatabaseException("API:exec:input[" + object.getClass().getSimpleName() + "]: Error assigning session data.", e);
            }
            offset += _apiService.getSessionStringsCount();
        }

        // Map input object
        for (int i = 0; i < accessors.length; i++) {
            try {
                mappers[i].invoke(i + offset, accessors[i].invoke(object), statement);
            } catch (Exception e) {
                throw new ExecutionException("API:exec:input[" + object.getClass().getSimpleName() + "]: Couldn't invoke accessor on record.");
            }
        }
    }
}
