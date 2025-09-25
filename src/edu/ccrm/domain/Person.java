package edu.ccrm.domain;

import java.time.LocalDate;

public abstract class Person {
    
    // --- Strong Encapsulation: Fields are private ---
    
    // Mandatory Immutability: Fields set once in constructor
    private final int id;
    private final String fullName;
    private final String email;
    private final LocalDate dateCreated; // Java Date/Time API

    // Mutable field (only one with a setter/mutator method)
    private boolean active; 

    public Person(int id, String fullName, String email) {
        // Validation check (Good practice)
        if (id <= 0 || fullName == null || fullName.isBlank() || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Person must be initialized with valid ID, name, and email.");
        }
        
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.active = true;
        this.dateCreated = LocalDate.now();
    }

    // --- Mandatory Abstraction: Abstract Method for Polymorphism ---
    
    /**
     * Defines the abstract method that all concrete subclasses (Student, Instructor) 
     * must implement to demonstrate polymorphism.
     */
    public abstract String getRole();

    // --- Getters (Full Encapsulation) ---
    // Note: Subclasses must use these public getters instead of direct field access.
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDate getDateCreated() { return dateCreated; }
    public boolean isActive() { return active; }

    // --- Mutator Method (Setter for the only mutable field) ---
    
    /**
     * Mutator method to change the state of the 'active' field.
     */
    public void deactivate() { 
        this.active = false; 
    }
}