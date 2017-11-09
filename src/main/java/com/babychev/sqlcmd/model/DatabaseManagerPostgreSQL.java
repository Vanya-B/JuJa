package com.babychev.sqlcmd.model;

import com.babychev.sqlcmd.controller.connection.*;
import java.sql.*;
import java.util.*;

public class DatabaseManagerPostgreSQL implements DatabaseManager {

    private Connection connection;
    private DataSource dataSource;
    private String schema;
    private String databaseName;
    private String userName;
    private String password;
    private static final int COLUMNINDEX = 1;

    public DatabaseManagerPostgreSQL(DataSource dataSource){
        this.dataSource = dataSource;
        this.schema = dataSource.getProperties().getProperty("schemaName");
        this.databaseName = dataSource.getProperties().getProperty("dataBaseName");
    }

    @Override
    public void connect() {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void connect(String[] parameters) throws SQLException {
        if (connection == null) {
            databaseName = parameters[1];
            userName = parameters[2];
            password = parameters[3];
            connection = dataSource.getConnection(databaseName, userName, password);
        } else {
            throw new RuntimeException(String.format("you are have already connected to '%s' database!", databaseName));
        }
    }

    @Override
    public boolean closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public Set<String> getListTables(){
        Set<String> result = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT table_name FROM information_schema.tables where table_schema = '" + schema + "'"))
        {
            int index = 1;
            while (rs.next()) {
                result.add(rs.getString(index++));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public List<DataSet> getTableData(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM " + schema + "." + tableName))
        {
            List<DataSet> result = new LinkedList<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i).trim(), rs.getString(i).trim());
                }
                result.add(dataSet);
            }

            if (result.size() == 0) {
                List<DataSet> columns = new LinkedList<>();
                DataSet dataSet = new DataSet();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    dataSet.put(rsmd.getColumnName(i).trim(), null);
                }
                columns.add(dataSet);
                return columns;
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DataSet> getTableDataLimit(String tableName, int limit, int offSet) {
        String sql = "SELECT * FROM " + schema + "." + tableName + " LIMIT " + limit  + " OFFSET " + offSet;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql))
        {
            if (offSet > getSize(schema ,tableName)) {
                throw new IllegalArgumentException("OFFSET cannot be bigger than size of table");
            }
            List<DataSet> result = new LinkedList<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i).trim(), rs.getString(i).trim());
                }
                result.add(dataSet);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("table doesn't exist, existing tables: ");
        }
        return null;
    }

    @Override
    public int update(String tableName, int id, DataSet data) {
        String tableColumns = formatedColumns(data, "%s = ?,");
        String sql = "UPDATE " + schema + "." + tableName + " SET " + tableColumns + " WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (String value: data.getValues()) {
                preparedStatement.setString(index++, value);
            }
            preparedStatement.setInt(index, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void create(String tableName, String[] data) {
        try(Statement statement = connection.createStatement()) {
            String columns = "";
            for (int i = 0; i < data.length; i++) {
                columns += data[i] + ", ";
            }
            columns = columns.substring(0, columns.length() - 2);
            String sql = "CREATE TABLE IF NOT EXISTS " + schema + "." + tableName + " (" + columns + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drop(String tableName) {
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE " + schema + "." + tableName + ";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(String tableName, DataSet data) {
        String columns = formatedColumns(data, "%s,");
        String values = formatedValues(data, "'%s',");
        String sql = "INSERT INTO " + schema + "." + tableName + " (" + columns + ") VALUES (" + values + ");";
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear(String tableName) {
        String sql = "TRUNCATE TABLE " + schema + "." + tableName;
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void createSchema(String schemaName) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create schema " + schemaName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void dropSchema(String schemaName) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("drop schema " + schemaName + " cascade");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Set<String> schemas() {
        Set<String> result = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select schema_name from information_schema.schemata"))
        {
            while (rs.next()) {
                result.add(rs.getString(COLUMNINDEX));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    @Override
    public String selectSchema(String schemaName) {
        Set<String> schemas = schemas();
        if (schemas != null && schemas.size() != 0) {
            for (String schema : schemas) {
                if (schema.equals(schemaName)) {
                    this.schema = schema;
                }
            }
        }
        return this.schema;
    }

    @Override
    public Set<String> databases() {
        Set<String> result = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT datname FROM pg_database"))
        {
            while (rs.next()) {
                result.add(rs.getString(COLUMNINDEX));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    @Override
    public void createDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE " + databaseName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void dropDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP DATABASE " + databaseName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String selectDatabase(String databaseName) {
        try {
            Set<String> databases = databases();
            if (databases != null && databases.size() != 0) {
                for (String database : databases) {
                    if (database.equals(databaseName)) {
                        connection.close();
                        connection = dataSource.getConnection(databaseName, userName, password);
                        this.databaseName = databaseName;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return  this.databaseName;
    }

    private String formatedValues(DataSet data, String format) {
        String result = "";
        for (String value: data.getValues()) {
            result += String.format(format, value);
        }
        return result.substring(0, result.length() - 1);
    }

    private String formatedColumns(DataSet data, String format) {
        String result = "";
        for (String column: data.getColumns()) {
            result += String.format(format, column);
        }
        return result.substring(0, result.length() - 1);
    }

    private int getSize(String schema, String tableName) throws SQLException {
        String sql = "";
        if (schema.equals("")) {
            sql = String.format("SELECT COUNT(*) FROM %s", tableName);
        } else {
            sql = String.format("SELECT COUNT(*) FROM %s%c%s", schema, '.', tableName);
        }
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql))
        {
            rs.next();
            int result = rs.getInt(COLUMNINDEX);
            rs.close();
            return result;
        }
    }
}
