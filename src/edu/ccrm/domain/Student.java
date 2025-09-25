package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.Collections; // For unmodifiable list
import java.util.List;
import java.util.Objects; // For generating equals/hashCode

public class Student extends Person {
    
    // Encapsulation: Internal list is private
    private final List<Course> enrolledCourses = new ArrayList<>(); 

    public Student(int id, String fullName, String email) {
        // Inheritance: Calls the Person base class constructor
        super(id, fullName, email);
    }

    // --- Mandatory Polymorphism/Abstraction Implementation ---
    
    @Override
    public String getRole() {
        return "Student";
    }

    // --- Getters (Strong Encapsulation) ---
    
    /**
     * FIX: Returns an unmodifiable view of the enrolled courses list.
     * This protects the internal state from unauthorized external modification.
     */
    public List<Course> getEnrolledCourses() {
        return Collections.unmodifiableList(enrolledCourses);
    }

    // --- Mutator ---

    public void enrollCourse(Course course) {
        // Basic check for business logic protection
        if (course != null && !enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }
    
    // --- Mandatory Overrides for Collections/Uniqueness ---
    
    /**
     * Student equality is determined by their unique ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // Uses the inherited getId() to check equality
        return this.getId() == ((Student) o).getId(); 
    }

    @Override
    public int hashCode() {
        // Uses the inherited getId() as the unique hash value
        return Objects.hash(this.getId()); 
    }

    @Override
    public String toString() {
        // Uses inherited getters for Person data and provides a clear description
        return String.format("ID: %d | Name: %s | Email: %s | Role: %s | Active: %b",
                             this.getId(), 
                             this.getFullName(), 
                             this.getEmail(), 
                             this.getRole(),
                             this.isActive());
    }
}