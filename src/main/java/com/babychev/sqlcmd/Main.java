package com.babychev.sqlcmd;

import com.babychev.sqlcmd.controller.MainController;
import com.babychev.sqlcmd.controller.connection.DataSource;
import com.babychev.sqlcmd.controller.connection.PostgresqlDataSource;
import com.babychev.sqlcmd.model.DatabaseManager;
import com.babychev.sqlcmd.model.DatabaseManagerPostgreSQL;
import com.babychev.sqlcmd.view.Console;
import com.babychev.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View console = new Console();
        DataSource dataSource = new PostgresqlDataSource("src/main/resources/connection.properties");
        DatabaseManager manager = new DatabaseManagerPostgreSQL(dataSource);
        MainController controller = new MainController(manager, console);
        controller.run();
    }


}
