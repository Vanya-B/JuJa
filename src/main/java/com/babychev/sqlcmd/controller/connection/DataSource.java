package com.babychev.sqlcmd.controller.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public interface DataSource {
    void setUserName(String name);
    void setPassword(String password);
    void setHost(String host);
    void setPort(String port);
    void setDataBaseName(String dataBaseName);
    void setSchemaName(String schemaName);
    Properties getProperties();
    Connection getConnection() throws SQLException;
    Connection getConnection(String dataBaseName, String userName, String password) throws SQLException;
}
