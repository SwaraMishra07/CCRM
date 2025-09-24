package edu.ccrm.domain;

import java.time.LocalDate;

public class Enrollment {
    private Student student;
    private Course course;
    private Semester semester;
    private LocalDate enrollmentDate;
    private Grade grade;

    public Enrollment(Student student, Course course, Semester semester) {
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.enrollmentDate = LocalDate.now();
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Grade getGrade() {
        return grade;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Semester getSemester() {
        return semester;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    @Override
    public String toString() {
        return student.getFullName() + " enrolled in " + course.getCode() +
               " (" + course.getTitle() + ") - Semester: " + semester +
               " - Grade: " + (grade != null ? grade : "N/A");
    }
}
