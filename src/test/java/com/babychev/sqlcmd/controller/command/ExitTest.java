package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.controller.exception.ExitException;
import com.babychev.sqlcmd.view.View;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExitTest {

    private View console = mock(View.class);

    @Test
    public void testCanProcess () {
        //given
        Command exit = new Exit(console);
        //when
        boolean actual = exit.canProcess("exit");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCanNotProcess () {
        //given
        Command exit = new Exit(console);
        //when
        boolean actual = exit.canProcess("exitqwe");
        //then
        assertFalse(actual);
    }

    @Test
    public void testExecute () {
        //given
        Command exit = new Exit(console);
        //when
        try {
            exit.execute();
            fail("Expected ExitException");
        } catch (ExitException e) {
            //doNothing
        }

        //then
        verify(console).print("============= Good Bay =============\n");
    }
}
