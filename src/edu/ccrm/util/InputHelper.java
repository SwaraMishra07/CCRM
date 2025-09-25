// edu.ccrm.util.InputHelper.java (Suggested Enhancement)
package edu.ccrm.util;

import java.util.Scanner;
import java.util.InputMismatchException;

public class InputHelper {

    /**
     * Reads and validates an integer input from the console.
     * @param sc The Scanner object.
     * @param prompt The prompt message to display.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @return The validated integer choice.
     */
    public static int readInt(Scanner sc, String prompt, int min, int max) {
        int choice = -1;
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine(); // Read the entire line

            try {
                // Explicitly check for NumberFormatException
                choice = Integer.parseInt(line); 
                
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    // Specific message for out-of-range error
                    System.out.printf("Input must be between %d and %d. Try again.\n", min, max); 
                }
            } catch (NumberFormatException e) {
                // Specific message for non-numeric input
                System.out.println("Invalid input: Please enter a valid number. Try again.");
            } catch (Exception e) {
                 // Catch any other unexpected exception
                System.out.println("An unknown error occurred. Try again.");
            }
        }
    }
}