package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DataSet;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpdateTest {

    private DatabaseManager manager;
    private View console;
    private Command update;

    @Before
    public void setup () {
        manager = mock(DatabaseManager.class);
        console = mock(View.class);
        update = new Update(manager, console);
    }

    @Test
    public void testCanProcess () {
        //when
        boolean actual = update.canProcess("update|");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //when
        boolean actual = update.canProcess("qwe");
        //then
        assertFalse(actual);
    }

    @Test
    public void testUpdate () {
        //given
        DataSet data = new DataSet();
        data.put("id", "1");
        data.put("column1", "value1");
        DataSet [] datas = new DataSet[1];
        datas[0] = data;
        when(manager.update(Mockito.anyString(), Mockito.anyInt(), Mockito.any(DataSet.class))).thenReturn(1);
        when(manager.getTableData("tableName")).thenReturn(datas);
        //when
        update.canProcess("update|tableName|1|column1|value1");
        update.execute();
        //then
        verify(console).print("+--+-------+\n" +
                              "|id|column1|\n" +
                              "+--+-------+\n" +
                              "| 1| value1|\n" +
                              "+--+-------+\n\n");
    }

    @Test
    public void testUpdateWrongNumberOfParameters () {
        //when
        update.canProcess("update|tableName|1");
        update.execute();
        //then
        verify(console).print("wrong number of parameters, you entered : 3, have to not less five!\n");
    }

    @Test
    public void testUpdateWrongParameters () {
        //when
        update.canProcess("update|tableName|1|column1|value1");
        update.execute();
        //then
        verify(console).print("wrong parameter tableName|id|column|...\n");
    }
}
