package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects; // Mandatory import for hashCode/equals

public class Enrollment {
    // Fields passed in the constructor should be final for immutability principles
    private final Student student;
    private final Course course;
    private final Semester semester;
    private final LocalDate enrollmentDate; // Automatically generated field made final

    // Grade is mutable (can be set later)
    private Grade grade; 

    public Enrollment(Student student, Course course, Semester semester) {
        // Essential check to ensure data integrity
        if (student == null || course == null || semester == null) {
            throw new IllegalArgumentException("Enrollment must have a valid student, course, and semester.");
        }
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.enrollmentDate = LocalDate.now(); // Java Date/Time API
    }

    // --- Getters (Encapsulation) ---
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public Semester getSemester() { return semester; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public Grade getGrade() { return grade; }

    // --- Setters ---
    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    // --- Mandatory Overrides for Equality (Best Practice) ---
    
    /**
     * Enrollment equality is determined by the unique combination of 
     * Student, Course, and Semester.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        
        // Logical equality check based on the unique key
        return Objects.equals(student, that.student) &&
               Objects.equals(course, that.course) &&
               semester == that.semester;
    }

    @Override
    public int hashCode() {
        // Generates a hash based on the unique key fields
        return Objects.hash(student, course, semester);
    }
    
    // --- Mandatory toString() Override (Transcript/Reporting) ---

    @Override
    public String toString() {
        // Detailed toString() for transcript/listing view
        String gradeDisplay = (grade != null ? grade.name() + " (" + grade.getPointValue() + " points)" : "N/A");
        
        return String.format("%s - %s | %s | Enrolled: %s | Grade: %s",
                             course.getCode(), 
                             course.getTitle(),
                             semester.name(),
                             enrollmentDate,
                             gradeDisplay);
    }
}