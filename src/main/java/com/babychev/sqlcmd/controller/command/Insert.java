package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DataSet;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class Insert implements Command{

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Insert (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("insert|");
    }

    @Override
    public void execute() {
        try {
            String[] params = parameters.split("\\|");
            if (params.length < 4) {
                throw new IllegalArgumentException("wrong number of parameters, have to more than 4, entered : " + params.length);
            }
            DataSet data = new DataSet();
            for (int i = 2; i < params.length; i++) {
                data.put(params[i], params[++i]);
            }
            manager.insert(params[1], data);
            console.print("inserted successfully\n");
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
