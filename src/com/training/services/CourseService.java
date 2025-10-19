package com.training.services;

import com.training.entities.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private final List<Course> data = new ArrayList<>();

    public CourseService(){
        data.add(new Course("Java Web", "http://example.com/javaweb"));
        data.add(new Course("Data Structure", "http://example.com/ds"));
    }

    public List<Course> all(){ return data; }

    public void add(Course c){ data.add(c); }
}