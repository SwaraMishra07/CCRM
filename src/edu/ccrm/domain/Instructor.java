package edu.ccrm.domain;

// Instructor extends the Person base class, demonstrating Inheritance
public class Instructor extends Person {
    
    private String department; // Encapsulation: private field

    // Constructor chains to the superclass constructor
    public Instructor(int id, String fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
    }

    // --- Mandatory Polymorphism/Abstraction Implementation ---
    
    /**
     * Implementation of the abstract method defined in the abstract Person class.
     * This demonstrates Polymorphism based on the Person hierarchy.
     */
    @Override
    public String getRole() {
        return "Instructor";
    }

    // --- Getters and Setters (Encapsulation) ---
    
    public String getDepartment() { // Corrected the typo (was getDepartmsent)
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // --- Mandatory toString() Override ---

    /**
     * Overriding toString() and using inherited getters (getId(), getFullName(), getEmail()) 
     * to respect Encapsulation principles, as the fields in Person are private.
     */
    @Override
    public String toString() {
        // Uses inherited getters for Person data
        return String.format("ID: %d | Name: %s | Email: %s | Role: %s | Dept: %s",
                             this.getId(), 
                             this.getFullName(), 
                             this.getEmail(), 
                             this.getRole(),
                             this.department);
    }
}