package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class activity_Sessions extends AppCompatActivity {
    protected RecyclerView sessionRecyclerView;
    protected RecyclerView.LayoutManager sessionLayoutManager;
    protected SessionViewAdapter sessionViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        sessionRecyclerView = findViewById(R.id.session_view);

        sessionLayoutManager = new LinearLayoutManager(this);
        sessionRecyclerView.setLayoutManager(sessionLayoutManager);

        List<String> sessionNames = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Sessions", MODE_PRIVATE);
        Map<String, ?> keys = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            sessionNames.add(entry.getKey());
        }

        SharedPreferences sharedPreferencesStarted = getSharedPreferences("Started", MODE_PRIVATE);
        Boolean isStarted = sharedPreferencesStarted.getBoolean("started", false);
        if (isStarted) {
            Intent intent = new Intent(this, MainAppActivity.class);
            startActivity(intent);
        }

//
//        System.out.println(keys);
//
//        System.out.println("Test");
//        System.out.println("Session Len: " +  sessionNames.size());
//        for(String name : sessionNames){
//            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
//            System.out.println(name);
//        }

        sessionViewAdapter = new SessionViewAdapter(sessionNames);
        sessionRecyclerView.setAdapter(sessionViewAdapter);

    }

    public void onAddCourseClicked(View view) {
        Intent intent = new Intent(this, CourseEntryActivity.class);
        startActivity(intent);
    }

    public void sessionClicked(View view) {
        //when you click create a new session

        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);


    }
}