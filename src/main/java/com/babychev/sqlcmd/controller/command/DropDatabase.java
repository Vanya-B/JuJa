package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class DropDatabase implements Command {

    private String parameters;
    private DatabaseManager manager;
    private View console;

    public DropDatabase (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("dropDatabase|");
    }

    @Override
    public void execute() {
        try {
            String [] params = parameters.split("\\|");
            if (params.length > 2 || params.length < 2) {
                throw new IllegalArgumentException("wrong number of parameters, expected 2 but have : " + params.length);
            }
            String databaseName = params[1];
            manager.dropDatabase(databaseName);
            console.print(String.format("congratulation, database %s is deleted \n", databaseName));
        } catch (Exception e ) {
            console.print(e.getMessage() + "\n");
        }
    }
}
