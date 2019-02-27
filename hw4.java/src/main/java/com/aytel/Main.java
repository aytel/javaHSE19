package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static PhoneBook phoneBook;
    private static Scanner inputScanner = new Scanner(System.in);

    private static String HELP =
            "0 - exit\n" +
            "1 - add ownership\n" +
            "2 - find numbers by name\n" +
            "3 - find names by number\n" +
            "4 - delete ownership\n" +
            "5 - change ownership's name\n" +
            "6 - change ownership's number\n" +
            "7 - print all ownerships";

    private static String read(@NotNull String valueToRead) {
        System.out.println("Print " + valueToRead + ":");
        return inputScanner.next();
    }

    private static void init() {
        final String databaseName = read("name of database");
        phoneBook = new PhoneBook(databaseName);
        System.out.println("Print --help to see commands.");
    }

    public static void main(String[] args) {
        init();
        mainLoop:
        while (true) {
            String query = null;
            int queryType;
            try {
                query = read("next command");
                queryType = Integer.parseInt(query);
            } catch (NumberFormatException e) {
                if (query == null || !query.equals("--help")) {
                    System.out.println("Unknown command.");
                } else {
                    System.out.println(HELP);
                }
                continue;
            }
            switch (queryType) {
                case 0: {
                    break mainLoop;
                }
                case 1: {
                    add();
                    break;
                }
                case 2: {
                    getByName();
                    break;
                }
                case 3: {
                    getByNumber();
                    break;
                }
                case 4: {
                    remove();
                    break;
                }
                case 5: {
                    updateName();
                    break;
                }
                case 6: {
                    updateNumber();
                    break;
                }
                case 7: {
                    getAll();
                    break;
                }
                default: {
                    System.out.println("Unknown command.");
                }
            }
        }
    }

    private static void add() {
        String name = read("name");
        String number = read("number");
        System.out.println(!phoneBook.add(name, number) ? "Ok, added\n" : "Already added to phonebook.");
    }

    private static void remove() {
        String name = read("name");
        String number = read("number");
        System.out.println(phoneBook.remove(name, number) ? "Ok, removed\n" : "No such ownership in phonebook.");
    }

    private static void updateName() {
        String nameBefore = read("old name");
        String number = read("number");
        String nameAfter = read("new name");
        try {
            System.out.println(phoneBook.updateName(nameBefore, number, nameAfter)
                    ? "Ok, changed\n" : "Result of change already in phonebook.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateNumber() {
        String name = read("name");
        String numberBefore = read("old number");
        String numberAfter = read("new number");
        try {
            System.out.println(phoneBook.updateNumber(name, numberBefore, numberAfter)
                    ? "Ok, changed\n" : "Result of change already in phonebook.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getAll() {
        for (Ownership ownership : phoneBook.getAll()) {
            System.out.println(ownership.toString());
        }
    }

    private static void getByName() {
        String name = read("name");
        for (Ownership ownership : phoneBook.getByName(name)) {
            System.out.println(ownership.toString());
        }
    }

    private static void getByNumber() {
        String number = read("number");
        for (Ownership ownership : phoneBook.getByNumber(number)) {
            System.out.println(ownership.toString());
        }
    }
}
