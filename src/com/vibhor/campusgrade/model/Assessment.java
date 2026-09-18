package com.vibhor.campusgrade.model;

import com.vibhor.campusgrade.exception.ValidationException;

public class Assessment {
    private final String studentId;
    private final String courseCode;
    private final String name;
    private final double score;
    private final double maxScore;
    private final double weight;

    public Assessment(String studentId, String courseCode, String name,
                      double score, double maxScore, double weight) throws ValidationException {
        if (score < 0 || maxScore <= 0 || score > maxScore) {
            throw new ValidationException("Score must be between 0 and max score.");
        }
        if (weight <= 0 || weight > 100) {
            throw new ValidationException("Weight must be in the range (0, 100].");
        }
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.name = name;
        this.score = score;
        this.maxScore = maxScore;
        this.weight = weight;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public String getName() { return name; }
    public double getScore() { return score; }
    public double getMaxScore() { return maxScore; }
    public double getWeight() { return weight; }

    public double percentage() { return (score / maxScore) * 100.0; }

    public String toFileString() {
        return String.join("|", studentId, courseCode, name,
                String.valueOf(score), String.valueOf(maxScore), String.valueOf(weight));
    }

    public static Assessment fromFileString(String line) throws ValidationException {
        String[] p = line.split("\\|", -1);
        if (p.length != 6) throw new ValidationException("Invalid assessment record");
        return new Assessment(p[0], p[1], p[2],
                Double.parseDouble(p[3]), Double.parseDouble(p[4]), Double.parseDouble(p[5]));
    }
}
