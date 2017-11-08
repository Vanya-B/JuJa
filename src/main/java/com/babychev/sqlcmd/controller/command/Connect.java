package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class Connect implements Command {

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Connect (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("connect|");
    }

    @Override
    public void execute() {
            try{
                String[] params = parameters.split("\\|");
                if (params.length > 4 || params.length < 4) {
                    throw new IllegalArgumentException("wrong number of parameters, expected 4 but have : " + params.length);
                }
                    manager.connect(params);
                    console.print("congratulation, connected was success\n");
            } catch (Exception e) {
                console.print(e.getMessage() + "\n");
            }
    }
}
