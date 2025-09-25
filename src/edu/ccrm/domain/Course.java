package edu.ccrm.domain;

// Import the assertion utility if you plan to use assertions in the Builder
// import static edu.ccrm.util.Validator.validateCredits; // Example utility import

public class Course {
    
    // Mandatory Immutability Principle: Core identity fields are FINAL
    private final String code;
    private final String title;
    private final int credits;
    
    // Mutable fields (can be changed after creation, e.g., assignment)
    private Instructor instructor;
    private String department; // Added department field from functional requirements
    private Semester semester;   // Added semester field from functional requirements

    /**
     * Private constructor used only by the static Builder.
     * @param builder The Course.Builder instance.
     */
    private Course(Builder builder) {
        // Encapsulation and Final Assignment
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.department = builder.department;
        this.semester = builder.semester;
        
        // Assertion Example (Must be enabled via -ea flag at runtime)
        assert this.credits >= 1 && this.credits <= 6 : "Assertion Failed: Course credits are out of bounds (1-6).";
    }

    // --- Mandatory Static Nested Class: Builder Design Pattern ---
    
    public static class Builder {
        // Mandatory Fields (copied from Course)
        private final String code;
        private final String title;
        
        // Optional Fields (set via chaining methods)
        private int credits = 3; // Default value
        private Instructor instructor = null;
        private String department = "N/A";
        private Semester semester = null;
        
        /**
         * Constructor for mandatory fields.
         */
        public Builder(String code, String title) {
            this.code = code;
            this.title = title;
        }

        // Chaining methods for optional fields
        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder instructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }
        
        public Builder department(String department) {
            this.department = department;
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        /**
         * Builds and returns the immutable Course object.
         */
        public Course build() {
            // Optional: Run pre-build validation here
            if (this.credits < 1 || this.credits > 6) {
                 throw new IllegalArgumentException("Credits must be between 1 and 6.");
            }
            return new Course(this);
        }
    }

    // --- Getters (Encapsulation) ---
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public String getDepartment() { return department; }
    public Semester getSemester() { return semester; }


    // --- Setters (Only for mutable fields) ---
    public void setInstructor(Instructor instructor) { 
        this.instructor = instructor; 
    }
    public void setDepartment(String department) { 
        this.department = department; 
    }
    public void setSemester(Semester semester) { 
        this.semester = semester; 
    }


    // --- Mandatory toString() Override ---
    @Override
    public String toString() {
        // Overriding method demonstration
        String instructorName = (instructor != null ? instructor.getFullName() : "Unassigned");
        String semesterInfo = (semester != null ? ", Semester: " + semester.name() : "");
        
        return String.format("%s - %s | Dept: %s | Credits: %d | Instructor: %s%s",
                             code, title, department, credits, instructorName, semesterInfo);
    }
    
    // Mandatory: Overriding hashCode() and equals() is good practice for domain objects
    // ...
}