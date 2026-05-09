package com.internetcafe.presentation;

import com.internetcafe.domain.entities.Session;
import com.internetcafe.domain.entities.Station;
import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.StationStatus;
import com.internetcafe.service.SessionContext;

import java.math.BigDecimal;
import java.util.List;

public class AdminMenu {
    private final AppContext appContext;

    public AdminMenu(AppContext appContext) {
        this.appContext = appContext;
    }

    public void show() {
        boolean running = true;
        while (running) {
            User user = SessionContext.getCurrentUser();
            ConsoleHelper.printHeader("===== ADMIN MENU =====");
            ConsoleHelper.printHeader("Logged in as:  " + user.getUsername());
            System.out.println("1. View all stations");
            System.out.println("2. Add station");
            System.out.println("3. Update station");
            System.out.println("4. Delete station");
            System.out.println("5. View all sessions");
            System.out.println("6. View all users");
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
                case 1 -> listAllStations();
                case 2 -> addStation();
                case 3 -> updateStation();
                case 4 -> deleteStation();
                case 5 -> viewAllSessions();
                case 6 -> viewAllUsers();
            }
        } catch (Exception ex) {
            ConsoleHelper.printError(ex.getMessage());
        }
        ConsoleHelper.pressEnterToContinue();
    }

    private void listAllStations() {
        List<Station> stations = appContext.getStationService().getAllStations();
        if (stations.isEmpty()) {
            System.out.println("There are no stations right now.");
            return;
        }

        ConsoleHelper.printHeader("ALL STATIONS");
        for (Station s : stations) {
            System.out.printf("ID: %d | Name: %s | Rate: %.2f AZN/hr | Specs: %s%n",
                    s.getId(), s.getName(), s.getHourlyRate(), s.getSpecs());
        }
    }

    private void addStation() {
        String name = ConsoleHelper.readString("Station Name: ");
        String specs = ConsoleHelper.readString("Specs: ");
        BigDecimal rate = ConsoleHelper.readDecimal("Hourly Rate: ");
        appContext.getStationService().addStation(name, specs, rate);
        ConsoleHelper.printSuccess("Station added!");
    }

    private void updateStation() {
        listAllStations();
        int id = ConsoleHelper.readInt("Enter Station ID to update: ");
        String name = ConsoleHelper.readString("New Name: ");
        String specs = ConsoleHelper.readString("New Specs: ");
        BigDecimal rate = ConsoleHelper.readDecimal("New Hourly Rate: ");
        System.out.println("Select Status (0: AVAILABLE, 1: IN_USE, 2: MAINTENANCE): ");
        int statusIdx = ConsoleHelper.readInt("Status: ", 0, 2);
        StationStatus status = StationStatus.values()[statusIdx];

        appContext.getStationService().updateStation(id, name, specs, rate, status);
        ConsoleHelper.printSuccess("Station updated successfully!");
    }

    private void deleteStation() {
        listAllStations();
        int id = ConsoleHelper.readInt("Enter Station ID to DELETE: ");
        appContext.getStationService().deleteStation(id);
        ConsoleHelper.printSuccess("Station deleted successfully!");
    }

    private void viewAllSessions() {
        List<Session> sessions = appContext.getSessionService().getAllSessions();
        if (sessions.isEmpty()) {
            System.out.println("There are no sessions right now.");
            return;
        }

        ConsoleHelper.printHeader("ALL SESSIONS");
        for (Session s : sessions) {
            System.out.printf(
                    "ID: %d | User ID: %d | Station ID: %d | Start: %s | End: %s | Cost: %s AZN%n",
                    s.getId(),
                    s.getUserId(),
                    s.getStationId(),
                    s.getStartTime(),
                    s.getEndTime(),
                    s.getCost()
            );
        }
    }

    private void viewAllUsers() {
        List<User> users = appContext.getAuthService().getAllUsers();
        if (users.isEmpty()) {
            System.out.println("There are no users right now.");
            return;
        }
        for (User u : users) {
            System.out.printf(
                    "ID: %d | Username: %s | Role: %s | Balance: %s AZN | Created At: %s%n",
                    u.getId(),
                    u.getUsername(),
                    u.getRole(),
                    u.getBalance(),
                    u.getCreatedAt()
            );
        }
    }
}
