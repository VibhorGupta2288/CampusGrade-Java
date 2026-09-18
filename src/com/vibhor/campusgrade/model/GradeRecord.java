package com.vibhor.campusgrade.model;

public class GradeRecord {
    private final String courseCode;
    private final double percentage;
    private final String letterGrade;
    private final double gradePoint;

    public GradeRecord(String courseCode, double percentage) {
        this.courseCode = courseCode;
        this.percentage = percentage;
        this.letterGrade = grade(percentage);
        this.gradePoint = points(letterGrade);
    }

    private static String grade(double p) {
        if (p >= 90) return "A+";
        if (p >= 80) return "A";
        if (p >= 70) return "B";
        if (p >= 60) return "C";
        if (p >= 50) return "D";
        return "F";
    }

    private static double points(String grade) {
        return switch (grade) {
            case "A+" -> 10.0;
            case "A" -> 9.0;
            case "B" -> 8.0;
            case "C" -> 7.0;
            case "D" -> 6.0;
            default -> 0.0;
        };
    }

    public String getCourseCode() { return courseCode; }
    public double getPercentage() { return percentage; }
    public String getLetterGrade() { return letterGrade; }
    public double getGradePoint() { return gradePoint; }
}
