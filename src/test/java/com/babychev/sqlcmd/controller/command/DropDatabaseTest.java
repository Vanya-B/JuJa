package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropDatabaseTest {

    private DatabaseManager manager;
    private View console;
    private Command dropDatabase;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        dropDatabase = new DropDatabase(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = dropDatabase.canProcess("dropDatabase|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = dropDatabase.canProcess("dropDatabase");
        //then
        assertFalse(actual);
    }

    @Test
    public void testDropDatabase () {
        //when
        dropDatabase.canProcess("dropDatabase|dataBaseName");
        dropDatabase.execute();
        //then
        verify(console).print("congratulation, database dataBaseName is deleted \n");
    }

    @Test
    public void testDropDatabaseWrongNumberOfParameters () {
        //when
        dropDatabase.canProcess("dropDatabase|");
        dropDatabase.execute();
        //then
        verify(console).print("wrong number of parameters, expected 2 but have : 1\n");
    }
}
