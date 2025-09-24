package edu.ccrm.util;

import java.util.Scanner;

public class InputHelper {

    public static int readInt(Scanner sc, String prompt, int min, int max) {
        int choice;
        while (true) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(sc.nextLine());
                if (choice >= min && choice <= max) return choice;
            } catch (Exception ignored) {}
            System.out.println("Invalid input. Try again.");
        }
    }
}
