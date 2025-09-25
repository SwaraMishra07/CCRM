package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.config.AppConfig;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CCRMService {

    // Fields hold references to the lists passed from AppConfig
    private final List<Student> students;
    private final List<Instructor> instructors;
    private final List<Course> courses;
    private final List<Enrollment> enrollments;

    public CCRMService(List<Student> students, List<Instructor> instructors,
                       List<Course> courses, List<Enrollment> enrollments) {
        this.students = students;
        this.instructors = instructors;
        this.courses = courses;
        this.enrollments = enrollments;
    }

    // --- FIX: Accessor methods for mutable lists (Used by Main.java for import/export) ---
    public List<Student> getStudents() { return students; }
    public List<Instructor> getInstructors() { return instructors; }
    public List<Course> getCourses() { return courses; }
    public List<Enrollment> getEnrollments() { return enrollments; }


    // --- Data Access & CRUD Operations ---
    
    public List<Student> listStudents() { 
        return students.stream().toList(); // Stream API
    }
    
    public Student getStudent(int sId) {
        return students.stream()
                .filter(s -> s.getId() == sId)
                .findFirst()
                .orElse(null);
    }
    
    public boolean addStudent(String name) {
        if (name == null || name.isBlank()) return false;
        String email = name.toLowerCase().replace(" ", "") + "@mail.com";
        
        boolean exists = students.stream().anyMatch(s -> s.getEmail().equals(email));
        if (exists) return false;
        
        int nextId = students.stream().mapToInt(Person::getId).max().orElse(0) + 1;
        students.add(new Student(nextId, name, email));
        return true;
    }

    // --- Course Operations (FIXED: Uses Course Builder) ---
    public List<Course> listCourses() { 
        return courses.stream().toList(); 
    }

    public Course getCourse(int cIndex) {
        if (cIndex <= 0 || cIndex > courses.size()) return null;
        return courses.get(cIndex - 1);
    }
    
    public boolean addCourse(String code, String title) {
        if (code == null || code.isBlank() || title == null || title.isBlank()) return false;
        
        boolean exists = courses.stream().anyMatch(c -> c.getCode().equalsIgnoreCase(code));
        if (exists) return false;
        
        // FIX: Use the Course.Builder pattern now that it's implemented
        courses.add(new Course.Builder(code, title).credits(3).build());
        return true;
    }
    
    // --- Instructor Operations (ADDED: addInstructor method) ---
    
    public List<Instructor> listInstructors() { return instructors.stream().toList(); }
    
    // FIX: Added the missing addInstructor method
    public boolean addInstructor(String name, String dept) {
        if (name == null || name.isBlank() || dept == null || dept.isBlank()) return false;
        String email = name.toLowerCase().replace(" ", "") + "@mail.com";
        
        boolean exists = instructors.stream().anyMatch(i -> i.getEmail().equals(email));
        if (exists) return false;
        
        int nextId = instructors.stream().mapToInt(Person::getId).max().orElse(0) + 1;
        instructors.add(new Instructor(nextId, name, email, dept));
        return true;
    }
    
    public boolean assignInstructorToCourse(int iId, int cIndex) {
        Instructor inst = instructors.stream().filter(i -> i.getId() == iId).findFirst().orElse(null);
        Course c = getCourse(cIndex);
        if (inst == null || c == null) return false;
        c.setInstructor(inst);
        return true;
    }


    // --- Mandatory Enrollment Logic (Exceptions & Singleton) ---
    
    // Helper method using Stream API to aggregate current credits
    private int getCreditsForSemester(Student s, Semester sem) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(s) && e.getSemester() == sem)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum(); // Stream aggregation/reduction
    }

    public void enrollStudent(int sId, int cIndex, Semester sem) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
            
        Student s = getStudent(sId);
        Course c = getCourse(cIndex);
        if (s == null || c == null || sem == null) {
            throw new IllegalArgumentException("Invalid Student ID, Course Index, or Semester provided.");
        }

        // 1. Check for Duplicate Enrollment (Custom Checked Exception)
        boolean alreadyEnrolled = enrollments.stream()
                .anyMatch(e -> e.getStudent().equals(s) && e.getCourse().equals(c) && e.getSemester() == sem);
        
        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException(
                String.format("Student %s is already enrolled in %s for %s.", s.getFullName(), c.getCode(), sem));
        }

        // 2. Check Max Credit Limit (Custom Checked Exception & Singleton Access)
        int currentCredits = getCreditsForSemester(s, sem);
        int maxCredits = AppConfig.getInstance().getMaxCreditsPerSemester(); // Singleton access
        
        if (currentCredits + c.getCredits() > maxCredits) {
            throw new MaxCreditLimitExceededException(
                String.format("Enrollment failed. %s is at %d credits, exceeding the limit of %d.", 
                              s.getFullName(), currentCredits, maxCredits));
        }

        // If all checks pass
        Enrollment e = new Enrollment(s, c, sem);
        enrollments.add(e);
        s.enrollCourse(c); // Update the Student domain object list
        System.out.printf("Enrollment successful: %s in %s (%s).\n", s.getFullName(), c.getCode(), sem);
    }
    
    public boolean recordGrade(int eIndex, Grade grade) {
        if (eIndex <= 0 || eIndex > enrollments.size() || grade == null) return false;
        Enrollment e = enrollments.get(eIndex - 1);
        e.setGrade(grade);
        
        // Assertion Demo
        assert e.getGrade() == grade : "Grade recording failed invariant check!"; 
        
        return true;
    }

    public List<Enrollment> listEnrollments() { 
        return enrollments.stream().toList(); 
    }


    // --- Mandatory GPA Computation & Transcript ---

    public List<Enrollment> getStudentTranscript(int sId) {
        Student s = getStudent(sId);
        if (s == null) return Collections.emptyList();

        return enrollments.stream()
                .filter(e -> e.getStudent().equals(s))
                .sorted(Comparator.comparing(Enrollment::getSemester).thenComparing(e -> e.getCourse().getCode()))
                .toList();
    }
    
    /**
     * Computes the GPA for a student (Mandatory Stream Aggregation).
     */
    public double computeGPA(Student s) {
        List<Enrollment> completed = getStudentTranscript(s.getId()).stream()
                .filter(e -> e.getGrade() != null)
                .toList();

        if (completed.isEmpty()) return 0.0;

        // Stream API aggregation: total grade points * credits
        double totalGradePoints = completed.stream()
                .mapToDouble(e -> e.getGrade().getPointValue() * e.getCourse().getCredits())
                .sum(); 

        // Stream API aggregation: total credits
        int totalCredits = completed.stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum(); 

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    /**
     * Prints the student transcript (FIXED: Uses getRole() for Polymorphism).
     */
    public void printTranscript(int sId) {
        Student s = getStudent(sId);
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("\n--- Official Transcript ---");
        // FIX: Using getRole() or getFullName() + getRole() as getProfileDetails() 
        // was not consistently defined or implemented.
        System.out.println("Name: " + s.getFullName() + " | Role: " + s.getRole()); 
        System.out.println("---------------------------------");

        getStudentTranscript(sId).forEach(e -> {
            // Polymorphism: using Enrollment's toString() override
            System.out.println(e.toString()); 
        });

        System.out.println("---------------------------------");
        System.out.printf("CUMULATIVE GPA: %.2f\n", computeGPA(s));
    }
    
    // --- Mandatory Stream API Search/Filter ---

    /**
     * Demonstrates Stream API filtering using a Predicate lambda.
     */
    public List<Course> searchCourses(Predicate<Course> predicate) {
        return courses.stream()
                .filter(predicate)
                .toList(); 
    }
    
    public List<Course> searchCoursesByCode(String codePrefix) {
        return searchCourses(c -> c.getCode().startsWith(codePrefix.toUpperCase()));
    }
}