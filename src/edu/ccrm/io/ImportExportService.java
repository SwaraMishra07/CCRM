package edu.ccrm.io;

import edu.ccrm.domain.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors; 

public class ImportExportService {

    // ---------------- Students ----------------
    public static List<Student> importStudents(Path path) throws IOException {
        try (var lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> {
                        try {
                            String[] parts = line.split(",");
                            int id = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            String email = parts[2].trim();
                            return new Student(id, name, email); 
                        } catch (Exception e) {
                            System.err.println("Skipping invalid student record: " + line);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) 
                    .toList(); 
        }
    }

    public static void exportStudents(List<Student> students, Path path) throws IOException {
        List<String> lines = students.stream()
                .map(s -> s.getId() + "," + s.getFullName() + "," + s.getEmail())
                .toList(); 
        
        Files.write(path, lines);
    }

    // ---------------- Courses (CRITICAL FIX APPLIED HERE) ----------------
    public static List<Course> importCourses(Path path) throws IOException {
        try (var lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> {
                        try {
                            String[] parts = line.split(",");
                            String code = parts[0].trim();
                            String title = parts[1].trim();
                            int credits = Integer.parseInt(parts[2].trim()); 
                            
                            // CRITICAL FIX: Use the Course.Builder to resolve constructor error
                            return new Course.Builder(code, title)
                                    .credits(credits)
                                    .build();
                            
                        } catch (Exception e) {
                             System.err.println("Skipping invalid course record: " + line);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList(); // Fixed incompatible types error by ensuring the Stream content is Course
        }
    }

    public static void exportCourses(List<Course> courses, Path path) throws IOException {
        List<String> lines = courses.stream()
                .map(c -> c.getCode() + "," + c.getTitle() + "," + c.getCredits())
                .toList();

        Files.write(path, lines);
    }

    // ---------------- Enrollments ----------------
    public static void exportEnrollments(List<Enrollment> enrollments, Path path) throws IOException {
        List<String> lines = enrollments.stream()
                .map(e -> {
                    String gradeStr = e.getGrade() != null ? e.getGrade().name() : "";
                    return String.join(",",
                            String.valueOf(e.getStudent().getId()),
                            e.getCourse().getCode(),
                            e.getSemester().name(),
                            gradeStr
                    );
                })
                .toList();

        Files.write(path, lines);
    }

    public static List<Enrollment> importEnrollments(Path path, List<Student> students, List<Course> courses) throws IOException {
        Map<Integer, Student> studentMap = students.stream().collect(
                Collectors.toMap(Student::getId, s -> s)); 
        
        Map<String, Course> courseMap = courses.stream().collect(
                Collectors.toMap(Course::getCode, c -> c)); 
        
        try (var lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> {
                        try {
                            String[] parts = line.split(",");
                            if (parts.length < 3) return null;
                            
                            int sId = Integer.parseInt(parts[0].trim());
                            String cCode = parts[1].trim();
                            Semester sem = Semester.valueOf(parts[2].trim());
                            Grade grade = parts.length > 3 && !parts[3].trim().isEmpty() ? Grade.valueOf(parts[3].trim()) : null;

                            Student s = studentMap.get(sId);
                            Course c = courseMap.get(cCode);

                            if (s != null && c != null) {
                                Enrollment e = new Enrollment(s, c, sem);
                                e.setGrade(grade);
                                return e; 
                            }
                            return null;
                        } catch (Exception e) {
                            System.err.println("Skipping invalid enrollment record: " + line + ". Error: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }
    }
}