module com.github.jblair.sqlMapper {
    requires java.sql;
    exports com.github.jjbcoding.sqlmap.api.annotations;
    exports com.github.jjbcoding.sqlmap.api.interfaces;
    exports com.github.jjbcoding.sqlmap.exceptions;
    exports com.github.jjbcoding.sqlmap.service;
    exports com.github.jjbcoding.sqlmap.service.adapters;
    exports com.github.jjbcoding.sqlmap.service.adapters.enums;
    exports com.github.jjbcoding.sqlmap.service.providers;
    exports com.github.jjbcoding.sqlmap.service.providers.defaults;
}