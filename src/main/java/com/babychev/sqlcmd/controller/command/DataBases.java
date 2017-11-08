package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import java.util.Arrays;

public class DataBases implements Command {
    private View console;
    private DatabaseManager manager;

    public DataBases (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        return parameters.equals("databases");
    }

    @Override
    public void execute() {
        console.print(Arrays.toString(manager.databases()) + "\n");
    }
}
