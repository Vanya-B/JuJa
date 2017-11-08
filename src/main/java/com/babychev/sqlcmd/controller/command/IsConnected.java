package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class IsConnected implements Command {

    private DatabaseManager manager;
    private View console;
    private String parameter;

    public IsConnected (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameter) {
        this.parameter = parameter;
        return !manager.isConnected();
    }

    @Override
    public void execute() {
        console.print(String.format("You cannot use command '%s' , before you are not connect \n", parameter));
    }
}
