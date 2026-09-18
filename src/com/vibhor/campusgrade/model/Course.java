package com.vibhor.campusgrade.model;

import java.util.Objects;

public class Course {
    private final String code;
    private String title;
    private int credits;

    public Course(String code, String title, int credits) {
        this.code = code;
        this.title = title;
        this.credits = credits;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }

    public void setTitle(String title) { this.title = title; }
    public void setCredits(int credits) { this.credits = credits; }

    public String toFileString() {
        return String.join("|", code, title, String.valueOf(credits));
    }

    public static Course fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length != 3) throw new IllegalArgumentException("Invalid course record");
        return new Course(p[0], p[1], Integer.parseInt(p[2]));
    }

    @Override
    public String toString() {
        return String.format("%-10s %-38s %d", code, title, credits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }
}
