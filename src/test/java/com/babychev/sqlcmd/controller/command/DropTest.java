package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DropTest {

    private DatabaseManager manager;
    private View console;
    private Command drop;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        drop = new Drop(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = drop.canProcess("drop|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = drop.canProcess("drop");
        //then
        assertFalse(actual);
    }

    @Test
    public void testDropTable () {
        //given
        Set<String> tables = new LinkedHashSet<>();
        when(manager.getListTables()).thenReturn(tables);
        //when
        drop.canProcess("drop|tableName");
        drop.execute();
        //then
        verify(console).print("dropped success! list of tables : []\n");
    }

    @Test
    public void testDropTableWrongNumberOfParameters () {
        //when
        drop.canProcess("drop");
        drop.execute();
        //then
        verify(console).print("wrong number of parameters, have to 2, but entered : 1\n");
    }
}
