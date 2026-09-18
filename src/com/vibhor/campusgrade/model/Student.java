package com.vibhor.campusgrade.model;

import java.util.Objects;

public class Student {
    private final String id;
    private String name;
    private String email;
    private String program;

    public Student(String id, String name, String email, String program) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.program = program;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getProgram() { return program; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setProgram(String program) { this.program = program; }

    public String toFileString() {
        return String.join("|", id, name, email, program);
    }

    public static Student fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length != 4) throw new IllegalArgumentException("Invalid student record");
        return new Student(p[0], p[1], p[2], p[3]);
    }

    @Override
    public String toString() {
        return String.format("%-10s %-24s %-30s %-24s", id, name, email, program);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return id.equals(student.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
