package com.example.birdsofafeather.Utilities;

import com.example.birdsofafeather.model.db.Student;

import java.util.Comparator;

public class StudentComparison implements Comparator<Student> {
    // Return 0 for "no swap", 1 for "swap"
    public int compare(Student student1, Student student2) {
        return student2.isWaved() - student1.isWaved();
    }
}