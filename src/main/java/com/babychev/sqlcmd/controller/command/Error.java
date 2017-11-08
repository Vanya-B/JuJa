package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.view.View;

public class Error implements Command {

    private View console;

    public Error (View console) {
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameters) {
        return true;
    }

    @Override
    public void execute() {
        console.print("wrong command, try again or enter help!\n");
    }
}
