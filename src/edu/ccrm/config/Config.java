package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public static final Path DATA_FOLDER = Paths.get("data");
    public static final Path STUDENT_FILE = DATA_FOLDER.resolve("students.csv");
    public static final Path COURSE_FILE = DATA_FOLDER.resolve("courses.csv");
    public static final Path ENROLL_FILE = DATA_FOLDER.resolve("enrollments.csv");
}
