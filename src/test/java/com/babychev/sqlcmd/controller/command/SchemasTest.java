package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SchemasTest {

    private DatabaseManager manager;
    private View console;
    private Command schemas;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        schemas = new Schemas(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = schemas.canProcess("schemas");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = schemas.canProcess("qwe");
        //then
        assertFalse(actual);
    }

    @Test
    public void testSchemas () {
        //given
        Set<String> schemas = new LinkedHashSet<>(Arrays.asList("schema1", "schema2"));
        when(manager.schemas()).thenReturn(schemas);
        //when
        this.schemas.execute();
        //then
        verify(console).print("[schema1, schema2]\n");
    }

    @Test
    public void testEmptySchemas () {
        //when
        schemas.execute();
        //then
        verify(console).print("[]\n");
    }
}
