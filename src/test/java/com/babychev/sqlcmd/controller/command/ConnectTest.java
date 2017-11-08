package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectTest {

    private DatabaseManager manager;
    private View console;
    private Command connect;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        connect = new Connect(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = connect.canProcess("connect|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = connect.canProcess("connect");
        //then
        assertFalse(actual);
    }

    @Test
    public void testConnect () {
        //when
        connect.canProcess("connect|dbName|userName|password");
        connect.execute();
        //then
        verify(console).print("congratulation, connected was success\n");
    }

    @Test
    public void testConnectWrongNumberOfParameters () {
        //when
        connect.canProcess("connect|dbName|userName|password|param");
        connect.execute();
        //then
        verify(console).print("wrong number of parameters, expected 4 but have : 5\n");
    }
}
