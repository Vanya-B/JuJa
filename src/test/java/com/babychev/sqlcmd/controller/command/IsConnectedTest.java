package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IsConnectedTest {

    private DatabaseManager manager;
    private View console;
    private Command isConnect;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        isConnect = new IsConnected(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = isConnect.canProcess("qwe");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //given
        when(manager.isConnected()).thenReturn(true);
        //when
        boolean actual = isConnect.canProcess("qwe");
        //then
        assertFalse(actual);
    }

    @Test
    public void testExecute () {
        //when
        isConnect.canProcess("qwe");
        isConnect.execute();
        //then
        verify(console).print("You cannot use command 'qwe' , before you are not connect \n");
    }
}
