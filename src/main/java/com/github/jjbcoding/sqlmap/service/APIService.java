package com.github.jjbcoding.sqlmap.service;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.github.jjbcoding.sqlmap.api.interfaces.APIInput;
import com.github.jjbcoding.sqlmap.exceptions.BuilderException;
import com.github.jjbcoding.sqlmap.exceptions.ExecutionException;
import com.github.jjbcoding.sqlmap.service.adapters.*;
import com.github.jjbcoding.sqlmap.service.adapters.enums.*;
import com.github.jjbcoding.sqlmap.service.providers.*;
import com.github.jjbcoding.sqlmap.service.adapters.InputMapper;
import com.github.jjbcoding.sqlmap.service.adapters.OutputMapper;
import com.github.jjbcoding.sqlmap.service.adapters.OutputResultsMapper;
import com.github.jjbcoding.sqlmap.service.adapters.enums.InputMapperEnum;
import com.github.jjbcoding.sqlmap.service.adapters.enums.OutputMapperEnum;
import com.github.jjbcoding.sqlmap.service.adapters.enums.OutputResultsMapperEnum;
import com.github.jjbcoding.sqlmap.service.providers.*;

import java.sql.ResultSet;

import static com.github.jjbcoding.sqlmap.util.internals.MapHelper.combineMaps;

/**
 * Facilitates API calls.
 * Does not need to be accessed directly outside the API package.
 */
public class APIService {
	// ----- DYNAMIC
	// *** FIELDS
	// Mapper & configurer registries
	Map<Class<?>, InputMapper> classToInputMapper;
	Map<Class<?>, Integer> classToOutputConfigurer;
	Map<Class<?>, OutputMapper> classToOutputMapper;
	Map<Class<?>, OutputResultsMapper> classToOutputResultsMapper;
	
	// Input class registry
	private final Map<Class<?>, InputConfiguration> registry;

	// Output & results sets
	private final HashSet<Class<?>> discoveredOutputs, discoveredOutputResults;

	// Configuration / Providers
	IClassProvider inputProvider;
	ISessionProvider sessionProvider;
	IConnectionProvider connectionProvider;
	INameProvider nameProvider;
	// Configuration / Rules
	IRule outputRule, outputResultsRule, bothRule;
	// Configuration / State
	private boolean configured;

	// *** CONSTRUCTORS
	/**
	 * An API service instance contains read-only mapper registries, and a
	 * registry of scanned API endpoints.
	 */
	public APIService() {
		// ** Initialise
		// * Maps
		// Input mapper map
		constructInputMapperRegistry();
		
		// Output configurer map
		constructOutputConfigurerRegistry();
		
		// Output mapper map
		constructOutputMapperRegistry();
		
		// Output results mapper map
		constructOutputResultsMapperRegistry();
		
		// * Registries & sets
		registry = new HashMap<>();
		discoveredOutputs = new HashSet<>();
		discoveredOutputResults = new HashSet<>();

		// * Configuration
		configured = false;
	}

	// *** METHODS
	// ** PUBLIC
	/**
	 * Configures and builds the service. It will not be possible
	 * to execute API calls via APIInput or execute() until this method
	 * has been called.
	 * @param configuration	The configuration object.
	 */
	@SuppressWarnings("unused")
	public void build(APIServiceConfigurationBuilder configuration) {
		// * Configuration
		// Finalise
		configuration.finalise();

		// Clone
		inputProvider = configuration.inputProvider;
		sessionProvider = configuration.sessionProvider;
		connectionProvider = configuration.connectionProvider;
		nameProvider = configuration.nameProvider;
		outputRule = configuration.outputRule;
		outputResultsRule = configuration.outputResultsRule;
		bothRule = configuration.bothRule;

		// Combine Registries
		combineMaps(classToInputMapper, configuration.classToInputMapper, "API:init: Class was already associated with an input mapper");
		combineMaps(classToOutputConfigurer, configuration.classToOutputConfigurer, "API:init: Class already associated with a type constant");
		combineMaps(classToOutputMapper, configuration.classToOutputMapper, "API:init: Class was already associated with an output mapper");
		combineMaps(classToOutputResultsMapper, configuration.classToOutputResultsMapper, "API:init: Class was already associated with an outputResults mapper");

		// * Discover
		for (Class<?> input : inputProvider.getClasses())
			registry.put(input, new InputConfiguration(this, input));

		// * Validate rules, if any
		if (outputRule != null && !outputRule.isValid(discoveredOutputs))
			throw new BuilderException("API:init: Output records did not conform to configured validation rule.");

		if (outputResultsRule != null && !outputResultsRule.isValid(discoveredOutputResults))
			throw new BuilderException("API:init: OutputResults records did not conform to configured validation rule.");

		if (bothRule != null) {
			HashSet<Class<?>> discovered = new HashSet<>(discoveredOutputs);
			discovered.addAll(discoveredOutputResults);

			if (!bothRule.isValid(discovered))
				throw new BuilderException("API:init: Output & OutputResults records did not conform to configured validation rule.");
		}

		// * Finalise
		configured = true;
	}

	/**
	 * Calls an API endpoint over an input record endpoint. Passes along the
	 * request to the correct input configuration instance through the registry.
	 * @param object		The input record.
	 * @return				The output record.
	 */
	public Object execute(APIInput<?> object) {
		// Validate configuration
		if (!configured)
			throw new ExecutionException("API:exec: Attempted to execute API request before service was configured.");

		// Get the class of the runtime object
		Class<?> cls = object.getClass();

		// VALIDATE: is object type in registry?
		InputConfiguration input = registry.get(cls);
		if (input == null)
			throw new ExecutionException("API:exec[" + cls.getSimpleName() + "] Attempted to execute API request on object which has not been scanned.");

		// Pass along to InputConfiguration object
		return input.execute(object);
	}

	// * Mappers & Configurers
	/**
	 * Registers an input mapper against a class.
	 * Must be registered before build() is called.
	 * Will throw a BuilderException if already associated with a class.
	 * @param fieldCls		The class
	 * @param mapper		The mapper
	 */
	@SuppressWarnings("unused")
	public void addInputMapper(Class<?> fieldCls, InputMapper mapper) {
		if (configured)
			throw new BuilderException("API:registry: APIService has already been configured and built.");
		if (classToInputMapper.put(fieldCls, mapper) != null)
			throw new BuilderException("API:registry: Class was already associated with an input mapper");
	}

	/**
	 * Registers an SQL type constant against a class.
	 * Must be registered before build() is called.
	 * Will throw a BuilderException if already associated with a type constant.
	 * @param fieldCls		The class
	 * @param type			The SQL type constant
	 */
	@SuppressWarnings("unused")
	public void addOutputConfigurer(Class<?> fieldCls, int type) {
		if (configured)
			throw new BuilderException("API:registry: APIService has already been configured and built.");
		if (classToOutputConfigurer.put(fieldCls, type) != null)
			throw new BuilderException("API:registry: Class already associated with a type constant");
	}

	/**
	 * Registers an output mapper against a class.
	 * Must be registered before build() is called.
	 * Will throw a BuilderException if already associated with a class.
	 * @param fieldCls		The class
	 * @param mapper		The mapper
	 */
	@SuppressWarnings("unused")
	public void addOutputMapper(Class<?> fieldCls, OutputMapper mapper) {
		if (configured)
			throw new BuilderException("API:registry: APIService has already been configured and built.");
		if (classToOutputMapper.put(fieldCls, mapper) != null)
			throw new BuilderException("API:registry: Class was already associated with an output mapper");
	}

	/**
	 * Registers an outputResults mapper against a class.
	 * Must be registered before build() is called.
	 * Will throw a BuilderException if already associated with a class.
	 * @param fieldCls		The class
	 * @param mapper		The mapper
	 */
	@SuppressWarnings("unused")
	public void addOutputResultsMapper(Class<?> fieldCls, OutputResultsMapper mapper) {
		if (configured)
			throw new BuilderException("API:registry: APIService has already been configured and built.");
		if (classToOutputResultsMapper.put(fieldCls, mapper) != null)
			throw new BuilderException("API:registry: Class was already associated with an outputResults mapper");
	}

	// ** PACKAGE-PRIVATE
	// * Enum Mappers
	InputMapper getOrCreateInputMapperEnum(Class<?> enumCls) {
		InputMapper mapper = classToInputMapper.get(enumCls);
		if (mapper == null) {
			mapper = new InputMapperEnum(enumCls.getEnumConstants());
			classToInputMapper.put(enumCls, mapper);
		}
		return mapper;
	}

	OutputMapper getOrCreateOutputMapperEnum(Class<?> enumCls) {
		OutputMapper mapper = classToOutputMapper.get(enumCls);
		if (mapper == null) {
			mapper = new OutputMapperEnum(enumCls.getEnumConstants());
			classToOutputMapper.put(enumCls, mapper);
		}
		return mapper;
	}

	OutputResultsMapper getOrCreateOutputResultsMapperEnum(Class<?> enumCls) {
		OutputResultsMapper mapper = classToOutputResultsMapper.get(enumCls);
		if (mapper == null) {
			mapper = new OutputResultsMapperEnum(enumCls.getEnumConstants());
			classToOutputResultsMapper.put(enumCls, mapper);
		}
		return mapper;
	}

	// * Discovery
	void discoverOutput(Class<?> cls) {
		discoveredOutputs.add(cls);
	}

	void discoverOutputResults(Class<?> cls) {
		discoveredOutputResults.add(cls);
	}

	// * Providers / Getters
	boolean isConfigured() {
		return configured;
	}

	boolean canExposeSession() {
		return sessionProvider != null;
	}

	String[] getSessionStrings() {
		if (!sessionProvider.sessionExists())
			throw new ExecutionException("API:exec: An API call expects an ongoing session, but there is none.");
		return sessionProvider.getStrings();
	}

	int getSessionStringsCount() {
		return sessionProvider.getNumberOfStrings();
	}

	Connection getConnection()
		throws SQLException {
		return connectionProvider.getConnection();
	}

	String getName(String recordName, boolean session) {
		return nameProvider.getName(recordName, session);
	}

	// ** PRIVATE
	// * Default Registry Construction
	private void constructInputMapperRegistry() {
		// Construct map
		classToInputMapper = new HashMap<>();
		
		// Supply entries
		classToInputMapper.put(Integer.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setInt(i, (int)object);
			}
		});
		classToInputMapper.put(Long.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setLong(i, (Long)object);
			}
		});
		classToInputMapper.put(String.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setString(i, (String)object);
			}
		});
		classToInputMapper.put(byte[].class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setBytes(i, (byte[])object);
			}
		});
		classToInputMapper.put(Timestamp.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setTimestamp(i, (Timestamp)object);
			}
		});
		classToInputMapper.put(Date.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setDate(i, (Date)object);
			}
		});
	}
	
	private void constructOutputConfigurerRegistry() {
		// Construct map
		classToOutputConfigurer = new HashMap<>();
		
		// Supply entries
		classToOutputConfigurer.put(
				Enum.class,
				Types.VARCHAR);
		classToOutputConfigurer.put(
				Integer.class,
				Types.INTEGER);
		classToOutputConfigurer.put(
				Long.class,
				Types.BIGINT);
		classToOutputConfigurer.put(
				String.class, 
				Types.VARCHAR);
		classToOutputConfigurer.put(
				byte[].class, 
				Types.VARBINARY);
		classToOutputConfigurer.put(
				Timestamp.class,
				Types.TIMESTAMP);
		classToOutputConfigurer.put(
				Date.class,
				Types.DATE);
	}

	@SuppressWarnings("RedundantCast")
	private void constructOutputMapperRegistry() {
		// Construct map
		classToOutputMapper = new HashMap<>();
		
		// Supply entries
		classToOutputMapper.put(Long.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getLong(i);
			}
		});
		classToOutputMapper.put(Integer.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getInt(i);
			}
		});
		classToOutputMapper.put(String.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getString(i);
			}
		});
		classToOutputMapper.put(byte[].class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getBytes(i);
			}
		});
		classToOutputMapper.put(Timestamp.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getDate(i);
			}
		});
		classToOutputMapper.put(Date.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getDate(i);
			}
		});
	}

	@SuppressWarnings("RedundantCast")
	private void constructOutputResultsMapperRegistry() {
		// Construct map
		classToOutputResultsMapper = new HashMap<>();
		
		// Supply entries
		classToOutputResultsMapper.put(Integer.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getInt(name);
			}
		});
		classToOutputResultsMapper.put(Long.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getLong(name);
			}
		});
		classToOutputResultsMapper.put(String.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getString(name);
			}
		});
		classToOutputResultsMapper.put(byte[].class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getBytes(name);
			}
		});
		classToOutputResultsMapper.put(Timestamp.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getTimestamp(name);
			}
		});
		classToOutputResultsMapper.put(Date.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getDate(name);
			}
		});
		classToOutputResultsMapper.put(Time.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getTime(name);
			}
		});
		classToOutputResultsMapper.put(long.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getLong(name);
			}
		});
		classToOutputResultsMapper.put(Boolean.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getBoolean(name);
			}
		});
	}
}

