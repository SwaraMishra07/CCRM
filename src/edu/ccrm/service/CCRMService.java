package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;

public class CCRMService {

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

    // ----------- STUDENTS -----------
    public boolean addStudent(String name) {
        if (name == null || name.isBlank()) return false;
        String email = name.toLowerCase().replace(" ", "") + "@mail.com";
        // Check duplicate by name/email
        for (Student s : students) if (s.getEmail().equals(email)) return false;
        students.add(new Student(students.size() + 1, name, email));
        return true;
    }

    public List<Student> listStudents() { return new ArrayList<>(students); }

    public Student getStudent(int sId) {
        if (sId <= 0 || sId > students.size()) return null;
        return students.get(sId - 1);
    }

    // ----------- INSTRUCTORS -----------
    public boolean addInstructor(String name, String dept) {
        if (name == null || name.isBlank() || dept == null || dept.isBlank()) return false;
        String email = name.toLowerCase().replace(" ", "") + "@mail.com";
        for (Instructor i : instructors) if (i.getEmail().equals(email)) return false;
        instructors.add(new Instructor(instructors.size() + 1, name, email, dept));
        return true;
    }

    public List<Instructor> listInstructors() { return new ArrayList<>(instructors); }

    public Instructor getInstructor(int iId) {
        if (iId <= 0 || iId > instructors.size()) return null;
        return instructors.get(iId - 1);
    }

    // ----------- COURSES -----------
    public boolean addCourse(String code, String title) {
        if (code == null || code.isBlank() || title == null || title.isBlank()) return false;
        for (Course c : courses) if (c.getCode().equalsIgnoreCase(code)) return false;
        courses.add(new Course(code, title, 3));
        return true;
    }

    public List<Course> listCourses() { return new ArrayList<>(courses); }

    public Course getCourse(int cIndex) {
        if (cIndex <= 0 || cIndex > courses.size()) return null;
        return courses.get(cIndex - 1);
    }

    // ----------- ENROLLMENTS -----------
    public boolean enrollStudent(int sId, int cIndex, Semester sem) {
        Student s = getStudent(sId);
        Course c = getCourse(cIndex);
        if (s == null || c == null || sem == null) return false;

        // Prevent duplicate enrollment
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(s) && e.getCourse().equals(c) && e.getSemester() == sem)
                return false;
        }

        Enrollment e = new Enrollment(s, c, sem);
        enrollments.add(e);
        s.enrollCourse(c);
        return true;
    }

    public boolean recordGrade(int eIndex, Grade grade) {
        if (eIndex <= 0 || eIndex > enrollments.size() || grade == null) return false;
        enrollments.get(eIndex - 1).setGrade(grade);
        return true;
    }

    public List<Enrollment> listEnrollments() { return new ArrayList<>(enrollments); }

    // ----------- INSTRUCTOR ASSIGNMENT -----------
    public boolean assignInstructorToCourse(int iId, int cIndex) {
        Instructor inst = getInstructor(iId);
        Course c = getCourse(cIndex);
        if (inst == null || c == null) return false;
        c.setInstructor(inst);
        return true;
    }

    // ----------- TRANSCRIPT -----------
    public List<Enrollment> getStudentTranscript(int sId) {
        Student s = getStudent(sId);
        if (s == null) return Collections.emptyList();

        List<Enrollment> transcript = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(s)) transcript.add(e);
        }
        return transcript;
    }
}
