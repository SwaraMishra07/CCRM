package edu.ccrm.domain;

public enum Grade {
    S(10),
    A(9),
    B(8),
    C(7),
    D(6),
    E(5),
    F(0); // fail

    private final int gradePoints;

    Grade(int points) {
        this.gradePoints = points;
    }

    public int getGradePoints() {
        return gradePoints;
    }
}
