// edu.ccrm.config.AppConfig.java
package edu.ccrm.config;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Student;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {
    
    // 1. Private static instance of the class (The Singleton)
    private static AppConfig instance; 

    // Configuration Fields (Now instance fields, not static)
    private final Path dataFolder;
    private final Path studentFile;
    private final Path courseFile;
    private final Path enrollFile;
    private final Path backupFolder; // Added for clarity with BackupUtil

    // Mandatory business rule property
    private final int maxCreditsPerSemester = 21; 

    private final List<Student> students = new ArrayList<>();
    private final List<Instructor> instructors = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();


    // 2. Private Constructor (Prevents direct instantiation)
    private AppConfig() {
        // Initialize paths
        this.dataFolder = Paths.get("data");
        this.studentFile = dataFolder.resolve("students.csv");
        this.courseFile = dataFolder.resolve("courses.csv");
        this.enrollFile = dataFolder.resolve("enrollments.csv");
        this.backupFolder = Paths.get("backups"); // Use a separate folder for backups

        // Ensure folders exist using NIO.2
        try {
            if (!Files.exists(this.dataFolder)) {
                Files.createDirectories(this.dataFolder);
                System.out.println("AppConfig: Created data folder.");
            }
            if (!Files.exists(this.backupFolder)) {
                Files.createDirectories(this.backupFolder);
                System.out.println("AppConfig: Created backup folder.");
            }
        } catch (IOException e) {
            System.err.println("AppConfig Initialization Error: Failed to create data folders: " + e.getMessage());
        }
    }

    // 3. Public static method to get the instance (The access point)
    public static AppConfig getInstance() {
        if (instance == null) {
            // Simple lazy initialization (thread safety can be added if needed, but simple is often fine)
            instance = new AppConfig();
        }
        return instance;
    }

    // Public Getters for all fields
    public Path getDataFolder() { return dataFolder; }
    public Path getStudentFile() { return studentFile; }
    public Path getCourseFile() { return courseFile; }
    public Path getEnrollFile() { return enrollFile; }
    public Path getBackupFolder() { return backupFolder; }
    public int getMaxCreditsPerSemester() { return maxCreditsPerSemester; }

    public List<Student> getStudents() { return students; }
    public List<Instructor> getInstructors() { return instructors; }
    public List<Course> getCourses() { return courses; }
    public List<Enrollment> getEnrollments() { return enrollments; }
}