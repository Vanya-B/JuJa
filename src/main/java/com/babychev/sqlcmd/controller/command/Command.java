package com.babychev.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(String parameter);

    void execute();
}
