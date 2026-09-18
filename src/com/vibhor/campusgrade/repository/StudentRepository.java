package com.vibhor.campusgrade.repository;

import com.vibhor.campusgrade.exception.ValidationException;
import com.vibhor.campusgrade.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentRepository {
    private final Map<String, Student> students = new LinkedHashMap<>();

    public void add(Student student) throws ValidationException {
        if (students.containsKey(student.getId())) {
            throw new ValidationException("Student ID already exists: " + student.getId());
        }
        students.put(student.getId(), student);
    }

    public Student find(String id) { return students.get(id); }

    public List<Student> findAll() { return Collections.unmodifiableList(new ArrayList<>(students.values())); }

    public void clear() { students.clear(); }

    public void replaceAll(List<Student> loadedStudents) {
        students.clear();
        for (Student s : loadedStudents) students.put(s.getId(), s);
    }
}
