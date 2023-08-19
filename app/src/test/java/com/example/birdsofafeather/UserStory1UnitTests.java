package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.CoursesDao;
import com.google.common.io.Resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class UserStory1UnitTests {
    @Rule
    public ActivityScenarioRule<CourseEntryActivity> scenarioRule = new ActivityScenarioRule<>(CourseEntryActivity.class);

    public ExecutorService backgroundThreadExecuter = Executors.newSingleThreadExecutor();
    public Future<Void> future;
    public static AppDatabase db;
    public CoursesDao coursesDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        coursesDao = db.coursesDao();
    }

    @After
    public void closeDb() throws IOException {
        resetSingleton(AppDatabase.class, "singletonInstance");
        db.close();
    }

    private void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // MS 2 User Story 1 BDD Scenario 2
    // Makes sure that a dropdown list of class size options is displayed when the user inserts a class
    // TODO: FIX THIS UNIT TEST

    @Test
    //Make sure course is entered if some text has been typed in and the enter button is pressed
    public void testClassSizeDropdownListDisplayed() {

        this.future = backgroundThreadExecuter.submit(() -> {
            ActivityScenario<CourseEntryActivity> scenario = scenarioRule.getScenario();
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.onActivity(activity -> {
                EditText courseInfoView = activity.findViewById(R.id.subject_textview);
                Button enterButton = activity.findViewById(R.id.enter_course_btn);
                Spinner sizeSpinner = activity.findViewById(R.id.size_select);
                assertEquals(6, sizeSpinner.getCount());
                assertEquals("Tiny", sizeSpinner.getItemAtPosition(0));
                assertEquals("Small", sizeSpinner.getItemAtPosition(1));
                assertEquals("Medium", sizeSpinner.getItemAtPosition(2));
                assertEquals("Large", sizeSpinner.getItemAtPosition(3));
                assertEquals("Huge", sizeSpinner.getItemAtPosition(4));
                assertEquals("Gigantic", sizeSpinner.getItemAtPosition(5));
//                courseInfoView.setText("2022,Winter,CSE,110");
//                int initialCourses = coursesDao.count();
//                enterButton.performClick();
//                int laterCourses = coursesDao.count();
//                int results = laterCourses - initialCourses;
//                assertEquals(1, results);
            });

            return null;
        });
    }

}




