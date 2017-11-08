package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class Create implements Command{

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Create (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("create|");
    }

    @Override
    public void execute() {
        try {
            String[] params = parameters.split("\\|");
            if (params.length < 3) {
                throw new IllegalArgumentException("wrong number of parameters, have to more than 3, entered : " + params.length);
            }
            String[] columns = new String[params.length - 2];
            System.arraycopy(params, 2, columns, 0, params.length - 2);
            manager.create(params[1], columns);
            console.print("table created : " + params[1] + "\n");
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
