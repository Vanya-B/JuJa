package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropSchemaTest {

    private DatabaseManager manager;
    private View console;
    private Command dropSchema;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        dropSchema = new DropSchema(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = dropSchema.canProcess("dropSchema|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = dropSchema.canProcess("dropSchema");
        //then
        assertFalse(actual);
    }

    @Test
    public void testDropSchema () {
        //when
        dropSchema.canProcess("dropSchema|schemaName");
        dropSchema.execute();
        //then
        verify(console).print("congratulation, schema schemaName is deleted \n");
    }

    @Test
    public void testDropSchemaWrongNumberOfParameters () {
        //when
        dropSchema.canProcess("dropSchema|");
        dropSchema.execute();
        //then
        verify(console).print("wrong number of parameters, expected 2 but have : 1\n");
    }
}
