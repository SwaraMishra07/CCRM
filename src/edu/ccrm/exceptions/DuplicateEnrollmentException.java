package edu.ccrm.exceptions;

/**
 * Custom checked exception thrown when a student attempts to enroll 
 * in a course/semester they are already enrolled in.
 */
public class DuplicateEnrollmentException extends Exception {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}