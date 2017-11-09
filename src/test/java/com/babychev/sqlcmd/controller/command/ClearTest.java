package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DataSet;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClearTest {

    private DatabaseManager manager;
    private View console;
    private Command clear;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        clear = new Clear(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = clear.canProcess("clear|users");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = clear.canProcess("clear");
        //then
        assertFalse(actual);
    }

    @Test
    public void testClear () {
        //given
        DataSet user1 = new DataSet();
        user1.put("id", null);
        user1.put("login", null);
        user1.put("password", null);
        List<DataSet> data = new LinkedList<>();
        data.add(user1);

        when(manager.getTableData("users")).thenReturn(data);

        //when
        clear.canProcess("clear|users");
        clear.execute();
        //then
        String expected = "[cleared : \n" +
                           "+--+-----+--------+\n" +
                           "|id|login|password|\n" +
                           "+--+-----+--------+\n" +
                           "\n" +
                           "]";
        shouldPrint(expected);
    }

    @Test
    public void testClearWrongParameters () {
        //when
        clear.canProcess("clear|users|qwe|qwe");
        clear.execute();
        //then
        verify(console).print("wrong number of parameters, have to 2, but entered : 4\n");
    }


    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(console).print(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
