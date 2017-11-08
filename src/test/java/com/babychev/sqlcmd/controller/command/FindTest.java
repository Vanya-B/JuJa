package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DataSet;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FindTest {

    private DatabaseManager manager;
    private View console;
    private Command find;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        find = new Find(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = find.canProcess("find|users");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = find.canProcess("find");
        //then
        assertFalse(actual);
    }

    @Test
    public void testPrintTableData () {
        //given
        DataSet user1 = new DataSet();
        DataSet user2 = new DataSet();
        user1.put("id", "1");
        user1.put("login", "John");
        user1.put("password", "******");
        user2.put("id", "2");
        user2.put("login", "Sara");
        user2.put("password", "****");
        DataSet[] data = new DataSet[] {user1, user2};

        when(manager.getTableData("users")).thenReturn(data);
        //when
        find.canProcess("find|users");
        find.execute();
        //then
        String expected = "[+--+-----+--------+\n" +
                           "|id|login|password|\n" +
                           "+--+-----+--------+\n" +
                           "| 1| John|  ******|\n" +
                           "+--+-----+--------+\n" +
                           "| 2| Sara|    ****|\n" +
                           "+--+-----+--------+\n" +
                           "\n" +
                           "]";
        shouldPrint(expected);
    }

    @Test
    public void testPrintEmptyTableData () {
        //given
        DataSet user1 = new DataSet();
        user1.put("id", null);
        user1.put("login", null);
        user1.put("password", null);
        DataSet[] data = new DataSet[] {user1};

        when(manager.getTableData("users")).thenReturn(data);
        //when
        find.canProcess("find|users");
        find.execute();
        //then
        String expected = "[+--+-----+--------+\n" +
                           "|id|login|password|\n" +
                           "+--+-----+--------+\n" +
                           "\n" +
                           "]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(console).print(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
