package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateTest {

    private DatabaseManager manager;
    private View console;
    private Command create;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        create = new Create(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = create.canProcess("create|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = create.canProcess("create");
        //then
        assertFalse(actual);
    }

    @Test
    public void testCreate () {
        //when
        create.canProcess("create|tableName|column1|column2|");
        create.execute();
        //then
        verify(console).print("table created : tableName\n");
    }

    @Test
    public void testCreateWrongNumberOfParameters () {
        //when
        create.canProcess("create|tableName");
        create.execute();
        //then
        verify(console).print("wrong number of parameters, have to more than 3, entered : 2\n");
    }

}
