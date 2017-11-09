package com.babychev.sqlcmd.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void connect();

    void connect(String[] parameters) throws SQLException;

    boolean closeConnection();

    Set<String> getListTables();

    List<DataSet> getTableData(String tableName);

    List<DataSet> getTableDataLimit(String tableName, int limit, int offSet);

    int update(String tableName, int id, DataSet data);

    void create(String tableName, String[] data);

    void drop(String tableName);

    void insert(String tableName, DataSet data);

    void clear(String tableName);

    boolean isConnected();

    void createSchema(String schemaName);

    void dropSchema(String schemaName);

    Set<String> schemas();

    String selectSchema(String schemaName);

    Set<String> databases();

    void createDatabase(String databaseName);

    void dropDatabase(String databaseName);

    String selectDatabase(String databaseName);
}
