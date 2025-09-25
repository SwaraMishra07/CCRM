package edu.ccrm.exceptions;

/**
 * Custom checked exception thrown when a student's enrollment exceeds 
 * the maximum allowed credits for a given semester.
 */
public class MaxCreditLimitExceededException extends Exception {
    public MaxCreditLimitExceededException(String message) {
        super(message);
    }
}