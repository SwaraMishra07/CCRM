# Campus Course & Records Manager (CCRM)

## 1. Project Overview

**CCRM** is a console-based Java application that allows institutes to manage:

* **Students**: Add, update, deactivate, enroll/unenroll in courses.
* **Courses**: Add, update, list, search, assign instructors.
* **Grades & Transcripts**: Record marks, compute letter grades and GPA, print transcripts.
* **File Utilities**: Import/export CSV files, backup and archive course data.

The project demonstrates **OOP concepts, Java NIO.2, Streams, Date/Time API**, and various **design patterns** (Singleton, Builder).

---

## 2. Project Setup

### Prerequisites

* Java SE 17+ installed
* IDE (Eclipse/IntelliJ) or terminal access

### Folder Structure

```
edu.ccrm
├─ cli/        // Main menu, input loop
├─ domain/     // Person (abstract), Student, Instructor, Course, Enrollment, Enums
├─ service/    // CCRMService: Student/Course/Enrollment management
├─ io/         // ImportExportService (CSV), BackupService
├─ util/       // InputHelper, Backup utilities, Recursion utilities
└─ config/     // Config (Paths), AppConfig (Singleton)
```

### Running the Program

1. Clone the repository:

```bash
git clone <repo-link>
cd ccrm
```

2. Compile and run:

```bash
javac -d bin src/edu/ccrm/cli/Main.java
java -cp bin edu.ccrm.cli.Main
```

---

## 3. Functional Overview

### Student Management

* Add, list, update, deactivate students
* Track enrolled courses and student details
* Print student profile & transcript

### Course Management

* Add, list, update, deactivate courses
* Assign instructors and semesters
* Search/filter courses using Stream API

### Enrollment & Grading

* Enroll/unenroll students
* Record grades, compute GPA
* Use enums for **Semester** and **Grade**
* Prevent duplicate enrollments

### File Operations

* Import/export CSV data
* Backup to timestamped folder
* Recursive utilities for folder size computation

### CLI Workflow

* Menu-driven console
* All operations available via numbered menu
* Validations and exception handling included

---

## 4. Technical Features

### Java Core

* Primitive types, operators, precedence
* Decision structures: `if`, `if-else`, nested `if`, `switch`
* Loops: `for`, `while`, `do-while`, enhanced for
* Arrays & Array utilities
* Strings & common methods (`split`, `join`, `equals`, `substring`)

### OOP Concepts

* **Encapsulation**: Private fields, getters/setters
* **Inheritance**: Abstract `Person` → `Student` / `Instructor`
* **Abstraction**: Abstract classes & methods
* **Polymorphism**: Overriding, toString() usage, interface implementations

### Advanced Concepts

* Immutability: Immutable value classes
* Nested classes: Inner and static nested classes
* Interfaces & lambdas: `Persistable`, comparators, predicates
* Anonymous inner classes for callbacks/strategy
* Design Patterns: Singleton (`AppConfig`), Builder (`Course.Builder`)
* Checked & unchecked exceptions, custom exceptions
* Assertions for invariants

### Java NIO.2 & Streams

* Path & Files API for reading/writing CSVs
* Stream pipelines for filtering/sorting/reporting
* Date/Time API for timestamps (enrollment date, backups)

---

## 5. Demonstration Flow

1. On start, **AppConfig (Singleton)** loads config.
2. Console menu:

   * Manage Students / Courses / Enrollments / Grades
   * Import/Export Data
   * Backup & Show Backup Size
   * Reports (Top students, GPA distribution)
   * Exit
3. Perform enrollments, record grades, print transcripts
4. Export data and backup to timestamped folder

---

## 6. Screenshots

<img width="500" height="150" alt="Screenshot 2025-09-25 185502" src="https://github.com/user-attachments/assets/6510aec5-3e93-4966-b472-297fb0152c7f" />


---

## 7. About Java

### 📌 Evolution of Java (Short Timeline)

1991 → Project "Oak" started by James Gosling at Sun Microsystems.

1995 → Renamed to Java, officially released.

1996 → First JDK (Java Development Kit 1.0).

1998 → Java 2 introduced (with SE, EE, ME editions).

2006 → Sun released Java as open-source (OpenJDK).

2009 → Oracle acquired Sun Microsystems → Java under Oracle.

2014 → Java 8 released (major update: Lambdas, Streams, etc.).

2017 → Java 9 (modules), Oracle moved to 6-month release cycle.

Now → Latest stable version is Java 21 (LTS) (2023).



---

### 📌 Java Editions (Comparison)

Edition	Full Form	Use Case	Example Applications

Java SE	Standard Edition	Core Java (basic libraries, desktop apps, standalone programs)	Calculator, Text Editor
Java EE	Enterprise Edition	Extends SE with APIs for web & enterprise apps	E-commerce, Banking Apps
Java ME	Micro Edition	Subset of SE for mobile & embedded devices	Early Android (pre-2008), IoT



---

### 📌 Java Architecture (JDK, JRE, JVM)

JDK (Java Development Kit) → Complete package for developers (includes JRE + tools like compiler javac, debugger, etc.).

JRE (Java Runtime Environment) → Used to run Java applications (includes JVM + libraries).

JVM (Java Virtual Machine) → Executes bytecode (platform-independent), converts it into machine code for the OS/Hardware.


🔗 Interaction:

Developer writes .java → Compiler (javac in JDK) converts it into .class (bytecode).

JVM executes bytecode with help of JRE libraries.

JDK = JRE + Development tools, JRE = JVM + Libraries.

---

## 8. References / Acknowledgements

* Java official documentation: [https://docs.oracle.com/en/java/](https://docs.oracle.com/en/java/)
* StackOverflow for coding clarifications
* CSV reading/writing utilities examples

