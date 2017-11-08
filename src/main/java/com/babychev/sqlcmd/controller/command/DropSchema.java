package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class DropSchema implements Command {

    private String parameters;
    private DatabaseManager manager;
    private View console;

    public DropSchema (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("dropSchema|");
    }

    @Override
    public void execute() {
        try{
            String[] params = parameters.split("\\|");
            if (params.length != 2) {
                throw new IllegalArgumentException("wrong number of parameters, expected 2 but have : " + params.length);
            }
            String schemaName = params[1];
            manager.dropSchema(schemaName);
            console.print(String.format("congratulation, schema %s is deleted \n", schemaName));
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
