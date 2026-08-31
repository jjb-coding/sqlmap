module com.github.jblair.sqlMapper {
    requires java.sql;
    exports com.github.jblair.sqlmap.api.annotations;
    exports com.github.jblair.sqlmap.api.interfaces;
    exports com.github.jblair.sqlmap.exceptions;
    exports com.github.jblair.sqlmap.service;
    exports com.github.jblair.sqlmap.service.adapters;
    exports com.github.jblair.sqlmap.service.adapters.enums;
    exports com.github.jblair.sqlmap.service.providers;
    exports com.github.jblair.sqlmap.service.providers.defaults;
}