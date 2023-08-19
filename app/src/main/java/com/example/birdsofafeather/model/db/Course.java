package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.birdsofafeather.model.ICourse;

@Entity(tableName = "courses")
public class Course implements ICourse {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="course_id")
    private int courseId;

    @ColumnInfo(name="student_id")
    private String studentId;

    @ColumnInfo(name="text")
    private String courseInfoText;

    @ColumnInfo(name="class_size")
    private String classSize;

    public Course(String studentId, String courseInfoText, String classSize) {
        this.studentId = studentId;
        this.courseInfoText = courseInfoText;
        this.classSize = classSize;
    }

    public int getCourseId() { return this.courseId; }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getStudentId() { return this.studentId; }

    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseInfoText() {
        return this.courseInfoText;
    }

    public void setCourseInfoText(String courseInfoText) { this.courseInfoText = courseInfoText; }

    public String getClassSize() { return this.classSize; }

    public void setClassSize(String classSize) { this.classSize = classSize; }
}
