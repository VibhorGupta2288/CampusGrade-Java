package com.vibhor.campusgrade.repository;

import com.vibhor.campusgrade.exception.ValidationException;
import com.vibhor.campusgrade.model.Assessment;
import com.vibhor.campusgrade.model.Course;
import com.vibhor.campusgrade.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileDataStore {
    private final Path dataDir;
    private final Path studentsFile;
    private final Path coursesFile;
    private final Path assessmentsFile;

    public FileDataStore(Path dataDir) {
        this.dataDir = dataDir;
        this.studentsFile = dataDir.resolve("students.csv");
        this.coursesFile = dataDir.resolve("courses.csv");
        this.assessmentsFile = dataDir.resolve("assessments.csv");
    }

    public void save(List<Student> students, List<Course> courses, List<Assessment> assessments) throws IOException {
        Files.createDirectories(dataDir);
        Files.write(studentsFile, linesOfStudents(students));
        Files.write(coursesFile, linesOfCourses(courses));
        Files.write(assessmentsFile, linesOfAssessments(assessments));
    }

    public List<Student> loadStudents() throws IOException {
        if (!Files.exists(studentsFile)) return new ArrayList<>();
        List<Student> result = new ArrayList<>();
        for (String line : Files.readAllLines(studentsFile)) if (!line.isBlank()) result.add(Student.fromFileString(line));
        return result;
    }

    public List<Course> loadCourses() throws IOException {
        if (!Files.exists(coursesFile)) return new ArrayList<>();
        List<Course> result = new ArrayList<>();
        for (String line : Files.readAllLines(coursesFile)) if (!line.isBlank()) result.add(Course.fromFileString(line));
        return result;
    }

    public List<Assessment> loadAssessments() throws IOException, ValidationException {
        if (!Files.exists(assessmentsFile)) return new ArrayList<>();
        List<Assessment> result = new ArrayList<>();
        for (String line : Files.readAllLines(assessmentsFile)) if (!line.isBlank()) result.add(Assessment.fromFileString(line));
        return result;
    }

    private List<String> linesOfStudents(List<Student> list) {
        return list.stream().map(Student::toFileString).toList();
    }
    private List<String> linesOfCourses(List<Course> list) {
        return list.stream().map(Course::toFileString).toList();
    }
    private List<String> linesOfAssessments(List<Assessment> list) {
        return list.stream().map(Assessment::toFileString).toList();
    }
}
