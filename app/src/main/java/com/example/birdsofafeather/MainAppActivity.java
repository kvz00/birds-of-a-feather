package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birdsofafeather.Utilities.CourseComparison;
import com.example.birdsofafeather.Utilities.Utility;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainAppActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected RecyclerView studentsRecyclerView;
    protected RecyclerView.LayoutManager studentsLayoutManager;
    protected StudentViewAdapter studentViewAdapter;
    AppDatabase db;
    private Spinner filterSelect;
    private ArrayAdapter<CharSequence> filterAdapter;
    private ArrayAdapter<String> filterClassesAdapter;
    private String filter = "Recent";
    private String filterClasses = "Course";
    private List<Student> validStudents;
    private List<Student> sortedBySize;
    private List<Student> sortedByRecency;
    private MessageListener mMessageListener;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newsessionpopup_name;
    private Button saveSessionButton, cancelSessionButton;
    private Intent sesssionIntent;

    Activity activity;

    Button startStop;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private BlueToothMessageMockListener blueToothMessageMockListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        sesssionIntent = new Intent(this, activity_Sessions.class);
        this.activity = this;
        sharedPreferences = getSharedPreferences("Sessions", MODE_PRIVATE);

        Set<String> sessionIds;

        Intent intent = getIntent();
        String sessionTitle = intent.getStringExtra("title");
        if(intent.getStringExtra("title") == null){
            sharedPreferences = getSharedPreferences("Started", MODE_PRIVATE);
            setTitle(sharedPreferences.getString("startedName", "New Session"));
            sessionIds = new HashSet<>();
        }
        else{
            ////
            setTitle(sessionTitle);
            sharedPreferences = getSharedPreferences("Started", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("startedName", sessionTitle);
            editor.apply();
            sharedPreferences = getSharedPreferences("Sessions", MODE_PRIVATE);
            sessionIds = sharedPreferences.getStringSet(sessionTitle, new HashSet<>());
        }

        //System.out.println(sessionTitle + ": " + sessionIds);

        db = AppDatabase.singleton(this);
        List<Student> students = db.studentsDao().getAllStudents();
        validStudents = new ArrayList<>();
        List<Course> ourCourses = db.coursesDao().getForStudent("0");

        for (Student student : students) {
            int count = 0;
            for (Course studentCourse : db.coursesDao().getForStudent(student.getStudentId())) {
                for (Course ourCourse: ourCourses) {
                    if (studentCourse.getCourseInfoText().equals(ourCourse.getCourseInfoText())) count++;
                }
            }

            //COMMENT OUT IF NEED TO TEST SESSION SAVE
            sharedPreferences = getSharedPreferences("Sessions", MODE_PRIVATE);
            SharedPreferences sharedPreferencesStarted = getSharedPreferences("Started", MODE_PRIVATE);
            sessionIds = sharedPreferences.getStringSet(sharedPreferencesStarted.getString("startedName", ""), new HashSet<>());
            if (count != 0 && !student.getStudentId().equals("0") && sessionIds.contains(student.getStudentId())) {
                validStudents.add(student);
            }
        }


        sortedBySize = new ArrayList<>();
        sortedByRecency = new ArrayList<>(validStudents);
        float tempSize = 0;
        boolean inOurCourses = false;
        for (Student s: validStudents) {
            for (Course c: db.coursesDao().getForStudent(s.getStudentId())) {
                for (Course c2: ourCourses) {
                    if (c.getCourseInfoText().equals(c2.getCourseInfoText())) {
                        inOurCourses = true;
                        break;
                    }
                }
                if (c.getClassSize().equals("Tiny") && inOurCourses) {
                    tempSize += 1.00;
                }
                else if (c.getClassSize().equals("Small")  && inOurCourses) {
                    tempSize += 0.33;
                }
                else if (c.getClassSize().equals("Medium")  && inOurCourses) {
                    tempSize += 0.18;
                }
                else if (c.getClassSize().equals("Large")  && inOurCourses) {
                    tempSize += 0.10;
                }
                else if (c.getClassSize().equals("Huge")  && inOurCourses) {
                    tempSize += 0.06;
                }
                else if (c.getClassSize().equals("Gigantic")  && inOurCourses) {
                    tempSize += 0.03;
                }
                inOurCourses = false;
            }
            s.setScoreSize(tempSize);
            sortedBySize.add(s);
            tempSize = 0;
        }
        Collections.sort(sortedBySize, (a, b) -> {
            return CourseComparison.compareSizeWeighted(a, b);
        });

        Collections.sort(sortedByRecency, (x, y) -> {
            return CourseComparison.compareTimeWeighted(
                    Utility.getCurrentQuarter(LocalDateTime.now()),
                    db.coursesDao().getForStudent("0"),
                    db.coursesDao().getForStudent(x.getStudentId()),
                    db.coursesDao().getForStudent(y.getStudentId()));
        });

        studentsRecyclerView = findViewById(R.id.students_view);

        studentsLayoutManager = new LinearLayoutManager(this);
        studentsRecyclerView.setLayoutManager(studentsLayoutManager);

        SharedPreferences sharedPreferencesStarted = getSharedPreferences("Started", MODE_PRIVATE);
        Boolean isStarted = sharedPreferencesStarted.getBoolean("started", false);



        studentViewAdapter = new StudentViewAdapter(validStudents);
        studentsRecyclerView.setAdapter(studentViewAdapter);

        filterSelect = (Spinner) findViewById(R.id.filter_select);
        filterAdapter = ArrayAdapter.createFromResource(this, R.array.filters_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSelect.setAdapter(filterAdapter);
        filterSelect.setOnItemSelectedListener(this);

        startStop = findViewById(R.id.startStop_button);
        if (isStarted) {
            startStop.setText(R.string.stop);
        } else {
            startStop.setText(R.string.start);
        }

        Map<String, String> m = new HashMap<>();
        m.put("FA", "Fall");
        m.put("WI", "Winter");
        m.put("SP", "Spring");
        m.put("SU", "Summer I");

        this.mMessageListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String decodedMessage = new String(message.getContent());
                Log.d("onFound", "Found message: " + new String(message.getContent()));
                if (decodedMessage.contains("user/")) {
                    String[] splitDecodedMessage = decodedMessage.replace("user/,", "").split(",");
                    Student newStudent = new Student(splitDecodedMessage[0], splitDecodedMessage[1], splitDecodedMessage[2]/*, splitDecodedMessage[0]*/);
                    db.studentsDao().insert(newStudent);

                    System.out.println("dsdd");
                    SharedPreferences sharedPreferencesStarted = getSharedPreferences("Started", MODE_PRIVATE);
                    System.out.println(sharedPreferencesStarted.getString("startedName", ""));
                    sharedPreferences = getSharedPreferences("Sessions", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    Set<String> ids = new HashSet<>(sharedPreferences.getStringSet(sharedPreferencesStarted.getString("startedName", ""), new HashSet<>()));
                    System.out.println(ids.size());
                    ids.add(db.studentsDao().getStudent(splitDecodedMessage[0]).getStudentId());
                    System.out.println(ids.size());
                    editor.putStringSet(sharedPreferencesStarted.getString("startedName", ""), ids);
                    editor.apply();


                } else if (decodedMessage.contains("action/")) {
                    String[] splitDecodedMessage = decodedMessage.replace("action/,", "").split("\n");
                    String[] userDecodedMessage = splitDecodedMessage[0].split(",");
                    String[] actionDecodedMessage = splitDecodedMessage[1].split(",");
                    if (splitDecodedMessage[1].contains("wave")) {
                        System.out.println("waved" + actionDecodedMessage[0]);
                        db.studentsDao().getStudent(userDecodedMessage[0]).setWaved(1);
                        Utility.showAlert(activity, db.studentsDao().getStudent(userDecodedMessage[0]).getStudentName() + " waved");

                    } else {
                        System.out.println(userDecodedMessage[0]);
                        System.out.println(actionDecodedMessage[0] + "," + m.get(actionDecodedMessage[1]) + "," + actionDecodedMessage[2] + "," + actionDecodedMessage[3]);
                        Course newCourse = new Course(userDecodedMessage[0], actionDecodedMessage[0] + "," + m.get(actionDecodedMessage[1]) + "," + actionDecodedMessage[2] + "," + actionDecodedMessage[3], actionDecodedMessage[4]);
                        db.coursesDao().insert(newCourse);
                    }
                }
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d("d", "Lost sign of message: " + new String(message.getContent()));
            }
        };
        this.mMessageListener = BlueToothMessageMockListener.BlueToothMessageMockListener(this.mMessageListener);
        blueToothMessageMockListener = BlueToothMessageMockListener.BlueToothMessageMockListener();
    }

    // COPY PASTE HERE

    private void subscribe() {
        //Log.i(TAG, "Subscribing");
        Nearby.getMessagesClient(this).subscribe(mMessageListener);
    }


    private void publish(Message message) {
        //Log.i(TAG, "Publishing");
        Nearby.getMessagesClient(this).publish(message);
    }


    private void unsubscribe() {
        // Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
    }


    private void unpublish(Message message) {
        // Log.i(TAG, "Unpublishing.");
        Nearby.getMessagesClient(this).unpublish(message); // Needs a message
    }

    public void onAddCourseClicked(View view) {
        setTitle("Birds of a Feather");
        Intent intent = new Intent(this, CourseEntryActivity.class);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        List<Student> favorites;
        favorites = new ArrayList<>();
        for (Student s: validStudents) {
            if (s.isFavorited()) favorites.add(s);
        }
        SharedPreferences sharedPreferencesStarted = getSharedPreferences("Started", MODE_PRIVATE);
        Boolean isStarted = sharedPreferencesStarted.getBoolean("started", false);
        if (!isStarted) {
            final List<Student> temp = new ArrayList<>();
            studentViewAdapter = new StudentViewAdapter(temp);
            studentsRecyclerView.setAdapter(studentViewAdapter);
            return;
        }
        switch (parent.getId()) {
            case R.id.filter_select:
                filter = (String) parent.getItemAtPosition(pos);
                if (filter.equals("Small classes")) {
                    studentViewAdapter = new StudentViewAdapter(sortedBySize);
                }
                else if (filter.equals("Favorites")) {
                    studentViewAdapter = new StudentViewAdapter(favorites);
                }
                else if (filter.equals("Recent")) {
                    studentViewAdapter = new StudentViewAdapter(sortedByRecency);
                }
                else {
                    studentViewAdapter = new StudentViewAdapter(validStudents);
                }
                studentsRecyclerView.setAdapter(studentViewAdapter);

                break;
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {}

    public void useCSV(View view) {
        TextView csvData = findViewById(R.id.csvData);
        String data = csvData.getText().toString();
        String[] splitData = data.split(",,,,");
        List<String> splitDataNewLine = new ArrayList<>();
        for (int i = 0; i < splitData.length; i++) {
            for (String line : splitData[i].split("\n")) {
                splitDataNewLine.add(line);
            }
        }
        splitDataNewLine = splitDataNewLine.stream().filter(x -> !x.equals("")).collect(Collectors.toList());

        List<Utility.Pair<String, List<String>>> userActionPairing = new ArrayList<>();

        String user = "";
        boolean prevDataAction = false;
        List<String> actions = new ArrayList<>();
        for (int i = 0; i < splitDataNewLine.size(); i++) {
            // This means that it is a user
            if (!splitDataNewLine.get(i).contains(",")) {
                if (prevDataAction) {
                    userActionPairing.add(new Utility.Pair<>(user, new ArrayList<>(actions)));
                    user = "";
                }
                user += "," + splitDataNewLine.get(i).trim();
                prevDataAction = false;
                actions.clear();
            } else {
                prevDataAction = true;
                actions.add(splitDataNewLine.get(i).trim());
            }
        }

        if (prevDataAction) {
            userActionPairing.add(new Utility.Pair<>(user, actions));
        }

        for (int i = 0; i < userActionPairing.size(); i++) {
            blueToothMessageMockListener.connectedUser(userActionPairing.get(i).getKey());
            for (int j = 0; j < userActionPairing.get(i).getValue().size(); j++) {
                blueToothMessageMockListener.actionFromUser(userActionPairing.get(i).getKey(), userActionPairing.get(i).getValue().get(j));
            }
        }

        csvData.setText("");
    }

    public void StartStopClicked(View view) {
        String state = (String) startStop.getText();
        if(state.toLowerCase().equals("start")){
            //System.out.println(state);
            sharedPreferences = getSharedPreferences("Started", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean("started", true);
            editor.apply();
            startStop.setText(R.string.stop);


        }
        else{
            //System.out.println("Else: " + state);
            sharedPreferences = getSharedPreferences("Started", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean("started", false);
            editor.apply();
            startStop.setText(R.string.start);
            createNewSessionDialog();
        }
    }

    public void createNewSessionDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View sessionPopupView = getLayoutInflater().inflate(R.layout.popup_session, null);
        newsessionpopup_name = (EditText) sessionPopupView.findViewById(R.id.newsessionpopup_name);

        saveSessionButton = (Button) sessionPopupView.findViewById(R.id.saveSessionButton);
        cancelSessionButton = (Button) sessionPopupView.findViewById(R.id.cancelSessionButton);

        dialogBuilder.setView(sessionPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        saveSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define save button
                EditText nameEditTxt = (EditText) dialog.findViewById(R.id.newsessionpopup_name);

                String sessionName = nameEditTxt.getText().toString();
                sharedPreferences = getSharedPreferences("Sessions", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                Set<String> ids = new HashSet<>();
                for (Student s : validStudents) {
                    ids.add(s.getStudentId());
                }
                editor.putStringSet(sessionName, ids);
                editor.apply();

                sharedPreferences.getStringSet("jj", new HashSet<>());
                for (String x : sharedPreferences.getStringSet("jj", new HashSet<>())) {
                    System.out.println(" xp "  + x);
                }


                Toast.makeText(MainAppActivity.this,"Session Saved", Toast.LENGTH_SHORT).show();
                startActivity(sesssionIntent);
            }
        });

        cancelSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(sesssionIntent);
                // dialog.dismiss();
            }
        });
    }
}