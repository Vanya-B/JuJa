package com.babychev.sqlcmd.model;

import com.babychev.sqlcmd.controller.connection.DataSource;
import com.babychev.sqlcmd.controller.connection.PostgresqlDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import static org.junit.Assert.*;

public class DatabaseManagerTest {
    private DatabaseManagerPostgreSQL manager;
    private DataSource dataSource;
    private  String userName;
    private  String password;
    private  String databaseName;
    private  String tableName;
    private  String schemaName;
    private static String PATH_TO_PROPERTIES = "src/test/resources/connection.properties";
    private static String PATH_TO_INVALID_PROPERTIES = "src/test/resources/invalidConnection.properties";

    @Before
    public  void setUp(){
        dataSource = new PostgresqlDataSource(PATH_TO_PROPERTIES);
        schemaName = dataSource.getProperties().getProperty("schemaName");
        tableName = dataSource.getProperties().getProperty("tableName");
        userName = dataSource.getProperties().getProperty("userName");
        password = dataSource.getProperties().getProperty("password");
        databaseName = dataSource.getProperties().getProperty("dataBaseName");
        manager = new DatabaseManagerPostgreSQL(dataSource);
        manager.connect();
        manager.createSchema(schemaName);
        manager.create(tableName, new String[]{"id SERIAL PRIMARY KEY, login CHAR(64), password CHAR(64)"});
        DataSet newData = new DataSet();
        newData.put("login", "John");
        newData.put("password", "qwerty");
        manager.insert(tableName, newData);
    }

    @After
    public void drop(){
        manager.dropSchema(schemaName);
        manager.closeConnection();
    }

    @Test
    public void testConnect () {
        //given
        boolean expected = manager.isConnected();
        //when
        manager.connect();
        //then
        boolean actuale = manager.isConnected();
        assertEquals(expected, actuale);
    }

    @Test
    public void testConnectWithInvalidProperties () {
        //given
        DataSource dataSource = new PostgresqlDataSource(PATH_TO_INVALID_PROPERTIES);
        DatabaseManagerPostgreSQL manager = new DatabaseManagerPostgreSQL(dataSource);
        boolean expected = false;
        //when
        manager.connect();
        //then
        boolean actuale = manager.isConnected();
        assertEquals(expected, actuale);
    }

    @Test
    public void testConnectWithParameters () {
        //given
        manager.closeConnection();
        String [] parameters = new String[] {"", databaseName, userName, password};
        boolean expected = true;
        //when
        try {
            manager.connect(parameters);
        } catch (SQLException e) {
            //doNothing
        }
        //then
        boolean actual = manager.isConnected();
        assertEquals(expected, actual);
    }

    @Test (expected = RuntimeException.class)
    public void testMultiplyConnectWithParameters () {
        //given
        String [] parameters = new String[] {"", databaseName, userName, password};
        //when
        try {
            manager.connect(parameters);
        } catch (SQLException e) {
            //then
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetListTables(){
        String expected = "[" + tableName + "]";
        String actual = manager.getListTables().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTableData(){
        //TODO println correct output
        String expected = "[column : id\n" +
                "value : 1\n" +
                "column : login\n" +
                "value : John\n" +
                "column : password\n" +
                "value : qwerty\n" +
                "]";
        DataSet[] actual = manager.getTableData(tableName);
        assertEquals(expected, Arrays.toString(actual));
    }

    @Test
    public void testInsert(){
        String expected = "[column : id\n" +
                "value : 1\n" +
                "column : login\n" +
                "value : John\n" +
                "column : password\n" +
                "value : qwerty\n" +
                ", column : id\n" +
                "value : 2\n" +
                "column : login\n" +
                "value : insert\n" +
                "column : password\n" +
                "value : insert\n" +
                "]";
        DataSet data = new DataSet();
        data.put("login", "insert");
        data.put("password", "insert");
        manager.insert(tableName, data);
        DataSet[] actual = manager.getTableData(tableName);
        assertEquals(expected, Arrays.toString(actual));
    }

    @Test
    public void testGetTableDataLimit(){
        String expected = "[column : id\n" +
                "value : 1\n" +
                "column : login\n" +
                "value : John\n" +
                "column : password\n" +
                "value : qwerty\n" +
                "]";
        DataSet data = new DataSet();
        data.put("login", "insert");
        data.put("password", "insert");
        manager.insert(tableName, data);
        DataSet[] actual = manager.getTableDataLimit(tableName, 1,0);
        assertEquals(expected, Arrays.toString(actual));
    }

    @Test
    public void testUpdate(){
        String expected = "[column : id\n" +
                            "value : 1\n" +
                            "column : login\n" +
                            "value : John\n" +
                            "column : password\n" +
                            "value : 12345\n" +
                            "]";
        DataSet newData = new DataSet();
        newData.put("login", "John");
        newData.put("password", "12345");
        manager.update(tableName, 1, newData);
        DataSet[] actual = manager.getTableData(tableName);
        assertEquals(expected, Arrays.toString(actual));
    }

    @Test
    public void clear(){
        String expected = "[column : id\n" +
                          "value : null\n" +
                          "column : login\n" +
                          "value : null\n" +
                          "column : password\n" +
                          "value : null\n" +
                          "]";
        manager.clear(tableName);
        DataSet[] actual = manager.getTableData(tableName);
        assertEquals(expected, Arrays.toString(actual));
    }

    @Test
    public void testCreateSchema () {
        //given
        String [] schemasBeforeCreate = manager.schemas();

        //when
        manager.createSchema("testSchemaCreated");

        //then
        String [] schemasAfterCreate = manager.schemas();
        assertNotEquals(schemasBeforeCreate, schemasAfterCreate);
        manager.dropSchema("testSchemaCreated");
    }

    @Test
    public void testSchemas() {
        //given
        String expectedSchemas = "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]";

        //when
        String actualSchemas = Arrays.toString(manager.schemas());

        //then
        assertEquals(expectedSchemas, actualSchemas);
    }

    @Test
    public void testDropSchema () {
        //given
        manager.createSchema("createdSchema");
        String expected = Arrays.toString(manager.schemas());

        //when
        manager.dropSchema("createdSchema");

        //then
        String actual = Arrays.toString(manager.schemas());
        assertNotEquals(expected, actual);
    }

    @Test
    public void testSelectSchema () {
        //given
        String schemaName = this.schemaName;

        //when
        String actual = manager.selectSchema(schemaName);

        //then
        assertEquals(schemaName, actual);
        manager.selectSchema("public");
    }

    @Test
    public void testCreateDatabase () {
        //given
        String unexpected = Arrays.toString(manager.databases());

        //when
        manager.createDatabase("testdb");

        //then
        String actual = Arrays.toString(manager.databases());
        assertNotEquals(unexpected, actual);
        manager.dropDatabase("testdb");
    }

    @Test
    public void testDropDatabase () {
        //given
        String expected = Arrays.toString(manager.databases());
        manager.createDatabase("testdb");

        //when
        manager.dropDatabase("testdb");

        //then
        String actual = Arrays.toString(manager.databases());
        assertEquals(expected, actual);
    }

}
