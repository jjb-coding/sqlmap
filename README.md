# SqlMap
SqlMap is a Reflection-based system that marshals and automates SQL Stored Procedure invocations. A single statement can execute a Stored Procedure and map its return back into a data structure. For example:
```
GetDetailsOutput details = (GetDetailsOutput)new GetDetailsInput(uuid).execute();
```
can return an object with both fields that correspond to procedure output and result sets. 

## Overview
The implementer defines a system of Input, Output and Results record classes, using annotations to describe how they are connected. Output records contain fields that capture immediate procedure output variables, and `List<Result>` fields. Result records capture rows in a result set. It is possible to map a response that contains procedure output variables and multiple result sets. 

If fields of uncommon types are included, the implementer can register custom configurers and mappers that translate those fields into manipulations of an SQL Connection instance.

## Features
- **Extendable Configurers and Mappers**: The framework recognises a variety of types that appear as fields in Input, Output and Results records. 
- **Sessions**: A Session is a number of additional strings delivered by a configured Session Provider. Procedures can be configured to have their input parameters automatically prepended with the session data, for seamless sharing of context with any procedure request.
- **Name Providers**: Name Providers define how to compute the name of the stored procedure itself, taking into account record name,
- **Class Providers**: Classes can be discovered through Reflection, a list of classes, or the implementer can extend and supply a custom Class Provider object.
- **Validation Rules**: Which Input, Output and Results records were discovered can be compared against a package, list of classes, or custom Class Provider.

## Architecture
- **Configuration Pattern**: The Service provides a parameterless constructor, such that it can be hosted as a Singleton by any framework the implementer uses. Methods are blocked until the Service is configured with an AppServiceConfigurationBuilder.
- **Reflection Caching**: All Nodes and Injectables are discovered when the Service is configured, and scanned once, a digest containing all relevant information being retained thereafter.
- **No Specific Dependencies**: The implementer configures a ConnectionProvider that provides SQL Connection instances, acting as an interface between their database system and this library. 

## Installation
### Maven
If you have access to the published Maven artifact, add the following dependency for the library:

```
<dependency>
    <groupId>com.github.jjb-coding</groupId>
    <artifactId>sqlmap</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Building from source
Clone the repository. To build:

```
mvn package
```

## Documentation
See [documentation](https://jjb-coding.github.io/sqlmap/) for library documentation.

## Licence
Distributed under the MIT License.