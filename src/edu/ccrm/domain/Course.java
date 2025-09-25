package edu.ccrm.domain;

public class Course {
    private String code;
    private String title;
    private int credits;
    private Instructor instructor; // added instructor

    public Course(String code, String title, int credits) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.instructor = null; // default
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }

    public Instructor getInstructor() { return instructor; } // getter
    public void setInstructor(Instructor instructor) { this.instructor = instructor; } // setter

    @Override
    public String toString() {
        return code + " - " + title + " (" + credits + " credits)" +
               (instructor != null ? " | Instructor: " + instructor.getFullName() : "");
    }
}
