package com.example.birdsofafeather.Utilities;

import static com.example.birdsofafeather.Utilities.Utility.getOverlapClasses;
import static com.example.birdsofafeather.Utilities.Utility.parseCourseQuarter;
import static com.example.birdsofafeather.Utilities.Utility.parseCourseYear;

import com.example.birdsofafeather.model.DummyCourse;
import com.example.birdsofafeather.model.ICourse;
import com.example.birdsofafeather.Utilities.Utility.Quarter;
import com.example.birdsofafeather.Utilities.Utility.Pair;
import com.example.birdsofafeather.model.db.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;


public class CourseComparison {
    interface CourseComparisonBehavior {
        boolean compare(List<? extends ICourse> student1, List<? extends ICourse> student2);
    }

    public static int compareTimeWeighted(Pair<Integer, Quarter> currentQuarter, List<? extends ICourse> own, List<? extends ICourse> student1, List<? extends ICourse> student2) {
        int timeWeightedScoreStudent1 = 0;
        int timeWeightedScoreStudent2 = 0;

        Integer currentQuarterYear = currentQuarter.getKey();
        Quarter currentQuarterQuarter = currentQuarter.getValue();

        BiPredicate<ICourse, ICourse> courseOverlap = (student1Course, student2Course) -> student1Course.getCourseInfoText().equals(student2Course.getCourseInfoText());

        List<ICourse> student1CourseMatches = getOverlapClasses(own, student1, courseOverlap);
        Integer student1CourseMatchesSize = student1CourseMatches.size();
        List<ICourse> student2CourseMatches = getOverlapClasses(own, student2, courseOverlap);
        Integer student2CourseMatchesSize = student2CourseMatches.size();


        for (int i = 5; i >= 1; i--) {
            currentQuarterQuarter = currentQuarterQuarter.previous();

            // If going back a quarter makes it fall, then it means we are in the previous year
            if (currentQuarterQuarter == Quarter.Fall) {
                currentQuarterYear--;
            }

            List<ICourse> tempCompareCourseList = new ArrayList<>();
            ICourse tempCompareCourse = new DummyCourse(currentQuarterYear + "," + currentQuarterQuarter + ",,");
            tempCompareCourseList.add(tempCompareCourse);

            BiPredicate<ICourse, ICourse> courseQuarterOverlap = (student1Course, student2Course) -> {
                return parseCourseQuarter(student1Course.getCourseInfoText()).equals(parseCourseQuarter(student2Course.getCourseInfoText())) &&
                        parseCourseYear(student1Course.getCourseInfoText()).equals(parseCourseYear(student2Course.getCourseInfoText()));
            };
            List<ICourse> student1CourseQuarterMatches = getOverlapClasses(tempCompareCourseList, student1CourseMatches, courseQuarterOverlap);
            timeWeightedScoreStudent1 += student1CourseQuarterMatches.size() * i;
            student1CourseMatchesSize -= student1CourseQuarterMatches.size();
            List<ICourse> student2CourseQuarterMatches = getOverlapClasses(tempCompareCourseList, student2CourseMatches, courseQuarterOverlap);
            timeWeightedScoreStudent2 += student2CourseQuarterMatches.size() * i;
            student2CourseMatchesSize -= student2CourseQuarterMatches.size();
        }

        return (timeWeightedScoreStudent1 + student1CourseMatchesSize) - (timeWeightedScoreStudent2 + student2CourseMatchesSize);
    }

    public static int compareSizeWeighted(Student student1, Student student2) {
        return Float.compare(student2.getScoreSize(), student1.getScoreSize());
    }
}
