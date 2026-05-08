package com.internetcafe.presentation;

import java.math.BigDecimal;
import java.util.*;

public class ConsoleHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt + ": ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
            }
        }

        return input;
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number < min || number > max) {
                    System.out.println("Error: Please enter a number between " + min + " and " + max + ".");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static BigDecimal readDecimal(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal.");
            }
        }
    }

    public static void printHeader(String title) {
        System.out.println("===== " + title.toUpperCase() + " =====");
    }

    public static void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void printLine() {
        System.out.println("--------------------");
    }

    public static void pressEnterToContinue() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

}