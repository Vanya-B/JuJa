package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TablesTest {

    private DatabaseManager manager;
    private View console;
    private Command tables;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        tables = new Tables(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = tables.canProcess("tables");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = tables.canProcess("qwe");
        //then
        assertFalse(actual);
    }

    @Test
    public void testTables () {
        //given
        String [] listTables = new String[] {"table1", "table2"};
        when(manager.getListTables()).thenReturn(listTables);
        //when
        tables.execute();
        //then
        verify(console).print("[table1, table2]\n");
    }

    @Test
    public void testEmptyTables () {
        //when
        tables.execute();
        //then
        verify(console).print("null\n");
    }
}
