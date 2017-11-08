package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DataSet;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.TableViewUtil;
import com.babychev.sqlcmd.view.View;

public class Find implements Command{

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Find (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("find|");
    }

    @Override
    public void execute() {
        try {
            DataSet[] data = null;
            String table = null;
            String [] params = parameters.split("\\|");
            if (params.length == 2) {
                data = manager.getTableData(params[1]);
                if (data != null) {
                    table = TableViewUtil.getViewTable(data);
                }
            } else if (params.length == 4) {
                if (Integer.valueOf(params[2]) == 0) {
                    throw new IllegalArgumentException("LIMIT cannot be less than 1");
                }
                data = manager.getTableDataLimit(params[1], Integer.valueOf(params[2]), Integer.valueOf(params[3]));
                if (data != null) {
                    table = TableViewUtil.getViewTable(data);
                }
            } else {
                throw new IllegalArgumentException("wrong number of parameters! you entered " + params.length + ", but have to 2 or 4");
            }
            console.print(table + "\n");
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
