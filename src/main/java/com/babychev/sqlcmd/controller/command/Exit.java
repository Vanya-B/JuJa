package com.babychev.sqlcmd.controller.command;

import com.babychev.sqlcmd.controller.exception.ExitException;
import com.babychev.sqlcmd.view.View;

public class Exit implements Command{

    private View console;

    public Exit (View console) {
        this.console = console;
    }

    @Override
    public boolean canProcess(String parameter) {
        return parameter.equals("exit");
    }

    @Override
    public void execute() {
        console.print("============= Good Bay =============\n");
        throw new ExitException();
    }
}
