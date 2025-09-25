// edu.ccrm.domain.Grade.java (Refined Naming)
package edu.ccrm.domain;

public enum Grade {
    S(10),
    A(9),
    B(8),
    C(7),
    D(6),
    E(5),
    F(0); // fail

    // Use 'pointValue' or 'gpaPoints' for clarity in service calculations
    private final int pointValue; 

    // Mandatory: Constructor in enum
    Grade(int pointValue) {
        this.pointValue = pointValue;
    }

    // Getter for the value
    public int getPointValue() { 
        return pointValue;
    }
}