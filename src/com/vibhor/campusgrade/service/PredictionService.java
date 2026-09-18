package com.vibhor.campusgrade.service;

import com.vibhor.campusgrade.exception.ValidationException;
import com.vibhor.campusgrade.model.Assessment;

import java.util.List;

public class PredictionService {
    private final AcademicService service;

    public PredictionService(AcademicService service) {
        this.service = service;
    }

    /**
     * Returns the minimum percentage needed on the remaining assessment weight
     * to reach the requested final course percentage.
     */
    public double requiredRemainingPercentage(String studentId, String courseCode, double targetPercentage)
            throws ValidationException {
        if (targetPercentage < 0 || targetPercentage > 100) {
            throw new ValidationException("Target percentage must be between 0 and 100.");
        }
        List<Assessment> rows = service.getStudentAssessments(studentId, courseCode);
        if (rows.isEmpty()) throw new ValidationException("No assessments found for this student and course.");

        double usedWeight = 0.0;
        double weightedPoints = 0.0;
        for (Assessment a : rows) {
            usedWeight += a.getWeight();
            weightedPoints += a.percentage() * a.getWeight();
        }
        if (usedWeight >= 100.0) {
            throw new ValidationException("No remaining assessment weight is available.");
        }

        double remainingWeight = 100.0 - usedWeight;
        return (targetPercentage * 100.0 - weightedPoints) / remainingWeight;
    }
}
