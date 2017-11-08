package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;
import java.util.Arrays;

public class Drop implements Command{

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Drop (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("drop|");
    }

    @Override
    public void execute() {
        try {
            String[] params = parameters.split("\\|");
            if (params.length != 2) {
                throw new IllegalArgumentException("wrong number of parameters, have to 2, but entered : " + params.length);
            }
            manager.drop(params[1]);
            console.print("dropped success! list of tables : " + Arrays.toString(manager.getListTables()) + "\n");
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
