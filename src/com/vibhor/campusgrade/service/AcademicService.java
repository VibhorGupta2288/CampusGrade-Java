package com.vibhor.campusgrade.service;

import com.vibhor.campusgrade.exception.ValidationException;
import com.vibhor.campusgrade.model.Assessment;
import com.vibhor.campusgrade.model.Course;
import com.vibhor.campusgrade.model.Student;
import com.vibhor.campusgrade.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class AcademicService {
    private final StudentRepository studentRepository;
    private final List<Course> courses = new ArrayList<>();
    private final List<Assessment> assessments = new ArrayList<>();

    public AcademicService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addStudent(Student student) throws ValidationException { studentRepository.add(student); }

    public void addCourse(Course course) throws ValidationException {
        if (findCourse(course.getCode()) != null) throw new ValidationException("Course code already exists: " + course.getCode());
        if (course.getCredits() < 1 || course.getCredits() > 6) throw new ValidationException("Credits must be between 1 and 6.");
        courses.add(course);
    }

    public void addAssessment(Assessment assessment) throws ValidationException {
        if (studentRepository.find(assessment.getStudentId()) == null) throw new ValidationException("Student not found.");
        if (findCourse(assessment.getCourseCode()) == null) throw new ValidationException("Course not found.");
        assessments.add(assessment);
    }

    public Course findCourse(String code) {
        return courses.stream().filter(c -> c.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }

    public Student findStudent(String id) { return studentRepository.find(id); }
    public List<Student> getStudents() { return studentRepository.findAll(); }
    public List<Course> getCourses() { return List.copyOf(courses); }
    public List<Assessment> getAssessments() { return List.copyOf(assessments); }

    public List<Assessment> getStudentAssessments(String studentId, String courseCode) {
        return assessments.stream()
                .filter(a -> a.getStudentId().equalsIgnoreCase(studentId))
                .filter(a -> courseCode == null || a.getCourseCode().equalsIgnoreCase(courseCode))
                .toList();
    }

    public double coursePercentage(String studentId, String courseCode) {
        List<Assessment> rows = getStudentAssessments(studentId, courseCode);
        double weightedScore = 0.0;
        double totalWeight = 0.0;
        for (Assessment a : rows) {
            weightedScore += a.percentage() * a.getWeight();
            totalWeight += a.getWeight();
        }
        return totalWeight == 0 ? 0.0 : weightedScore / totalWeight;
    }

    public Map<String, Double> studentCoursePercentages(String studentId) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (Course c : courses) {
            double p = coursePercentage(studentId, c.getCode());
            if (!getStudentAssessments(studentId, c.getCode()).isEmpty()) result.put(c.getCode(), p);
        }
        return result;
    }

    public void replaceStudents(List<Student> loadedStudents) {
        studentRepository.replaceAll(loadedStudents);
    }

    public void replaceCourses(List<Course> loadedCourses) {
        courses.clear();
        courses.addAll(loadedCourses);
    }

    public void replaceAssessments(List<Assessment> loadedAssessments) {
        assessments.clear();
        assessments.addAll(loadedAssessments);
    }
}
