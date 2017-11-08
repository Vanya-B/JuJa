package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.TableViewUtil;
import com.babychev.sqlcmd.view.View;

public class Clear implements Command{

    private DatabaseManager manager;
    private View console;
    private String parameters;

    public Clear (DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        this.parameters = parameters;
        return parameters.startsWith("clear|");
    }

    @Override
    public void execute() {
        try{
            String [] params = parameters.split("\\|");
            if (params.length != 2) {
                throw new IllegalArgumentException("wrong number of parameters, have to 2, but entered : " + params.length);
            }
            manager.clear(params[1]);
            String result = TableViewUtil.getViewTable(manager.getTableData(params[1]));
            console.print("cleared : \n" + result + "\n");
        } catch (IllegalArgumentException e) {
            console.print(e.getMessage() + "\n");
        } catch (Exception ex) {
            console.print(ex.getMessage() + "\n");
        }
    }
}
