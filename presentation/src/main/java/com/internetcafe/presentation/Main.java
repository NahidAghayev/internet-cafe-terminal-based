package com.internetcafe.presentation;


import com.internetcafe.dataaccess.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java -jar app.jar <DB_URL> <DB_USER> <DB_PASSWORD>");
            return;
        }

        String url = args[0];
        String user = args[1];
        String pass = args[2];

        try {
            ConnectionManager connectionManager = new ConnectionManager(url, user, pass);
            AppContext context = new AppContext(connectionManager);

            MainMenu mainMenu = new MainMenu(context);
            mainMenu.show();
            System.out.println("System initialized successfully.");
        } catch (Exception ex) {
            System.err.println("Failed to start application: " + ex.getMessage());
        }
    }
}