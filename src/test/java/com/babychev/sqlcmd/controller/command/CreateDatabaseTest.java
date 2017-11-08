package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateDatabaseTest {

    private DatabaseManager manager;
    private View console;
    private Command createDatabase;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        createDatabase = new CreateDatabase(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = createDatabase.canProcess("createDatabase|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = createDatabase.canProcess("createDatabase");
        //then
        assertFalse(actual);
    }

    @Test
    public void testCreateDatabase () {
        //when
        createDatabase.canProcess("createDatabase|databaseName");
        createDatabase.execute();
        //then
        verify(console).print("congratulation, database databaseName is created \n");
    }

    @Test
    public void testCreateDatabaseWrongNumberOfParameters () {
        //when
        createDatabase.canProcess("createDatabase");
        createDatabase.execute();
        //then
        verify(console).print("wrong number of parameters, expected 2 but have : 1\n");
    }
}
