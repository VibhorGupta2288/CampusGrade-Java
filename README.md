# CampusGrade - Java Academic Performance Manager

CampusGrade is a command-line Java application for maintaining student records, defining courses, recording weighted assessments, and generating academic performance summaries.

The project was built as an original Programming in Java course project and is intentionally dependency-free: it uses the Java Standard Library and stores data in simple text CSV-like files so that an evaluator can compile and run it directly from a terminal.

## Features

### 1. Student Management
- Add a student with ID, name, email, and program.
- List all stored students.
- Duplicate student IDs are rejected.

### 2. Course Management
- Add a course with course code, title, and credit value.
- List all courses.
- Duplicate course codes and invalid credit values are rejected.

### 3. Assessment and Grading
- Record an assessment against a student and course.
- Validate score, maximum score, and weight.
- Calculate a weighted course percentage.
- Convert the course percentage to a letter grade and grade point.

### 4. Analytics and Reports
- Print a student's course-wise grade card.
- Calculate a credit-weighted GPA on a 10-point scale.
- Show a course class average and student scores.
- Run a what-if analysis to estimate the required average on remaining assessment weight for a target course percentage.

### 5. What-if Analysis
- Enter a target course percentage and estimate the average required on remaining assessment weight.
- Report when a target is already secured or mathematically unreachable.

### 6. Persistence
- Save students, courses, and assessments to `data/*.csv`.
- Reload stored data during the current execution.

## Java Concepts Demonstrated

- Classes and objects
- Encapsulation
- Constructors, methods, getters and setters
- Collections: `List`, `Map`
- Streams and lambda expressions
- Exception handling with a custom checked exception
- File I/O using `java.nio.file`
- Enums are not required; grading is implemented through a dedicated model class
- Modular package structure
- A target-score prediction module for remaining assessment weight
- Manual validation tests

## Project Structure

```text
CampusGrade/
├── README.md
├── statement.md
├── .gitignore
├── data/
│   ├── students.csv
│   ├── courses.csv
│   └── assessments.csv
├── docs/
│   ├── architecture.png
│   ├── workflow.png
│   ├── usecase.png
│   ├── sequence.png
│   ├── class.png
│   ├── data_model.png
│   └── sample_session.txt
├── src/
│   └── com/vibhor/campusgrade/
│       ├── Main.java
│       ├── exception/ValidationException.java
│       ├── model/Student.java
│       ├── model/Course.java
│       ├── model/Assessment.java
│       ├── model/GradeRecord.java
│       ├── repository/StudentRepository.java
│       ├── repository/FileDataStore.java
│       ├── service/AcademicService.java
│       ├── service/AnalyticsService.java
│       ├── service/PredictionService.java
│       └── util/InputValidator.java
└── tests/
    └── TestRunner.java
```

## Requirements

- Java Development Kit (JDK) 17 or newer.
- A terminal/command prompt.
- No external Java libraries are required.

The project was verified with OpenJDK 21.

For convenience, `run.sh` compiles and starts the application on Linux/macOS, `run.ps1` does the same on PowerShell, and `test.sh` compiles and runs the test suite.

## Setup and Run

Open a terminal at the repository root.

### 1. Compile

Linux/macOS:

```bash
rm -rf out
mkdir -p out
javac -d out $(find src tests -name "*.java")
```

Windows PowerShell:

```powershell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory out | Out-Null
javac -d out (Get-ChildItem -Recurse src,tests -Filter *.java).FullName
```

### 2. Run the application

Linux/macOS:

```bash
java -cp out com.vibhor.campusgrade.Main
```

Windows PowerShell:

```powershell
java -cp out com.vibhor.campusgrade.Main
```

The application reads the sample files in `data/` when it starts.

## Testing

Compile the project as above, then run:

```bash
java -cp out TestRunner
```

Expected result:

```text
ALL TESTS PASSED
```

The tests cover weighted percentage calculation, GPA calculation, invalid assessment input, duplicate student detection, target-score prediction, and file persistence.

## Data Format

The three persistence files are plain text with `|` as the field separator.

`students.csv`:

```text
studentId|name|email|program
```

`courses.csv`:

```text
courseCode|title|credits
```

`assessments.csv`:

```text
studentId|courseCode|assessmentName|score|maxScore|weight
```

The program validates user input so the `|` separator is not accepted inside entered values.

## Grading Logic

For each assessment:

```text
percentage = (score / maxScore) * 100
```

For a course, percentages are combined using the entered assessment weights:

```text
weighted course percentage = sum(assessment percentage * weight) / sum(weight)
```

Grade mapping:

| Percentage | Grade | Grade Point |
|---|---|---:|
| 90 - 100 | A+ | 10 |
| 80 - 89.99 | A | 9 |
| 70 - 79.99 | B | 8 |
| 60 - 69.99 | C | 7 |
| 50 - 59.99 | D | 6 |
| Below 50 | F | 0 |

GPA is the credit-weighted average of grade points for courses that have at least one recorded assessment.

## Design Artifacts

The `docs/` folder contains the system architecture, workflow, use case, sequence, class/component, and data model diagrams used in the project report.

## Version Control

The repository is designed to be used as a Git repository. The included `.gitignore` keeps generated Java bytecode and IDE files out of version control.

## Notes for Evaluation

- The program is fully terminal-based.
- It does not require a GUI, database server, or third-party package.
- The source is organized by model, service, repository, utility, and exception responsibilities.
