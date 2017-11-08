package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SelectSchemaTest {

    private DatabaseManager manager;
    private View console;
    private Command selectSchema;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        selectSchema = new SelectSchema(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = selectSchema.canProcess("selectSchema|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = selectSchema.canProcess("selectSchema");
        //then
        assertFalse(actual);
    }

    @Test
    public void testSelectSchema () {
        //given
        when(manager.selectSchema("schemaName")).thenReturn("schemaName");
        //when
        selectSchema.canProcess("selectSchema|schemaName");
        selectSchema.execute();
        //then
        verify(console).print("'schemaName' schema is selected \n");
    }

    @Test
    public void testSelectSchemaNotExist () {
        //given
        when(manager.selectSchema("schemaName")).thenReturn("schemaName");
        //when
        selectSchema.canProcess("selectSchema|notExistSchema");
        selectSchema.execute();
        //then
        verify(console).print("cannot find schema 'notExistSchema' \n");
    }

    @Test
    public void testSelectSchemaWrongNumberOfParameters () {
        //when
        selectSchema.canProcess("selectSchema|");
        selectSchema.execute();
        //then
        verify(console).print("wrong number of parameters, have to 2, entered : 1\n");
    }
}
