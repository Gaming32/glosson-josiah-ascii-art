package io.github.gaming32;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Josiah (Gaming32) Glosson
 */
public class GlossonJosiahASCIIArt {
    public static void main(String[] args) {
        int floorCount;
        boolean animate;

        // Ask the user for the number of floors
        // (17 is required for 400 characters)
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many floors would you like? (default: 17) ");
        try {
            floorCount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            floorCount = 17;
            System.out.println("Defaulted to 17 floors");
        }
        newLine();

        final boolean[][] floors = printBuilding(floorCount);

        System.out.print("Do you want the building to be animated? (default: false) ");
        animate = Boolean.parseBoolean(scanner.nextLine());
        if (animate) {
            animateBulding(floors);
        }
    }

    static void animateBulding(final boolean[][] floors) {
        ansiEscape("D", 1000);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            int floor = ThreadLocalRandom.current().nextInt(floors.length);
            animateFloor(floor, floors);
        }
    }

    static void animateFloor(int floor, boolean[][] floors) {
        boolean[] floorData = floors[floor];
        int up = floors.length - floor + 2;
        ansiEscape("A", up);
        floorData[3] = !floorData[3];
        int color = floorData[3] ? 43 : 0;
        for (int i = 0; i < 3; i++) {
            ansiEscape("C", 3);
            if (floorData[i]) {
                ansiEscape("m", color);
                print("\u00af");
            } else {
                ansiEscape("C", 1);
            }
        }
        ansiEscape("D", 1000);
        ansiEscape("B", up);
    }

    static void ansiEscape(String escape, int... args) {
        StringBuilder code = new StringBuilder("\u001b[");
        if (args.length > 0) {
            code.append(args[0]);
            for (int i = 1; i < args.length; i++) {
                code.append(";");
                code.append(args[i]);
            }
        }
        code.append(escape);
        print(code.toString());
    }

    /**
     * Prints an ASCII art building
     * @param floorCount The number of floors in the building
     */
    static boolean[][] printBuilding(final int floorCount) {
        header();
        final boolean[][] floors = new boolean[floorCount][4];
        for (int i = 0; i < floorCount; i++) {
            printFloor(i, floors);
        }
        footer();
        return floors;
    }

    /**
     * Print the roof of the building (including a fancy flag)
     */
    static void header() {
        printPad(7, "|\u00af\u00af|\n");
        printPad(7, "|\u00af\u00af\n");
        printPad(7, "|\n");
        for (int i = 0; i < 6; i++) {
            printPad(6 - i, "/");
            printPad(i * 2 + 1, "\\");
            newLine();
        }
    }

    /**
     * Print a single floor of the building
     * @param i The number of the floor (the top floor is {@code 0})
     * @param floors 2D array of booleans where each row is a floor
     * and each column indicates whether there's a window in that column
     */
    static void printFloor(int i, boolean[][] floors) {
        print("| ");
        for (int j = 0; j < 3; j++) {
            boolean window = ThreadLocalRandom.current().nextBoolean();
            floors[i][j] = window;
            if (window) {
                print("|\u00af|");
            } else if (i > 0 && floors[i - 1][j]) {
                print(" \u00af ");
            } else {
                printN(" ", 3);
            }
            print(" ");
        }
        print("|\n");
    }

    /**
     * Print the ground below the building
     */
    static void footer() {
        printN("\u00af", 15);
        newLine();
    }

    /**
     * Print the string {@code s} with {@code pad} spaces before it
     * @param pad The number of spaces to print
     * @param s The string to print after the spaces
     */
    static void printPad(int pad, String s) {
        printN(" ", pad);
        print(s);
    }

    /**
     * Print the string {@code s} {@code n} times
     * @param s The string to print
     * @param n The number of times to print the string
     */
    static void printN(String s, int n) {
        for (int i = 0; i < n; i++) {
            print(s);
        }
    }

    /**
     * Prints a string (without an automatic newline).
     * Analogous to {@code System.out.print(s)}.
     * @param s The string to print
     */
    static void print(String s) {
        System.out.print(s);
    }

    /**
     * Creates a newline.
     * Analogous to {@code System.out.println()}.
     */
    static void newLine() {
        System.out.println();
    }
}
