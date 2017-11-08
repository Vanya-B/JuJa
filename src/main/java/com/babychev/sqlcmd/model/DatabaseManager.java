package com.babychev.sqlcmd.model;

import java.sql.SQLException;

public interface DatabaseManager {

    void connect();

    void connect(String[] parameters) throws SQLException;

    boolean closeConnection();

    String [] getListTables();

    DataSet [] getTableData(String tableName);

    DataSet [] getTableDataLimit(String tableName, int limit, int offSet);

    int update(String tableName, int id, DataSet data);

    void create(String tableName, String[] data);

    void drop(String tableName);

    void insert(String tableName, DataSet data);

    void clear(String tableName);

    String [] getColumns(String tableName);

    boolean isConnected();

    void createSchema(String schemaName);

    void dropSchema(String schemaName);

    String [] schemas();

    String selectSchema(String schemaName);

    String [] databases();

    void createDatabase(String databaseName);

    void dropDatabase(String databaseName);

    String selectDatabase(String databaseName);
}
