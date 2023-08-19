package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDetailActivity extends AppCompatActivity {
    private AppDatabase db;
    private Student student;

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private StudentDetailViewAdapter studentDetailViewAdapter;
    private ImageButton waveHand;
    private ImageButton favoriteStar;

    public List<Course> getSharedCourses (List<Course> x, List<Course> y) {
        List<Course> temp = new ArrayList<>();
        for (Course studentCourse : x) {
            for (Course ourCourse: y) {
                if (studentCourse.getCourseInfoText().equals(ourCourse.getCourseInfoText())) temp.add(studentCourse);
            }
        }
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Intent intent = getIntent();
        String studentId = intent.getStringExtra("student_id");

        db = AppDatabase.singleton(this);
        student = db.studentsDao().getStudent(studentId);
        List<Course> courses = db.coursesDao().getForStudent(studentId);
        courses = getSharedCourses(courses, db.coursesDao().getForStudent("0"));
        setTitle(student.getStudentName());

        coursesRecyclerView = findViewById(R.id.student_courses_view);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        studentDetailViewAdapter = new StudentDetailViewAdapter(courses);
        coursesRecyclerView.setAdapter(studentDetailViewAdapter);
        waveHand = findViewById(R.id.wave_button);
        waveHand.setBackgroundResource(R.drawable.a416016766695jxj6ttwhn);
        student.setWaved(0);
        favoriteStar = findViewById(R.id.favorite_button);
        student.setFavorited(student.getFavorited());
        if (student.isFavorited()) favoriteStar.setBackgroundResource(R.drawable.downloadyellow);
        else favoriteStar.setBackgroundResource(R.drawable.download);
    }

    public void onGoBackClicked(View view) { finish(); }

    public void onWaveClick(View view) {
        waveHand.setBackgroundResource(R.drawable.a416016766695jxj6ttwhnyellow);
        student.setWaved(1);
        //do Bluetooth stuff
    }

    public void onFavoriteClick(View view) {
        if (student.isFavorited()) {
            db.studentsDao().unfavoriteStudent(student.getStudentId());
            favoriteStar.setBackgroundResource(R.drawable.download);
        }
        else {
            db.studentsDao().favoriteStudent(student.getStudentId());
            favoriteStar.setBackgroundResource(R.drawable.downloadyellow);
        }
        Log.d("favorite status", "isFavorited = " + student.isFavorited());
    }
}