package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DataSet;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.TableViewUtil;
import com.babychev.sqlcmd.view.View;

public class Update implements Command{

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Update (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("update|");
    }

    @Override
    public void execute() {
        try {
            String[] params = parameters.split("\\|");
            if (params.length < 5) {
                throw new IllegalArgumentException("wrong number of parameters, you entered : " + params.length + ", have to not less five!");
            }
            DataSet data = new DataSet();
            for (int i = 3; i < params.length; i++) {
                data.put(params[i++], params[i]);
            }
            int result = manager.update(params[1], Integer.parseInt(params[2]), data);
            if (result == 0) {
                throw new Exception("wrong parameter tableName|id|column|...");
            }
            console.print(TableViewUtil.getViewTable(manager.getTableData(params[1])) + "\n");
        } catch (Exception e) {
            console.print(e.getMessage() + "\n");
        }
    }
}
