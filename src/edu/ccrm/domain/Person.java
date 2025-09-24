package edu.ccrm.domain;

import java.time.LocalDate;

public abstract class Person {
    protected int id;
    protected String fullName;
    protected String email;
    protected boolean active;
    protected LocalDate dateCreated;

    public Person(int id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.active = true;
        this.dateCreated = LocalDate.now();
    }

    public abstract String getRole();

    // getters and setters
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
}
