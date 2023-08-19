package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.Utilities.Utility.Pair;
import com.example.birdsofafeather.BlueToothMessageMockListener;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CourseEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AppDatabase db;

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CourseViewAdapter courseViewAdapter;
    private MessageListener mMessageListener;
    private Spinner yearSelect;
    private ArrayAdapter<CharSequence> yearAdapter;
    private Spinner quarterSelect;
    private ArrayAdapter<CharSequence> quarterAdapter;
    private Spinner sizeSelect;
    private ArrayAdapter<CharSequence> sizeAdapter;
    private String year = "";
    private String quarter = "";
    private String size = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_entry);

        db = AppDatabase.singleton(this);
        List<Course> courses = db.coursesDao().getForStudent("0");

        coursesRecyclerView = findViewById(R.id.courses_view);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseViewAdapter = new CourseViewAdapter(courses, (course) -> {db.coursesDao().delete(course);});
        coursesRecyclerView.setAdapter(courseViewAdapter);

        yearSelect = (Spinner) findViewById(R.id.year_select);
        yearAdapter = ArrayAdapter.createFromResource(this, R.array.years_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSelect.setAdapter(yearAdapter);
        yearSelect.setOnItemSelectedListener(this);
        quarterSelect = (Spinner) findViewById(R.id.quarter_select);
        quarterAdapter = ArrayAdapter.createFromResource(this, R.array.quarters_array, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSelect.setAdapter(quarterAdapter);
        quarterSelect.setOnItemSelectedListener(this);
        sizeSelect = (Spinner) findViewById(R.id.size_select);
        sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSelect.setAdapter(sizeAdapter);
        sizeSelect.setOnItemSelectedListener(this);
    }

    public void onDoneClicked(View view) {
        //finish();
        Intent intent = new Intent(this, activity_Sessions.class);
        startActivity(intent);
        //Intent intent = new Intent(this, home_screen.class);
        //startActivity(intent);
    }


    public boolean isTextViewEmpty(TextView textView) {
        return textView.getText().equals("");
    }

    public void onEnterCourseClicked(View view) {
        int newCourseId = db.coursesDao().count() + 1;
        TextView newSubjectTextView = findViewById(R.id.subject_textview);
        TextView newCourseNumberTextView = findViewById(R.id.course_number_textview);
        String newCourseText = year + "," + quarter + "," + newSubjectTextView.getText().toString() + "," + newCourseNumberTextView.getText().toString();

        Course newCourse = new Course("0", newCourseText, size);
        db.coursesDao().insert(newCourse);
        courseViewAdapter.addCourse(newCourse);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()) {
            case R.id.year_select:
                year = (String) parent.getItemAtPosition(pos);
                break;
            case R.id.quarter_select:
                quarter = (String) parent.getItemAtPosition(pos);
                break;
            case R.id.size_select:
                size = (String) parent.getItemAtPosition(pos);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {}

}