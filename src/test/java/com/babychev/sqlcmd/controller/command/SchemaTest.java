package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SchemaTest {

    private DatabaseManager manager;
    private View console;
    private Command schema;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        schema = new Schema(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = schema.canProcess("createSchema|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = schema.canProcess("createSchema");
        //then
        assertFalse(actual);
    }

    @Test
    public void testCreateSchema () {
        //when
        schema.canProcess("createSchema|schemaName");
        schema.execute();
        //then
        verify(console).print("congratulation, schema schemaName is created \n");
    }

    @Test
    public void testCreateSchemaWrongNumberOfParameters () {
        //when
        schema.canProcess("createSchema|");
        schema.execute();
        //then
        verify(console).print("wrong number of parameters, expected 2 but have : 1\n");
    }
}
