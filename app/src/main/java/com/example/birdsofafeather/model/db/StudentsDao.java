package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface StudentsDao {
    @Transaction
    @Query("SELECT * FROM students")
    List<Student> getAllStudents();

    @Query("SELECT * FROM students WHERE student_id=:id")
    Student getStudent(String id);

    @Query("SELECT COUNT(*) FROM students")
    int count();

    @Query("UPDATE students SET favorited = 1 WHERE student_id=:id")
    void favoriteStudent(String id);

    @Query("UPDATE students SET favorited = 0 WHERE student_id=:id")
    void unfavoriteStudent(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Student student);

    @Delete
    void delete(Student student);
}
