package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class Schema implements Command {

    private View console;
    private DatabaseManager manager;
    private String parameters;

    public Schema (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("createSchema|");
    }

    @Override
    public void execute() {
        try{
            String[] params = parameters.split("\\|");
            if (params.length != 2) {
                throw new IllegalArgumentException("wrong number of parameters, expected 2 but have : " + params.length);
            }
            String schemaName = params[1];
            manager.createSchema(schemaName);
            console.print(String.format("congratulation, schema %s is created \n", schemaName));
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
