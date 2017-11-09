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

public class DataBasesTest {

    private DatabaseManager manager;
    private View console;
    private Command databases;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        databases = new DataBases(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = databases.canProcess("databases");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = databases.canProcess("datas");
        //then
        assertFalse(actual);
    }

    @Test
    public void testDatabases () {
        //given
        Set<String> database = new LinkedHashSet<>(Arrays.asList("test_db1", "test_db2"));
        when(manager.databases()).thenReturn(database);
        //when
        databases.execute();
        //then
        verify(console).print("[test_db1, test_db2]\n");
    }

    @Test
    public void testEmptyDatabases () {
        //when
        databases.execute();
        //then
        verify(console).print("[]\n");
    }
}
