package com.babychev.sqlcmd.controller;


import com.babychev.sqlcmd.controller.command.*;
import com.babychev.sqlcmd.controller.command.Error;
import com.babychev.sqlcmd.controller.exception.ExitException;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.view.View;

public class MainController {

    private DatabaseManager manager;
    private View console;
    private Command [] commands;

    public MainController(DatabaseManager manager, View console) {
        this.manager = manager;
        this.console = console;
        this.commands = new Command[]{new Connect(manager, console),
                                      new Help(console),
                                      new Exit(console),
                                      new IsConnected(manager, console),
                                      new Schema(manager, console),
                                      new Schemas(manager, console),
                                      new DropSchema(manager, console),
                                      new SelectSchema(manager, console),
                                      new DataBases(manager, console),
                                      new CreateDatabase(manager, console),
                                      new DropDatabase(manager, console),
                                      new SelectDatabase(manager, console),
                                      new Tables(manager, console),
                                      new Find(manager, console),
                                      new Update(manager, console),
                                      new Insert(manager, console),
                                      new Clear(manager, console),
                                      new Create(manager, console),
                                      new Drop(manager, console),
                                      new Error(console)};
    }

    public void run() {

        console.print("================== Welcome to SQLcmd ==================" + '\n' +
                "Enter connect|dataBaseName|userName|password to connect to DB or enter help\n");
        try {
            while (true) {
                String input = console.read();
                for (Command command : commands) {
                    if (command.canProcess(input)) {
                        command.execute();
                        break;
                    }
                }
            }
        } catch (ExitException e) {
            //do nothing
        }
    }
}
