package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class SelectSchema implements Command {

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public SelectSchema (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("selectSchema|");
    }

    @Override
    public void execute() {
        try {
            String [] params = parameters.split("\\|");
            if (params.length != 2) {
                throw new IllegalArgumentException("wrong number of parameters, have to 2, entered : " + params.length + "\n");
            }
            String schemaName = params[1];
            String result = manager.selectSchema(schemaName);
            if (!schemaName.equals(result)) {
                throw new IllegalArgumentException(String.format("cannot find schema '%s' \n", schemaName));
            } else {
                console.print(String.format("'%s' schema is selected \n", schemaName));
            }
        } catch (Exception e) {
            console.print(e.getMessage());
        }
    }
}
