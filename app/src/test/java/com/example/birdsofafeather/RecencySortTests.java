package com.example.birdsofafeather;

import static com.example.birdsofafeather.Utilities.CourseComparison.compareTimeWeighted;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.Utilities.Utility;
import com.example.birdsofafeather.Utilities.Utility.Pair;
import com.example.birdsofafeather.Utilities.Utility.Quarter;

import com.example.birdsofafeather.model.DummyCourse;
import com.example.birdsofafeather.model.DummyStudent;
import com.example.birdsofafeather.model.ICourse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RecencySortTests {
    @Test
    public void testGetCurrentQuarterCurrentTime() {
        // Test getCurrentQuarter against the current LocalDateTime - should return Winter 2022
        LocalDateTime timeNow = LocalDateTime.now();
        Pair<Integer, Quarter> correctQuarter = new Pair<>(2022, Quarter.Winter);
        Pair<Integer, Quarter> actualQuarter = Utility.getCurrentQuarter(timeNow);
        assertEquals(correctQuarter.getKey(), actualQuarter.getKey());
        assertEquals(correctQuarter.getValue(), actualQuarter.getValue());
    }

    @Test
    public void testGetCurrentQuarterWinterSpringEdge() {
        // Test an edge case
        LocalDateTime edge1 = LocalDateTime.of(2022, Month.MARCH, 23, 0, 0, 0, 0);
        LocalDateTime edge2 = LocalDateTime.of(2022, Month.MARCH, 22, 23, 59, 59, 999999999);
        Pair<Integer, Quarter> correctQuarterEdge1 = new Pair<>(2022, Quarter.Spring);
        Pair<Integer, Quarter> correctQuarterEdge2 = new Pair<>(2022, Quarter.Winter);
        Pair<Integer, Quarter> actualQuarterEdge1 = Utility.getCurrentQuarter(edge1);
        Pair<Integer, Quarter> actualQuarterEdge2 = Utility.getCurrentQuarter(edge2);
        assertEquals(correctQuarterEdge1.getValue(), actualQuarterEdge1.getValue());
        assertEquals(correctQuarterEdge2.getValue(), actualQuarterEdge2.getValue());
    }

    @Test
    public void testCompareTimeWeightedBasic() {
        Pair<Integer, Quarter> currentQuarter = new Pair<Integer, Quarter>(2022, Quarter.Winter);

        // courseinfotext = it "year,quarter,course name (like cse),course number (like 101)"
        String course1infotext = "2021,Fall,CSE,100";
        String course2infotext = "2021,Spring,CSE,15L";
        String course3infotext = "2021,Winter,CSE,110";

        DummyCourse course1 = new DummyCourse(course1infotext);
        DummyCourse course2 = new DummyCourse(course2infotext);
        DummyCourse course3 = new DummyCourse(course3infotext);

        String[] student1courses = {course1infotext, course2infotext, course3infotext};
        String[] student2courses = {course1infotext};
        String[] student3courses = {course2infotext, course3infotext};

        DummyStudent student1 = new DummyStudent("Student 1", "STUDENT1PIC", student1courses);
        DummyStudent student2 = new DummyStudent("Student 2", "STUDENT2PIC", student2courses);
        DummyStudent student3 = new DummyStudent("Student 3", "STUDENT3PIC", student3courses);

        List<DummyCourse> student1courseList = new ArrayList<>();
        student1courseList.add(course1);
        student1courseList.add(course2);
        student1courseList.add(course3);
        List<DummyCourse> student2courseList = new ArrayList<>();
        student2courseList.add(course1);
        List<DummyCourse> student3courseList = new ArrayList<>();
        student3courseList.add(course2);
        student3courseList.add(course3);

        assertEquals(0, compareTimeWeighted(currentQuarter, student1courseList, student2courseList, student3courseList));
    }

    @Test
    public void testCompareTimeWeightedBeyond5() {
        Pair<Integer, Quarter> currentQuarter = new Pair<Integer, Quarter>(2022, Quarter.Winter);

        // courseinfotext = it "year,quarter,course name (like cse),course number (like 101)"
        String course1infotext = "2012,Spring,CSE,103";
        String course2infotext = "2013,Spring,CSE,12";
        //String course3infotext = "2022,Winter,CSE,110";

        DummyCourse course1 = new DummyCourse(course1infotext);
        DummyCourse course2 = new DummyCourse(course2infotext);
        //DummyCourse course3 = new DummyCourse(course3infotext);

        String[] student1courses = {course1infotext, course2infotext};
        String[] student2courses = {course1infotext};
        String[] student3courses = {};

        DummyStudent student1 = new DummyStudent("Student 1", "STUDENT1PIC", student1courses);
        DummyStudent student2 = new DummyStudent("Student 2", "STUDENT2PIC", student2courses);
        DummyStudent student3 = new DummyStudent("Student 3", "STUDENT3PIC", student3courses);

        List<DummyCourse> student1courseList = new ArrayList<>();
        student1courseList.add(course1);
        student1courseList.add(course2);
        List<DummyCourse> student2courseList = new ArrayList<>();
        student2courseList.add(course1);
        List<DummyCourse> student3courseList = new ArrayList<>();

        assertEquals(1, compareTimeWeighted(currentQuarter, student1courseList, student2courseList, student3courseList));
    }
}
