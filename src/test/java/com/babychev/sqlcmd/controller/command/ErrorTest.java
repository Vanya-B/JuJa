package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.view.View;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ErrorTest {

    private View console = mock(View.class);

    @Test
    public void testCanProcess () {
        //given
        Command error = new Error(console);
        //when
        boolean actual = error.canProcess("qwe");
        //then
        assertTrue(actual);
    }

    @Test
    public void testExecute () {
        //given
        Command error = new Error(console);
        //when
        error.execute();
        //then
        verify(console).print("wrong command, try again or enter help!\n");
    }
}
