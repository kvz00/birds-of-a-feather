package com.example.birdsofafeather.model.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "student_id")
    private String studentId = "0";

    @ColumnInfo(name = "student_name")
    private String studentName;

    @ColumnInfo(name = "student_image")
    private String studentImage;

    @ColumnInfo(name = "score_size")
    private float scoreSize;

    @ColumnInfo(name = "waved")
    private int waved;

    @ColumnInfo(name = "favorited")
    private int favorited;

    public Student(String studentId, String studentName, String studentImage) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentImage = studentImage;
    }

    public String getStudentId() { return studentId; }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentImage() { return studentImage; }

    public void setStudentImage(String studentImage) { this.studentImage = studentImage; }

    public float getScoreSize() { return scoreSize; }

    public void setScoreSize(float scoreSize) { this.scoreSize = scoreSize; }

    public void setWaved(int waved) { this.waved = waved; }

    public int isWaved() { return this.waved; }

    //DO NOT USE
    public int getWaved() { return this.waved; }

    public void setFavorited(int favorited) { this.favorited = favorited; }

    //DO NOT USE
    public int getFavorited() { return this.favorited; }

    public boolean isFavorited() { return this.favorited == 1; }
}
