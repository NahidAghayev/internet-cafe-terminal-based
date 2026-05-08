package com.internetcafe.presentation;

import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.UserRole;
import com.internetcafe.domain.exceptions.AuthenticationException;
import com.internetcafe.domain.exceptions.UserAlreadyExistsException;

public class MainMenu {
    private final AppContext appContext;

    public MainMenu(AppContext appContext) {
        this.appContext = appContext;
    }

    public void show() {
        boolean running = true;

        while (running) {
            ConsoleHelper.printHeader("Internet Cafe System");
            System.out.println("0. Exit");
            System.out.println("1. Login");
            System.out.println("2. Register");

            int choice = ConsoleHelper.readInt("Choice", 0, 2);
            switch (choice) {
                case 0 -> running = false;
                case 1 -> handleLogin();
                case 2 -> handleRegister();
            }
        }
    }

    private void handleLogin() {
        String username = ConsoleHelper.readString("Username");
        String password = ConsoleHelper.readString("Password");

        try {
            User user = appContext.getAuthService().login(username, password);
            ConsoleHelper.printSuccess("Welcome, " + user.getUsername());
            if (user.getRole() == UserRole.ADMIN) {
                new AdminMenu(appContext).show();
            } else {
                new CustomerMenu(appContext).show();
            }
        } catch (AuthenticationException ex) {
            ConsoleHelper.printError(ex.getMessage());
        }
    }

    private void handleRegister() {
        String username = ConsoleHelper.readString("Username");
        String password = ConsoleHelper.readString("Password");

        try {
            appContext.getAuthService().register(username, password);
            ConsoleHelper.printSuccess("User registered successfully!");
        } catch (UserAlreadyExistsException ex) {
            ConsoleHelper.printError(ex.getMessage());
        }
    }
}
