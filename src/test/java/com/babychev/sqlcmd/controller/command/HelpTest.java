package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.view.View;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HelpTest {

    private View console = mock(View.class);

    @Test
    public void testCanProcess () {
        //given
        Command help = new Help(console);
        //when
        boolean actual = help.canProcess("help");
        //then
        assertTrue(actual);
    }

    @Test
    public void testCannotProcess () {
        //given
        Command help = new Help(console);
        //when
        boolean actual = help.canProcess("qwe");
        //then
        assertFalse(actual);
    }

    @Test
    public void testExecute () {
        //given
        Command help = new Help(console);
        //when
        help.execute();
        //then
        verify(console).print("\"commands : \n" +
                "\t* connect|dataBaseName|userName|password \n" +
                "\t* databases \n" +
                "\t* createDatabase|databaseName \n" +
                "\t* dropDatabase|databaseName \n" +
                "\t* schemas \n" +
                "\t* createSchema|schemaName \n" +
                "\t* dropSchema|schemaName \n" +
                "\t* selectSchema|schemaName \n" +
                "\t* tables \n" +
                "\t* clear|tableName \n" +
                "\t* drop|tableName \n" +
                "\t* create|tableName|column1|column2|...|columnN \n" +
                "\t\t* example : create|users|id INT PRIMARY KEY|name VARCHAR(10)|... \n" +
                "\t* find|tableName \n" +
                "\t\t* find|tableName|LIMIT|OFFSET \n" +
                "\t* insert|tableName|column1|value1|column2|value2|...|columnN|valueN \n" +
                "\t* update|tableName|id|column|value|columnN|valueN \n" +
                "\t* exit \n");
    }
}
