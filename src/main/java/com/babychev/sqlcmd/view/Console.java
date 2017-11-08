package com.babychev.sqlcmd.view;

import java.util.Scanner;

public class Console implements View {

    Scanner scanner;

    public void println(String msg) {
        System.out.println(msg);
    }

    @Override
    public void print(String msg) {
        System.out.print(msg);
    }

    @Override
    public String read() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner.nextLine();
    }
}
