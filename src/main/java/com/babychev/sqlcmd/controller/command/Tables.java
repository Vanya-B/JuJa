package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

import java.util.Arrays;

public class Tables implements Command{

    private DatabaseManager manager;
    private View console;

    public Tables (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameter) {
        return parameter.equals("tables");
    }

    @Override
    public void execute() {
        console.print(Arrays.toString(manager.getListTables()) + "\n");
    }
}
