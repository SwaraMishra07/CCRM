package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private List<Course> enrolledCourses = new ArrayList<>();

    public Student(int id, String fullName, String email) {
        super(id, fullName, email);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
    }
}
