package com.internetcafe.presentation;

import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.entities.Station;
import com.internetcafe.domain.entities.Session;
import com.internetcafe.domain.entities.Transaction;
import com.internetcafe.service.SessionContext;
import java.math.BigDecimal;
import java.util.List;

public class CustomerMenu {
    private final AppContext appContext;

    public CustomerMenu(AppContext appContext) {
        this.appContext = appContext;
    }

    public void show() {
        boolean running = true;
        while (running) {
            User user = SessionContext.getCurrentUser();
            ConsoleHelper.printHeader("===== CUSTOMER MENU =====");
            ConsoleHelper.printHeader("Logged in as:  " + user.getUsername() + " | Balance: " + user.getBalance() + " AZN");
            System.out.println("1. View Available Stations");
            System.out.println("2. Start Session");
            System.out.println("3. End Session");
            System.out.println("4. Top up balance");
            System.out.println("5. My session history");
            System.out.println("6. My transaction history");
            System.out.println("0. Logout");

            int choice = ConsoleHelper.readInt("Choice: ", 0, 6);
            if (choice == 0) {
                running = false;
            } else {
                handleChoice(choice);
            }
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> viewAvailableStations();
                case 2 -> startSession();
                case 3 -> endSession();
                case 4 -> topUpBalance();
                case 5 -> viewSessionHistory();
                case 6 -> viewTransactionHistory();
            }
        } catch (Exception ex) {
            ConsoleHelper.printError(ex.getMessage());
        }
        ConsoleHelper.pressEnterToContinue();
    }

    private void viewAvailableStations() {
        List<Station> stations = appContext.getStationService().getAvailableStations();
        if (stations.isEmpty()) {
            System.out.println("No stations available right now.");
            return;
        }
        ConsoleHelper.printHeader("AVAILABLE STATIONS");
        for (Station s : stations) {
            System.out.printf("ID: %d | Name: %s | Rate: %.2f AZN/hr | Specs: %s%n",
                    s.getId(), s.getName(), s.getHourlyRate(), s.getSpecs());
        }
    }

    private void startSession() {
        List<Station> stations = appContext.getStationService().getAvailableStations();

        viewAvailableStations();
        if (!stations.isEmpty()) {
            int stationId = ConsoleHelper.readInt("Enter Station ID to start: ");
            appContext.getSessionService().startSession(stationId);
            ConsoleHelper.printSuccess("Session started successfully!");
        }
    }

    private void endSession() {
        appContext.getSessionService().endSession();
        ConsoleHelper.printSuccess("Session ended successfully!");
    }

    private void topUpBalance() {
        BigDecimal value = ConsoleHelper.readDecimal("Enter amount to top up: ");
        appContext.getTransactionService().topUp(value);
        ConsoleHelper.printSuccess("Balance topped up successfully!");
    }

    private void viewSessionHistory() {
        List<Session> history = appContext.getSessionService().getSessionHistory();
        if (history.isEmpty()) {
            System.out.println("No sessions found.");
            return;
        }
        ConsoleHelper.printHeader("SESSION HISTORY");
        for (Session s : history) {
            String end = (s.getEndTime() == null) ? "STILL ACTIVE" : s.getEndTime().toString();
            String cost = (s.getCost() == null) ? "0.00" : s.getCost().toString();
            System.out.printf("Station ID: %d | Start: %s | End: %s | Cost: %s AZN%n",
                    s.getStationId(), s.getStartTime(), end, cost);
        }
    }

    private void viewTransactionHistory() {
        List<Transaction> history = appContext.getTransactionService().getHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        ConsoleHelper.printHeader("TRANSACTION HISTORY");
        for (Transaction t : history) {
            System.out.printf("[%s] %s: %.2f AZN%n",
                    t.getCreatedAt(), t.getType(), t.getAmount());
        }
    }
}