package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InsertTest {

    private DatabaseManager manager;
    private View console;
    private Command insert;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        insert = new Insert(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = insert.canProcess("insert|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = insert.canProcess("insert");
        //then
        assertFalse(actual);
    }

    @Test
    public void testInsert () {
        //when
        insert.canProcess("insert|tableName|column1|value1|column2|value2");
        insert.execute();
        //then
        verify(console).print("inserted successfully\n");
    }

    @Test
    public void testInsertWrongNumberOfParameters () {
        //when
        insert.canProcess("insert|tableName|");
        insert.execute();
        //then
        verify(console).print("wrong number of parameters, have to more than 4, entered : 2\n");
    }
}
