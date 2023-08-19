package com.example.birdsofafeather.model;

import android.widget.ImageView;

import com.example.birdsofafeather.model.IStudent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DummyStudent implements IStudent {
    private final String name;
    private final String picture_url;
    private final String[] courses;

    public DummyStudent(String name, String picture_url,String[] courses ){
        this.name = name;
        this.picture_url = picture_url;
        this.courses = courses;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getStudentCourses() {
        return courses;
    }

    @Override
    public void getUrlPicture(ImageView imageView) {
        /*TODO Max Fix/Implement using Picasso... https://square.github.io/picasso/, this isnt working



         */

        Picasso.get().load(picture_url).into(imageView);
    }

    @Override
    public String getUrlString() {
        return picture_url;
    }
}
