package edu.ccrm.io;

import edu.ccrm.domain.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ImportExportService {

    // ---------------- Students ----------------
    public static List<Student> importStudents(Path path) throws IOException {
        List<Student> students = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            students.add(new Student(id, name, email));
        }
        return students;
    }

    public static void exportStudents(List<Student> students, Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Student s : students) {
            lines.add(s.getId() + "," + s.getFullName() + "," + s.getEmail());
        }
        Files.write(path, lines);
    }

    // ---------------- Courses ----------------
    public static List<Course> importCourses(Path path) throws IOException {
        List<Course> courses = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            String code = parts[0].trim();
            String title = parts[1].trim();
            int credits = Integer.parseInt(parts[2].trim());
            courses.add(new Course(code, title, credits));
        }
        return courses;
    }

    public static void exportCourses(List<Course> courses, Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Course c : courses) {
            lines.add(c.getCode() + "," + c.getTitle() + "," + c.getCredits());
        }
        Files.write(path, lines);
    }

    // ---------------- Enrollments ----------------
    public static void exportEnrollments(List<Enrollment> enrollments, Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Enrollment e : enrollments) {
            String gradeStr = e.getGrade() != null ? e.getGrade().name() : "";
            lines.add(e.getStudent().getId() + "," + e.getCourse().getCode() + "," +
                      e.getSemester() + "," + gradeStr);
        }
        Files.write(path, lines);
    }

    public static List<Enrollment> importEnrollments(Path path, List<Student> students, List<Course> courses) throws IOException {
        List<Enrollment> enrollments = new ArrayList<>();
        Map<Integer, Student> studentMap = new HashMap<>();
        Map<String, Course> courseMap = new HashMap<>();

        for (Student s : students) studentMap.put(s.getId(), s);
        for (Course c : courses) courseMap.put(c.getCode(), c);

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            // CSV format: studentId,courseCode,semester,grade
            String[] parts = line.split(",");
            int sId = Integer.parseInt(parts[0].trim());
            String cCode = parts[1].trim();
            Semester sem = Semester.valueOf(parts[2].trim());
            Grade grade = parts.length > 3 && !parts[3].trim().isEmpty() ? Grade.valueOf(parts[3].trim()) : null;

            Student s = studentMap.get(sId);
            Course c = courseMap.get(cCode);
            if (s != null && c != null) {
                Enrollment e = new Enrollment(s, c, sem);
                e.setGrade(grade);
                enrollments.add(e);
            }
        }
        return enrollments;
    }
}
