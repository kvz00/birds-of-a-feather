package com.example.birdsofafeather.model;

import android.widget.ImageView;

import java.util.List;
import java.util.ArrayList;

public interface IStudent {
    String getName();
    String[] getStudentCourses();
    void getUrlPicture(ImageView imageView);
    String getUrlString();

}
