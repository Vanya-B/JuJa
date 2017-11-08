package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SelectDatabaseTest {

    private DatabaseManager manager;
    private View console;
    private Command selectDatabase;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        selectDatabase = new SelectDatabase(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = selectDatabase.canProcess("selectDatabase|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = selectDatabase.canProcess("selectDatabase");
        //then
        assertFalse(actual);
    }

    @Test
    public void testSelectDatabase () {
        //given
        when(manager.selectDatabase("databaseName")).thenReturn("databaseName");
        //when
        selectDatabase.canProcess("selectDatabase|databaseName");
        selectDatabase.execute();
        //then
        verify(console).print("'databaseName' database is selected \n");
    }

    @Test
    public void testSelectDatabaseNotExist () {
        //given
        when(manager.selectDatabase("databaseName")).thenReturn("databaseName");
        //when
        selectDatabase.canProcess("selectDatabase|notExistDb");
        selectDatabase.execute();
        //then
        verify(console).print("cannot find database 'notExistDb' \n");
    }

    @Test
    public void testSelectDatabaseWrongNumberOfParameters () {
        //when
        selectDatabase.canProcess("selectDatabase|");
        selectDatabase.execute();
        //then
        verify(console).print("wrong number of parameters, have to 2, entered : 1\n");
    }
}
