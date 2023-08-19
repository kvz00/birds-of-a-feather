package com.example.birdsofafeather.Utilities;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.example.birdsofafeather.model.ICourse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class Utility {

    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // Used to couple classes together for sorting/predicates
    public static class Pair<T, U> {
        public final T t;
        public final U u;

        public Pair(T t, U u) {
            this.t = t;
            this.u = u;
        }

        public T getKey() {
            return t;
        }

        public U getValue() {
            return u;
        }
    }

    public static enum Quarter {
        Fall,
        Winter,
        Spring,
        Summer;

        private static Quarter[] vals = values();

        public Quarter previous() {

            /**
             * Adding three in this case is equivalent to subtracting one but
             * simplifies the logic (no out of bounds).
             */
            return vals[(this.ordinal() + 3) % vals.length];
        }
    }

    public static Pair<Integer, Quarter> getCurrentQuarter(LocalDateTime currentTime) {
        Integer currentTimeYear = currentTime.getYear();
        Quarter currentTimeQuarter = null;
        switch (currentTime.getMonth()) {
            case DECEMBER:
            case NOVEMBER:
            case OCTOBER: {
                currentTimeQuarter = Quarter.Fall;
                break;
            }
            case SEPTEMBER: {
                if (currentTime.getDayOfMonth() >= 20) {
                    currentTimeQuarter = Quarter.Fall;
                } else {
                    currentTimeQuarter = Quarter.Summer;
                }
                break;
            }
            case AUGUST:
            case JULY: {
                currentTimeQuarter = Quarter.Summer;
                break;
            }
            case JUNE: {
                if (currentTime.getDayOfMonth() >= 20) {
                    currentTimeQuarter = Quarter.Summer;
                } else {
                    currentTimeQuarter = Quarter.Spring;
                }
                break;
            }
            case MAY:
            case APRIL: {
                currentTimeQuarter = Quarter.Spring;
                break;
            }
            case MARCH: {
                if (currentTime.getDayOfMonth() >= 23) {
                    currentTimeQuarter = Quarter.Spring;
                } else {
                    currentTimeQuarter = Quarter.Winter;
                }
                break;
            }
            case FEBRUARY:
            case JANUARY: {
                currentTimeQuarter = Quarter.Winter;
                break;
            }
        }

        return new Pair<>(currentTimeYear, currentTimeQuarter);
    }

    public static List<ICourse> getOverlapClasses(List<? extends ICourse> student1, List<? extends ICourse> student2, BiPredicate<ICourse, ICourse> coursePredicate) {
        List<ICourse> matches = new ArrayList<>();
        for (ICourse student1Course : student1) {
            for (ICourse student2Course : student2) {
                if (coursePredicate.test(student1Course, student2Course)) {
                    matches.add(student1Course);
                }
            }
        }
        return matches;
    }

    public static Integer parseCourseYear(String courseText) {
        return Integer.parseInt(courseText.split(",")[0]);
    }

    public static Quarter parseCourseQuarter(String courseText) {
        String[] splitCourseText = courseText.split(",");
        switch (splitCourseText[1]) {
            case "Fall": {
                return Quarter.Fall;
            }
            case "Winter": {
                return Quarter.Winter;
            }
            case "Spring": {
                return Quarter.Spring;
            }
            // All the other cases are different summer sessions
            default: {
                return Quarter.Summer;
            }
        }
    }
}
