package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.view.View;

public class Help implements Command{

    private View console;

    public Help (View console) {
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameter) {
        return parameter.equals("help");
    }

    @Override
    public void execute() {
        console.print("\"commands : \n"
                + "\t* connect|dataBaseName|userName|password \n"
                + "\t* databases \n"
                + "\t* createDatabase|databaseName \n"
                + "\t* dropDatabase|databaseName \n"
                + "\t* schemas \n"
                + "\t* createSchema|schemaName \n"
                + "\t* dropSchema|schemaName \n"
                + "\t* selectSchema|schemaName \n"
                + "\t* tables \n"
                + "\t* clear|tableName \n"
                + "\t* drop|tableName \n"
                + "\t* create|tableName|column1|column2|...|columnN \n"
                + "\t\t* example : create|users|id INT PRIMARY KEY|name VARCHAR(10)|... \n"
                + "\t* find|tableName \n"
                + "\t\t* find|tableName|LIMIT|OFFSET \n"
                + "\t* insert|tableName|column1|value1|column2|value2|...|columnN|valueN \n"
                + "\t* update|tableName|id|column|value|columnN|valueN \n"
                + "\t* exit \n");
    }
}
