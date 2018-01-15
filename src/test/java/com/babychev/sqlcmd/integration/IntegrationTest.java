package com.babychev.sqlcmd.integration;

import com.babychev.sqlcmd.Main;
import com.babychev.sqlcmd.controller.connection.DataSource;
import com.babychev.sqlcmd.controller.connection.PostgresqlDataSource;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.model.DatabaseManagerPostgreSQL;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Set;

import static org.junit.Assert.*;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private LogOutputStream out;
    private static String tableName;
    private static String PATH_TO_PROPERTIES = "src/test/resources/connection.properties";
    private static String dataBaseName;
    private static String userName;
    private static String password;
    private static String schemaName;
    private static String dataBaseNameForTest;
    private static String databases;
    private static String tables;
    private static String schemas;
    private static String databasesAfterCreate;

    @BeforeClass
    public static void setupProperties() {
        DataSource dataSource = new PostgresqlDataSource(PATH_TO_PROPERTIES);
        DatabaseManager manager = new DatabaseManagerPostgreSQL(dataSource);
        manager.connect();
        Set<String> dbs = manager.databases();
        Set<String> scms = manager.schemas();
        dataBaseName = dataSource.getProperties().getProperty("dataBaseName");
        userName = dataSource.getProperties().getProperty("userName");
        password = dataSource.getProperties().getProperty("password");
        schemaName = dataSource.getProperties().getProperty("schemaName");
        dataBaseNameForTest = dataSource.getProperties().getProperty("dataBaseNameForTest");
        tableName = dataSource.getProperties().getProperty("tableName");
        databases = dbs.toString();
        dbs.add(dataBaseNameForTest);
        scms.add(schemaName);
        databasesAfterCreate = dbs.toString();
        tables = manager.getListTables().toString();
        schemas = scms.toString();
    }

    @Before
    public  void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExitWithoutConnect() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testHelpWithoutConnect() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "\"commands : \n" +
                "\t* connect|dataBaseName|userName|password \n" +
                "\t* databases \n" +
                "\t* createDatabase|databaseName \n" +
                "\t* dropDatabase|databaseName \n" +
                "\t* schemas \n" +
                "\t* createSchema|schemaName \n" +
                "\t* dropSchema|schemaName \n" +
                "\t* selectSchema|schemaName \n" +
                "\t* tables \n" +
                "\t* clear|tableName \n" +
                "\t* drop|tableName \n" +
                "\t* create|tableName|column1|column2|...|columnN \n" +
                "\t\t* example : create|users|id INT PRIMARY KEY|name VARCHAR(10)|... \n" +
                "\t* find|tableName \n" +
                "\t\t* find|tableName|LIMIT|OFFSET \n" +
                "\t* insert|tableName|column1|value1|column2|value2|...|columnN|valueN \n" +
                "\t* update|tableName|id|column|value|columnN|valueN \n" +
                "\t* exit \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testFindWithoutConnect () {
        //given
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "You cannot use command 'find|users' , before you are not connect \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testTablesWithoutConnect () {
        //given
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "You cannot use command 'tables' , before you are not connect \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testConnect () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testConnectWithInvalidPassword () {
        //given
        in.add(String.format("connect|%s|%s|invalidPassword", dataBaseName, userName));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "FATAL: password authentication failed for user \"" + userName + "\"\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testConnectWithInvalidLogin () {
        //given
        in.add(String.format("connect|%s|invalidLogin|%s", dataBaseName, password));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "FATAL: password authentication failed for user \"invalidLogin\"\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testConnectWithInvalidDatabase () {
        //given
        in.add(String.format("connect|invalidDataBase|%s|%s", userName, password));
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "FATAL: database \"invalidDataBase\" does not exist\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testConnectWithInvalidNumberOfParameters () {
        //given
        in.add("connect|invalidDataBase");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "wrong number of parameters, expected 4 but have : 2\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testInvalidCommand () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("invalidCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "wrong command, try again or enter help!\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testCreateDatabase () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("databases");
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("databases");
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                databases + "\n" +
                "congratulation, database " + dataBaseNameForTest + " is created \n" +
                databasesAfterCreate + "\n" +
                "congratulation, database " + dataBaseNameForTest + " is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testCreateDatabaseWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("databases");
        in.add("createDatabase|" + dataBaseNameForTest + "|param");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "[template1, template0, postgres]\n" +
                "wrong number of parameters, expected 2 but have : 3\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testSelectDatabase () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testSelectNotExistDatabase () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "cannot find database 'testdb' \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testCreateSchema () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema " + schemaName + " is created \n" +
                schemas + "\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testCreateSchemaWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName + "|param");
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "wrong number of parameters, expected 2 but have : 3\n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testSelectSchema () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database " + dataBaseNameForTest + " is created \n" +
                "'" + dataBaseNameForTest + "' database is selected \n" +
                "congratulation, schema " + schemaName + " is created \n" +
                schemas + "\n" +
                "'" + schemaName + "' schema is selected \n" +
                "congratulation, schema " + schemaName + " is deleted \n" +
                "'" + dataBaseName + "' database is selected \n" +
                "congratulation, database " + dataBaseNameForTest + " is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testSelectNotExistSchema () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("selectSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database " + dataBaseNameForTest + " is created \n" +
                "'" + dataBaseNameForTest + "' database is selected \n" +
                "cannot find schema '" + schemaName + "' \n" +
                "'" + dataBaseName + "' database is selected \n" +
                "congratulation, database " + dataBaseNameForTest + " is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testCreateTable () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testCreateTableWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName);
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "'testschema' schema is selected \n" +
                "wrong number of parameters, have to more than 3, entered : 2\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testInsertDataToTable () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testInsertDataToTableWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "wrong number of parameters, have to more than 4, entered : 3\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testFindTable () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("find|" + tableName);
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "+--+------+---------+\n" +
                "|id| login| password|\n" +
                "+--+------+---------+\n" +
                "| 1|Login1|password1|\n" +
                "+--+------+---------+\n" +
                "| 2|Login2|password2|\n" +
                "+--+------+---------+\n" +
                "| 3|Login3|password3|\n" +
                "+--+------+---------+\n" +
                "| 4|Login4|password4|\n" +
                "+--+------+---------+\n" +
                "| 5|Login5|password5|\n" +
                "+--+------+---------+\n" +
                "\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testFindNotExistTable () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("selectSchema|" + schemaName);
        in.add("find|" + tableName);
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "'testschema' schema is selected \n" +
                "org.postgresql.util.PSQLException: ERROR: relation \"testschema.testtable\" does not exist\n" +
                "  Position: 15\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testFindTableWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("find|" + tableName + "|param");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "wrong number of parameters! you entered 3, but have to 2 or 4\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testFindTableLimit () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("find|" + tableName + "|2|2");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "+--+------+---------+\n" +
                "|id| login| password|\n" +
                "+--+------+---------+\n" +
                "| 3|Login3|password3|\n" +
                "+--+------+---------+\n" +
                "| 4|Login4|password4|\n" +
                "+--+------+---------+\n" +
                "\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testFindTableWrongLimit () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("find|" + tableName + "|0|2");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "LIMIT cannot be less than 1\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testUpdateById () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("update|" + tableName + "|3|login|updatevalue|password|updatepassword");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "+--+-----------+--------------+\n" +
                "|id|      login|      password|\n" +
                "+--+-----------+--------------+\n" +
                "| 1|     Login1|     password1|\n" +
                "+--+-----------+--------------+\n" +
                "| 2|     Login2|     password2|\n" +
                "+--+-----------+--------------+\n" +
                "| 4|     Login4|     password4|\n" +
                "+--+-----------+--------------+\n" +
                "| 5|     Login5|     password5|\n" +
                "+--+-----------+--------------+\n" +
                "| 3|updatevalue|updatepassword|\n" +
                "+--+-----------+--------------+\n" +
                "\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testUpdateByIdWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("update|" + tableName + "|3");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "wrong number of parameters, you entered : 3, have to not less five!\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testUpdateByIdWrongParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("update|" + tableName + "|3|column1|value");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "ERROR: column \"column1\" of relation \"testtable\" does not exist\n" +
                "  Position: 33\n" +
                "wrong parameter tableName|id|column|...\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testClear () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("clear|" + tableName);
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "cleared : \n" +
                "+--+-----+--------+\n" +
                "|id|login|password|\n" +
                "+--+-----+--------+\n" +
                "\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testClearWrongNumberParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("insert|" + tableName + "|login|Login1|password|password1");
        in.add("insert|" + tableName + "|login|Login2|password|password2");
        in.add("insert|" + tableName + "|login|Login3|password|password3");
        in.add("insert|" + tableName + "|login|Login4|password|password4");
        in.add("insert|" + tableName + "|login|Login5|password|password5");
        in.add("clear|" + tableName + "|one|two");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "inserted successfully\n" +
                "wrong number of parameters, have to 2, but entered : 4\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testClearWrongTable () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("clear|wrongTable");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "org.postgresql.util.PSQLException: ERROR: relation \"testschema.wrongtable\" does not exist\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testTables () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("tables");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "[testtable]\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testDropTable () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("create|" + tableName + "|id SERIAL PRIMARY KEY|login CHAR(64)|password CHAR(64)");
        in.add("drop|" + tableName);
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "table created : testtable\n" +
                "dropped success! list of tables : []\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testDropTableWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("selectSchema|" + schemaName);
        in.add("drop|" + tableName + "|param");
        in.add("dropSchema|" + schemaName);
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "'testschema' schema is selected \n" +
                "wrong number of parameters, have to 2, but entered : 3\n" +
                "congratulation, schema testschema is deleted \n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testDropSchema () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("selectDatabase|" + dataBaseNameForTest);
        in.add("createSchema|" + schemaName);
        in.add("schemas");
        in.add("dropSchema|" + schemaName);
        in.add("schemas");
        in.add("selectDatabase|" + dataBaseName);
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "'testdb' database is selected \n" +
                "congratulation, schema testschema is created \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema, testschema]\n" +
                "congratulation, schema testschema is deleted \n" +
                "[pg_toast, pg_temp_1, pg_toast_temp_1, pg_catalog, public, information_schema]\n" +
                "'postgres' database is selected \n" +
                "congratulation, database testdb is deleted \n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testDropSchemaWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("dropSchema|" + schemaName + "|param");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "wrong number of parameters, expected 2 but have : 3\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testDropDatabase () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("createDatabase|" + dataBaseNameForTest);
        in.add("databases");
        in.add("dropDatabase|" + dataBaseNameForTest);
        in.add("databases");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "congratulation, database testdb is created \n" +
                "[template1, template0, postgres, testdb]\n" +
                "congratulation, database testdb is deleted \n" +
                "[template1, template0, postgres]\n" +
                "============= Good Bay =============\n", out.getData());
    }

    @Test
    public void testDropDatabaseWrongNumberOfParameters () {
        //given
        in.add(String.format("connect|%s|%s|%s", dataBaseName, userName, password));
        in.add("dropDatabase|" + dataBaseNameForTest + "|param");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("================== Welcome to SQLcmd ==================\n" +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n" +
                "congratulation, connected was success\n" +
                "wrong number of parameters, expected 2 but have : 3\n" +
                "============= Good Bay =============\n", out.getData());
    }


}
