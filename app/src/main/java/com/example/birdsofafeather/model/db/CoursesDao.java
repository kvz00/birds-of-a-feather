package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CoursesDao {
    @Transaction
    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Transaction
    @Query("SELECT * FROM courses WHERE student_id=:id")
    List<Course> getForStudent(String id);

    @Query("SELECT * FROM courses WHERE course_id=:id")
    Course getFromId(int id);

    @Query("SELECT COUNT(*) FROM courses")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Delete
    void delete(Course course);
}
