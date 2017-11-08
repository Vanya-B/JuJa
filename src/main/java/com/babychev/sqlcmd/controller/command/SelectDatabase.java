package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class SelectDatabase implements Command {

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public SelectDatabase (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("selectDatabase|");
    }

    @Override
    public void execute() {
        try {
            String [] params = parameters.split("\\|");
            if (params.length != 2) {
                throw new IllegalArgumentException("wrong number of parameters, have to 2, entered : " + params.length + "\n");
            }
            String databaseName = params[1];
            String result = manager.selectDatabase(databaseName);
            if (!databaseName.equals(result)) {
                throw new IllegalArgumentException(String.format("cannot find database '%s' \n", databaseName));
            } else {
                console.print(String.format("'%s' database is selected \n", databaseName));
            }
        } catch (Exception e) {
            console.print(e.getMessage());
        }
    }
}
