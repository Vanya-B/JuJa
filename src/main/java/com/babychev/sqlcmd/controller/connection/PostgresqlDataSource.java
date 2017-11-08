package com.babychev.sqlcmd.controller.connection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresqlDataSource implements DataSource {

    private String userName;
    private String password;
    private String host;
    private String port;
    private String dataBaseName;
    private String schemaName;
    private String pathToPropertiesFile;
    private Connection connection;
    private static final String URL = "jdbc:postgresql://";

    public PostgresqlDataSource(String pathToPropertiesFile){
        this.pathToPropertiesFile = pathToPropertiesFile;
        initialization(getProperties());
    }

    @Override
    public void setUserName(String name) {
        this.userName = name;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    @Override
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(URL +
                                                        host + ":" +
                                                        port + "/" +
                                                        dataBaseName + "?loggerLevel=OFF",
                                                        userName,
                                                        password);
        return connection;
    }

    @Override
    public Connection getConnection(String dataBaseName, String userName, String password) throws SQLException {
            connection = DriverManager.getConnection(URL +
                                                         host + ":" +
                                                         port + "/" +
                                                         dataBaseName + "?loggerLevel=OFF",
                                                         userName,
                                                         password);
        return connection;
    }

    public Properties getProperties(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(pathToPropertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    private void initialization(Properties properties){
        this.dataBaseName = properties.getProperty("dataBaseName");
        this.userName = properties.getProperty("userName");
        this.password = properties.getProperty("password");
        this.host = properties.getProperty("host");
        this.port = properties.getProperty("port");
        this.schemaName = properties.getProperty("schemaName");
    }
}
