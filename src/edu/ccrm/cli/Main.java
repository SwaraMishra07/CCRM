package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.CCRMService;
import edu.ccrm.util.BackupUtil;
import edu.ccrm.util.InputHelper;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static CCRMService service;
    static AppConfig config; // Instance of the Singleton

    public static void main(String[] args) {

        // MANDATORY: Initialize the AppConfig Singleton
        config = AppConfig.getInstance(); 

        // Initialize data lists (assuming AppConfig holds references to the lists)
        service = new CCRMService(
                config.getStudents(), 
                config.getInstructors(), 
                config.getCourses(), 
                config.getEnrollments()
        );

        // Ensure data folder exists (optional, as AppConfig constructor handles it)
        try { 
            if (!Files.exists(config.getDataFolder())) Files.createDirectories(config.getDataFolder()); 
        } catch (IOException e) { 
            System.err.println("Failed to ensure data folder exists: " + e.getMessage()); 
        }

        importDataOnStart();

        boolean running = true;
        while (running) {
            showMenu();
            // Expanded menu range (0-15)
            int choice = InputHelper.readInt(sc, "Enter choice: ", 0, 15);

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> listStudents();
                case 3 -> addInstructor();
                case 4 -> listInstructors();
                case 5 -> addCourse();
                case 6 -> listCourses();
                case 7 -> enrollStudent();
                case 8 -> recordGrade();
                case 9 -> listEnrollments();
                case 10 -> importDataManual(); // Import from custom path
                case 11 -> exportDataManual(); // Export to custom path
                case 12 -> assignInstructor();
                case 13 -> showTranscript();
                case 14 -> exportDataOnExit(false); // Quick save to default files
                case 15 -> runBackupAndCheckSize(); // Mandatory NIO.2 Recursion Demo
                case 0 -> {
                    running = false;
                    exportDataOnExit(true); // Auto-save before exit
                    System.out.println("\nExiting CCRM... Goodbye!");
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // -------------------- UI/Menu --------------------

    private static void showMenu() {
        System.out.println("\n--- CCRM Menu ---");
        System.out.println("1. Add Student");
        System.out.println("2. List Students");
        System.out.println("3. Add Instructor");
        System.out.println("4. List Instructors");
        System.out.println("5. Add Course");
        System.out.println("6. List Courses");
        System.out.println("7. Enroll Student in Course");
        System.out.println("8. Record Grade");
        System.out.println("9. List Enrollments");
        System.out.println("10. Import Data from CSV (Manual Path)");
        System.out.println("11. Export Data to CSV (Manual Path)");
        System.out.println("12. Assign Instructor to Course");
        System.out.println("13. Show Student Transcript (GPA)");
        System.out.println("14. Save Data to Default Files"); 
        System.out.println("15. Backup Data Folder & Show Recursive Size (NIO.2/Streams)");
        System.out.println("0. Exit (Auto-save and Exit)");
    }

    // -------------------- CASE 1: Students --------------------

    private static void addStudent() {
        System.out.print("Enter Student Full Name: ");
        String name = sc.nextLine();
        if (service.addStudent(name)) {
            System.out.println("✅ Student added successfully.");
        } else {
            System.err.println("🚫 Failed to add student. Name cannot be blank or student may already exist.");
        }
    }

    private static void listStudents() {
        List<Student> students = service.listStudents();
        if (students.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        System.out.println("\n--- Registered Students ---");
        students.forEach(s -> System.out.println(s.toString()));
    }

    // -------------------- CASE 3: Instructors --------------------

    private static void addInstructor() {
        System.out.print("Enter Instructor Full Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Instructor Department: ");
        String dept = sc.nextLine();
        if (service.addInstructor(name, dept)) {
            System.out.println("✅ Instructor added successfully.");
        } else {
            System.err.println("🚫 Failed to add instructor.");
        }
    }

    private static void listInstructors() {
        List<Instructor> instructors = service.listInstructors();
        if (instructors.isEmpty()) {
            System.out.println("No instructors registered.");
            return;
        }
        System.out.println("\n--- Registered Instructors ---");
        instructors.forEach(i -> System.out.println(i.toString()));
    }

    // -------------------- CASE 5: Courses --------------------

    private static void addCourse() {
        System.out.print("Enter Course Code (e.g., CS101): ");
        String code = sc.nextLine();
        System.out.print("Enter Course Title: ");
        String title = sc.nextLine();
        if (service.addCourse(code, title)) {
            System.out.println("✅ Course added successfully (default 3 credits).");
        } else {
            System.err.println("🚫 Failed to add course. Code may already exist.");
        }
    }

    private static void listCourses() {
        List<Course> courses = service.listCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        System.out.println("\n--- Available Courses (Index based) ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), courses.get(i).toString());
        }
    }

    // -------------------- CASE 7 & 8: Enrollments/Grades --------------------

    // MANDATORY: Exception Handling (Case 7)
    private static void enrollStudent() {
        listStudents();
        int sId = InputHelper.readInt(sc, "Enter student ID to enroll: ", 1, Integer.MAX_VALUE);
        
        listCourses();
        int cIndex = InputHelper.readInt(sc, "Enter course index to enroll: ", 1, service.listCourses().size());
        
        System.out.println("Choose semester: 1. SPRING 2. SUMMER 3. FALL");
        int semChoice = InputHelper.readInt(sc, "Enter semester choice: ", 1, 3);
        Semester sem = Semester.values()[semChoice - 1];

        try {
            service.enrollStudent(sId, cIndex, sem);
        } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException e) {
            System.err.println("🚫 Enrollment Failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("🚫 Enrollment Failed: Invalid input data. " + e.getMessage());
        }
    }

    // Case 8
    private static void recordGrade() {
        List<Enrollment> enrollments = service.listEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments to grade.");
            return;
        }
        
        System.out.println("\n--- Current Enrollments (Index based) ---");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            System.out.printf("%d. Student: %s | Course: %s | Grade: %s\n", 
                              (i + 1), e.getStudent().getFullName(), e.getCourse().getCode(), 
                              e.getGrade() != null ? e.getGrade().name() : "N/A");
        }

        int eIndex = InputHelper.readInt(sc, "Enter enrollment index to grade: ", 1, enrollments.size());
        
        System.out.println("Available Grades: S, A, B, C, D, E, F");
        System.out.print("Enter Grade: ");
        String gradeStr = sc.nextLine().toUpperCase();
        
        try {
            Grade grade = Grade.valueOf(gradeStr);
            if (service.recordGrade(eIndex, grade)) {
                System.out.println("✅ Grade recorded successfully.");
            } else {
                System.err.println("🚫 Failed to record grade.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("🚫 Invalid grade entered. Please use S, A, B, C, D, E, or F.");
        }
    }
    
    // Case 9
    private static void listEnrollments() {
        List<Enrollment> enrollments = service.listEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments recorded.");
            return;
        }
        System.out.println("\n--- All Enrollments ---");
        // Using Enrollment's improved toString for display
        enrollments.forEach(System.out::println); 
    }

    // -------------------- CASE 12: Assignment --------------------
    
    private static void assignInstructor() {
        listInstructors();
        int iId = InputHelper.readInt(sc, "Enter Instructor ID to assign: ", 1, Integer.MAX_VALUE);

        listCourses();
        int cIndex = InputHelper.readInt(sc, "Enter Course Index to assign: ", 1, service.listCourses().size());

        if (service.assignInstructorToCourse(iId, cIndex)) {
            System.out.println("✅ Instructor assigned successfully.");
        } else {
            System.err.println("🚫 Failed to assign instructor. Check IDs/Index.");
        }
    }

    // -------------------- CASE 13: Transcript/GPA --------------------

    // MANDATORY: Calls service method that demonstrates GPA, Polymorphism, and Streams
    private static void showTranscript() {
        listStudents();
        int sId = InputHelper.readInt(sc, "Enter student ID for transcript: ", 1, Integer.MAX_VALUE);
        
        service.printTranscript(sId);
    }

    // -------------------- DATA IMPORT/EXPORT (Cases 10, 11, 14, 0) --------------------

    private static void importDataOnStart() {
        try {
            // Accessing files via Singleton
            if (Files.exists(config.getStudentFile())) service.getStudents().addAll(ImportExportService.importStudents(config.getStudentFile()));
            if (Files.exists(config.getCourseFile())) service.getCourses().addAll(ImportExportService.importCourses(config.getCourseFile()));
            if (Files.exists(config.getEnrollFile())) service.getEnrollments().addAll(
                    ImportExportService.importEnrollments(config.getEnrollFile(), service.getStudents(), service.getCourses()));
            
            System.out.println("✅ Data imported from default files on start.");
        } catch (IOException e) { 
            System.err.println("Error importing data on start: " + e.getMessage()); 
        }
    }
    
    // Case 10: Import from manual path
    private static void importDataManual() {
         System.out.print("Enter path to import student CSV (e.g., manual/students.csv): ");
         String pathStr = sc.nextLine();
         Path path = Paths.get(pathStr);
         
         try {
             // Clear existing data before manual import
             service.getStudents().clear(); 
             service.getStudents().addAll(ImportExportService.importStudents(path));
             System.out.println("✅ Students imported successfully from " + pathStr);
         } catch (IOException e) {
             System.err.println("🚫 Error importing data: " + e.getMessage());
         }
         // Note: For a real app, you'd need logic to import ALL file types manually.
    }

    // Cases 14 & 0: Autosaves data on exit or manually saves to default files.
    private static void exportDataOnExit(boolean isExit) {
        try {
            ImportExportService.exportStudents(service.listStudents(), config.getStudentFile());
            ImportExportService.exportCourses(service.listCourses(), config.getCourseFile());
            ImportExportService.exportEnrollments(service.listEnrollments(), config.getEnrollFile());

            if (isExit) {
                System.out.println("✅ Current data state saved successfully before exit.");
            } else {
                System.out.println("✅ Data saved successfully to default files.");
            }
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    // Case 11: Export to user-specified path
    private static void exportDataManual() {
        System.out.print("Enter path to save student CSV: ");
        Path path = Paths.get(sc.nextLine());
        try {
            ImportExportService.exportStudents(service.listStudents(), path);
            System.out.println("✅ Students data exported successfully to " + path);
        } catch (IOException e) { 
            System.err.println("🚫 Error exporting data: " + e.getMessage()); 
        }
    }

    // -------------------- CASE 15: NIO.2 & RECURSION DEMO --------------------
    
    private static void runBackupAndCheckSize() {
        try {
            exportDataOnExit(false); // Ensure current state is saved before backup

            System.out.println("\nStarting data backup to timestamped folder...");
            
            // Run the backup using the data folder path from Singleton
            Path newBackupDir = BackupUtil.backupData(config.getDataFolder()); 

            if (newBackupDir != null) {
                System.out.println("✅ Backup successful! Created folder: " + newBackupDir.getFileName());

                // Mandatory recursive size check using BackupUtil's Stream implementation
                long totalSize = BackupUtil.calculateSizeRecursive(newBackupDir);
                
                double totalSizeKB = totalSize / 1024.0; 
                
                System.out.printf("📁 Total size of backup files (recursive utility demo): %.2f KB\n", totalSizeKB);
            }

        } catch (IOException e) {
            System.err.println("🛑 Backup failed due to IO error: " + e.getMessage());
        }
    }
}