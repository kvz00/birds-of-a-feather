package com.example.birdsofafeather.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class, Student.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "students.db").createFromAsset("starter_students_m2v2.db").allowMainThreadQueries().build();
        }

        return singletonInstance;
    }

    public abstract CoursesDao coursesDao();
    public abstract StudentsDao studentsDao();
}