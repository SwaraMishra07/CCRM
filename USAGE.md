---

# CCRM Usage Guide

## 1. Running the Application

### Compile & Run

```bash
javac -d bin src/edu/ccrm/cli/Main.java
java -cp bin edu.ccrm.cli.Main
```

### Menu Overview

Upon running, you will see the menu:

```
--- CCRM Menu ---
1. Add Student
2. List Students
3. Add Instructor
4. List Instructors
5. Add Course
6. List Courses
7. Enroll Student in Course
8. Record Grade
9. List Enrollments
10. Import Data from CSV
11. Export Data to CSV
12. Assign Instructor to Course
13. Show Student Transcript
14. Exit
```

Use **numbers 1–14** to navigate.

---

## 2. Sample Workflow

### Adding Students

1. Choose option `1`.
2. Enter the student name: `John Doe`
3. Email is generated automatically (`johndoe@mail.com`).

### Listing Students

* Option `2` prints all students with IDs, names, and emails.

### Adding Courses

1. Option `5`.
2. Enter course code: `CS101`
3. Enter course title: `Introduction to Programming`

### Enrolling Students

1. Option `7`.
2. Enter student ID and course index.
3. Choose semester: `1` for SPRING, `2` SUMMER, `3` FALL.

### Recording Grades

1. Option `8`.
2. Enter enrollment index.
3. Enter grade: `S, A, B, C, D, E, F`.

### Showing Transcripts

* Option `13`. Enter student ID.
* Displays enrolled courses, semesters, and grades.

---

## 3. Import/Export Data

### Import CSVs

* Option `10`. Provide paths for:

  * Students CSV
  * Courses CSV
  * Enrollments CSV

### Export CSVs

* Option `11`. Provide paths to save current data.

### Automatic Append on Exit

* Exiting via option `14` will append current session data to CSV files in `data/`.

---

## 4. Backup

* Use the **BackupService** (can be called from CLI or manually in Main)
* Backups are stored in `backup/YYYYMMDD_HHMMSS/` folder.
* Recursive utility can print total backup folder size.

---

## 5. Test Data

### Sample `students.csv`

```
id,fullName,email
1,John Doe,johndoe@mail.com
2,Jane Smith,janesmith@mail.com
...
```

### Sample `courses.csv`

```
code,title,credits
CS101,Introduction to Programming,3
CS102,Data Structures,3
...
```

### Sample `enrollments.csv`

```
studentId,courseCode,semester,grade
1,CS101,SPRING,S
2,CS102,FALL,A
...
```

---

## 6. Notes

* IDs start from `1`.
* Semester enum values: `SPRING, SUMMER, FALL`.
* Grade enum values: `S, A, B, C, D, E, F`.
* CLI validates input and prevents duplicates automatically.

---