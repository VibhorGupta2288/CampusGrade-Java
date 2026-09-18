package com.vibhor.campusgrade.service;

import com.vibhor.campusgrade.model.Course;
import com.vibhor.campusgrade.model.GradeRecord;
import com.vibhor.campusgrade.model.Student;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyticsService {
    private final AcademicService service;

    public AnalyticsService(AcademicService service) { this.service = service; }

    public double calculateGpa(String studentId) {
        double totalPoints = 0.0;
        int totalCredits = 0;
        for (Course c : service.getCourses()) {
            if (service.getStudentAssessments(studentId, c.getCode()).isEmpty()) continue;
            GradeRecord grade = new GradeRecord(c.getCode(), service.coursePercentage(studentId, c.getCode()));
            totalPoints += grade.getGradePoint() * c.getCredits();
            totalCredits += c.getCredits();
        }
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    public Map<String, GradeRecord> gradeCard(String studentId) {
        Map<String, GradeRecord> result = new LinkedHashMap<>();
        for (Map.Entry<String, Double> e : service.studentCoursePercentages(studentId).entrySet()) {
            result.put(e.getKey(), new GradeRecord(e.getKey(), e.getValue()));
        }
        return result;
    }

    public double classAverage(String courseCode) {
        double sum = 0.0;
        int count = 0;
        for (Student s : service.getStudents()) {
            if (!service.getStudentAssessments(s.getId(), courseCode).isEmpty()) {
                sum += service.coursePercentage(s.getId(), courseCode);
                count++;
            }
        }
        return count == 0 ? 0.0 : sum / count;
    }
}
