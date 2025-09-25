package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.config.Config;
import edu.ccrm.service.CCRMService;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import edu.ccrm.util.BackupUtil;
import edu.ccrm.util.InputHelper;
public class Main {

    static Scanner sc = new Scanner(System.in);
    static CCRMService service;

    public static void main(String[] args) {

        // Initialize data lists
        service = new CCRMService(
                Config.students,
                Config.instructors,
                Config.courses,
                Config.enrollments
        );

        try { if (!Files.exists(Config.DATA_FOLDER)) Files.createDirectories(Config.DATA_FOLDER); }
        catch (IOException e) { System.out.println("Failed to create data folder: " + e.getMessage()); }

        importDataOnStart();

        boolean running = true;
        while (running) {
            showMenu();
            int choice = InputHelper.readInt(sc, "Enter choice: ", 1, 14);

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
                case 10 -> importData();
                case 11 -> exportData();
                case 12 -> assignInstructor();
                case 13 -> showTranscript();
                case 14 -> {
                    running = false;
                    exportDataOnExit();
                    System.out.println("Exiting CCRM...");
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

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
        System.out.println("10. Import Data from CSV");
        System.out.println("11. Export Data to CSV");
        System.out.println("12. Assign Instructor to Course");
        System.out.println("13. Show Student Transcript");
        System.out.println("14. Exit");
        System.out.print("Enter choice: ");
    }

    // -------------------- MENU METHODS --------------------
    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();
        if (service.addStudent(name)) System.out.println("Student added successfully!");
        else System.out.println("Failed to add student (duplicate or invalid name).");
    }

    private static void listStudents() {
        List<Student> students = service.listStudents();
        if (students.isEmpty()) { System.out.println("No students found."); return; }
        System.out.println("\n--- Students ---");
        for (Student s : students) System.out.println(s.getId() + ": " + s.getFullName() + " (" + s.getEmail() + ")");
    }

    private static void addInstructor() {
        System.out.print("Enter instructor name: ");
        String name = sc.nextLine();
        System.out.print("Enter department: ");
        String dept = sc.nextLine();
        if (service.addInstructor(name, dept)) System.out.println("Instructor added successfully!");
        else System.out.println("Failed to add instructor (duplicate or invalid input).");
    }

    private static void listInstructors() {
        List<Instructor> instructors = service.listInstructors();
        if (instructors.isEmpty()) { System.out.println("No instructors found."); return; }
        System.out.println("\n--- Instructors ---");
        for (Instructor i : instructors) System.out.println(i);
    }

    private static void addCourse() {
        System.out.print("Enter course code: ");
        String code = sc.nextLine();
        System.out.print("Enter course title: ");
        String title = sc.nextLine();
        if (service.addCourse(code, title)) System.out.println("Course added successfully!");
        else System.out.println("Failed to add course (duplicate or invalid input).");
    }

    private static void listCourses() {
        List<Course> courses = service.listCourses();
        if (courses.isEmpty()) { System.out.println("No courses found."); return; }
        System.out.println("\n--- Courses ---");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.println((i + 1) + ". " + c.getCode() + " - " + c.getTitle() +
                    " (" + c.getCredits() + " credits)" +
                    (c.getInstructor() != null ? " | Instructor: " + c.getInstructor().getFullName() : ""));
        }
    }

    private static void enrollStudent() {
        listStudents(); listCourses();
        System.out.print("Enter student ID: ");
        int sId = Integer.parseInt(sc.nextLine());
        System.out.print("Enter course index: ");
        int cIndex = Integer.parseInt(sc.nextLine());
        System.out.println("Choose semester: 1. SPRING 2. SUMMER 3. FALL");
        int semChoice = Integer.parseInt(sc.nextLine());
        if (service.enrollStudent(sId, cIndex, Semester.values()[semChoice - 1]))
            System.out.println("Enrollment successful!");
        else System.out.println("Failed to enroll student (invalid input or duplicate).");
    }

    private static void recordGrade() {
        listEnrollments();
        System.out.print("Enter enrollment index: ");
        int eIndex = Integer.parseInt(sc.nextLine());
        System.out.print("Enter grade (S,A,B,C,D,E,F): ");
        Grade grade = Grade.valueOf(sc.nextLine().toUpperCase());
        if (service.recordGrade(eIndex, grade)) System.out.println("Grade recorded!");
        else System.out.println("Failed to record grade.");
    }

    private static void listEnrollments() {
        List<Enrollment> enrollments = service.listEnrollments();
        if (enrollments.isEmpty()) { System.out.println("No enrollments found."); return; }
        System.out.println("\n--- Enrollments ---");
        for (int i = 0; i < enrollments.size(); i++) {
            System.out.println((i + 1) + ". " + enrollments.get(i));
        }
    }

    private static void assignInstructor() {
        listInstructors(); listCourses();
        System.out.print("Enter instructor ID: ");
        int iId = Integer.parseInt(sc.nextLine());
        System.out.print("Enter course index: ");
        int cIndex = Integer.parseInt(sc.nextLine());
        if (service.assignInstructorToCourse(iId, cIndex))
            System.out.println("Instructor assigned successfully!");
        else System.out.println("Failed to assign instructor.");
    }

    private static void showTranscript() {
        listStudents();
        System.out.print("Enter student ID: ");
        int sId = Integer.parseInt(sc.nextLine());
        List<Enrollment> transcript = service.getStudentTranscript(sId);
        if (transcript.isEmpty()) { System.out.println("No enrollments found for this student."); return; }
        System.out.println("\n--- Transcript ---");
        for (Enrollment e : transcript) {
            System.out.println(e.getCourse().getCode() + " - " + e.getCourse().getTitle() +
                    " | Semester: " + e.getSemester() +
                    " | Grade: " + (e.getGrade() != null ? e.getGrade() : "N/A"));
        }
    }

    // -------------------- DATA IMPORT/EXPORT --------------------
    private static void importDataOnStart() {
        try {
            if (Files.exists(Config.STUDENT_FILE)) service.listStudents().addAll(ImportExportService.importStudents(Config.STUDENT_FILE));
            if (Files.exists(Config.COURSE_FILE)) service.listCourses().addAll(ImportExportService.importCourses(Config.COURSE_FILE));
            if (Files.exists(Config.ENROLL_FILE)) service.listEnrollments().addAll(
                    ImportExportService.importEnrollments(Config.ENROLL_FILE, service.listStudents(), service.listCourses()));
        } catch (IOException e) { System.out.println("Error importing data on start: " + e.getMessage()); }
    }

    private static void exportDataOnExit() {
    try {
        // Save CSVs
        if (!Files.exists(Config.STUDENT_FILE)) {
            ImportExportService.exportStudents(service.listStudents(), Config.STUDENT_FILE);
        } else {
            ImportExportService.exportStudents(service.listStudents(), Config.STUDENT_FILE, true);
        }

        if (!Files.exists(Config.COURSE_FILE)) {
            ImportExportService.exportCourses(service.listCourses(), Config.COURSE_FILE);
        } else {
            ImportExportService.exportCourses(service.listCourses(), Config.COURSE_FILE, true);
        }

        if (!Files.exists(Config.ENROLL_FILE)) {
            ImportExportService.exportEnrollments(service.listEnrollments(), Config.ENROLL_FILE);
        } else {
            ImportExportService.exportEnrollments(service.listEnrollments(), Config.ENROLL_FILE, true);
        }

        // Backup entire data folder recursively
        BackupUtil.backupData(Config.DATA_FOLDER);

        System.out.println("Data saved successfully!");
    } catch (IOException e) {
        System.out.println("Error saving data: " + e.getMessage());
    }
}

    private static void importData() {
        try {
            System.out.print("Enter path to student CSV: ");
            service.listStudents().addAll(ImportExportService.importStudents(Paths.get(sc.nextLine())));
            System.out.print("Enter path to course CSV: ");
            service.listCourses().addAll(ImportExportService.importCourses(Paths.get(sc.nextLine())));
            System.out.print("Enter path to enrollment CSV: ");
            service.listEnrollments().addAll(
                    ImportExportService.importEnrollments(Paths.get(sc.nextLine()), service.listStudents(), service.listCourses()));
            System.out.println("Data imported successfully!");
        } catch (IOException e) { System.out.println("Error importing data: " + e.getMessage()); }
    }

    private static void exportData() {
        try {
            System.out.print("Enter path to save student CSV: ");
            ImportExportService.exportStudents(service.listStudents(), Paths.get(sc.nextLine()));
            System.out.print("Enter path to save course CSV: ");
            ImportExportService.exportCourses(service.listCourses(), Paths.get(sc.nextLine()));
            System.out.print("Enter path to save enrollment CSV: ");
            ImportExportService.exportEnrollments(service.listEnrollments(), Paths.get(sc.nextLine()));
            System.out.println("Data exported successfully!");
        } catch (IOException e) { System.out.println("Error exporting data: " + e.getMessage()); }
    }
}
