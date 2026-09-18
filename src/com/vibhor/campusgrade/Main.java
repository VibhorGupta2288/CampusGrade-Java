package com.vibhor.campusgrade;

import com.vibhor.campusgrade.exception.ValidationException;
import com.vibhor.campusgrade.model.Assessment;
import com.vibhor.campusgrade.model.Course;
import com.vibhor.campusgrade.model.GradeRecord;
import com.vibhor.campusgrade.model.Student;
import com.vibhor.campusgrade.repository.FileDataStore;
import com.vibhor.campusgrade.repository.StudentRepository;
import com.vibhor.campusgrade.service.AcademicService;
import com.vibhor.campusgrade.service.AnalyticsService;
import com.vibhor.campusgrade.service.PredictionService;
import com.vibhor.campusgrade.util.InputValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final AcademicService academicService;
    private final AnalyticsService analyticsService;
    private final PredictionService predictionService;
    private final FileDataStore store = new FileDataStore(Path.of("data"));

    public Main() {
        academicService = new AcademicService(new StudentRepository());
        analyticsService = new AnalyticsService(academicService);
        predictionService = new PredictionService(academicService);
        loadData();
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("\n=== CampusGrade - Java Academic Performance Manager ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> listStudents();
                    case "3" -> addCourse();
                    case "4" -> listCourses();
                    case "5" -> recordAssessment();
                    case "6" -> studentReport();
                    case "7" -> courseSummary();
                    case "8" -> whatIfAnalysis();
                    case "9" -> saveData();
                    case "10" -> loadData();
                    case "0" -> running = false;
                    default -> System.out.println("Enter a valid option from 0 to 9.");
                }
            } catch (ValidationException | IOException e) {
                System.out.println("[ERROR] " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("[ERROR] Unexpected input. Please try again.");
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("\n1. Add student");
        System.out.println("2. List students");
        System.out.println("3. Add course");
        System.out.println("4. List courses");
        System.out.println("5. Record assessment");
        System.out.println("6. Student performance report");
        System.out.println("7. Course summary");
        System.out.println("8. What-if target analysis");
        System.out.println("9. Save data");
        System.out.println("10. Reload data");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private void addStudent() throws ValidationException {
        String id = InputValidator.required(prompt("Student ID"), "Student ID");
        String name = InputValidator.required(prompt("Name"), "Name");
        String email = InputValidator.required(prompt("Email"), "Email");
        String program = InputValidator.required(prompt("Program"), "Program");
        academicService.addStudent(new Student(id, name, email, program));
        System.out.println("Student added successfully.");
    }

    private void listStudents() {
        System.out.println("\nStudents");
        System.out.println("ID         Name                     Email                          Program");
        for (Student s : academicService.getStudents()) System.out.println(s);
    }

    private void addCourse() throws ValidationException {
        String code = InputValidator.required(prompt("Course code"), "Course code");
        String title = InputValidator.required(prompt("Course title"), "Course title");
        int credits = InputValidator.integer(prompt("Credits"), "Credits");
        academicService.addCourse(new Course(code, title, credits));
        System.out.println("Course added successfully.");
    }

    private void listCourses() {
        System.out.println("\nCourses");
        System.out.println("Code       Course title                             Credits");
        for (Course c : academicService.getCourses()) System.out.println(c);
    }

    private void recordAssessment() throws ValidationException {
        String studentId = InputValidator.required(prompt("Student ID"), "Student ID");
        String courseCode = InputValidator.required(prompt("Course code"), "Course code");
        String name = InputValidator.required(prompt("Assessment name"), "Assessment name");
        double score = InputValidator.number(prompt("Score"), "Score");
        double maxScore = InputValidator.number(prompt("Maximum score"), "Maximum score");
        double weight = InputValidator.number(prompt("Weight (%)"), "Weight");
        academicService.addAssessment(new Assessment(studentId, courseCode, name, score, maxScore, weight));
        System.out.println("Assessment recorded successfully.");
    }

    private void studentReport() throws ValidationException {
        String studentId = InputValidator.required(prompt("Student ID"), "Student ID");
        Student s = academicService.findStudent(studentId);
        if (s == null) throw new ValidationException("Student not found.");

        System.out.println("\nStudent: " + s.getName() + " (" + s.getId() + ")");
        System.out.println("Program: " + s.getProgram());
        System.out.println("------------------------------------------------------------");
        System.out.println("Course     Percentage     Grade     Grade Point");
        for (Map.Entry<String, GradeRecord> e : analyticsService.gradeCard(studentId).entrySet()) {
            GradeRecord g = e.getValue();
            System.out.printf("%-10s %10.2f%% %8s %14.1f%n", e.getKey(), g.getPercentage(), g.getLetterGrade(), g.getGradePoint());
        }
        System.out.printf("GPA: %.2f / 10.00%n", analyticsService.calculateGpa(studentId));
    }

    private void courseSummary() throws ValidationException {
        String code = InputValidator.required(prompt("Course code"), "Course code");
        Course course = academicService.findCourse(code);
        if (course == null) throw new ValidationException("Course not found.");
        System.out.println("\nCourse: " + course.getCode() + " - " + course.getTitle());
        System.out.printf("Class average: %.2f%%%n", analyticsService.classAverage(code));
        System.out.println("Student scores:");
        for (Student s : academicService.getStudents()) {
            if (!academicService.getStudentAssessments(s.getId(), code).isEmpty()) {
                System.out.printf("%-10s %-24s %.2f%%%n", s.getId(), s.getName(), academicService.coursePercentage(s.getId(), code));
            }
        }
    }

    private void whatIfAnalysis() throws ValidationException {
        String studentId = InputValidator.required(prompt("Student ID"), "Student ID");
        String courseCode = InputValidator.required(prompt("Course code"), "Course code");
        double target = InputValidator.number(prompt("Target final percentage"), "Target final percentage");
        double required = predictionService.requiredRemainingPercentage(studentId, courseCode, target);
        double current = academicService.coursePercentage(studentId, courseCode);
        System.out.printf("Current weighted percentage: %.2f%%%n", current);
        System.out.printf("Required average on remaining weight: %.2f%%%n", required);
        if (required <= 0) {
            System.out.println("Target is already mathematically secured by the recorded weight.");
        } else if (required > 100) {
            System.out.println("Target cannot be reached even with 100% on the remaining weight.");
        } else {
            System.out.println("The target is reachable if the remaining assessments average at least the value above.");
        }
    }

    private void saveData() throws IOException {
        store.save(academicService.getStudents(), academicService.getCourses(), academicService.getAssessments());
        System.out.println("Data saved to data/*.csv");
    }

    private void loadData() {
        try {
            academicService.replaceStudents(store.loadStudents());
            academicService.replaceCourses(store.loadCourses());
            academicService.replaceAssessments(store.loadAssessments());
            System.out.println("Data loaded from data/*.csv");
        } catch (IOException | ValidationException e) {
            System.out.println("[ERROR] Could not load data: " + e.getMessage());
        }
    }

    private String prompt(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }
}
