package com.example.birdsofafeather.model;

public class DummyCourse implements ICourse {
    private String courseInfo;

    public DummyCourse(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    @Override
    public String getCourseInfoText() {
        return courseInfo;
    }
}
