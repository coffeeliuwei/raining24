package com.training.services;

import com.training.entities.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private final List<Student> data = new ArrayList<>();

    public StudentService(){
        data.add(new Student(20201, "张三", true, "13800000000"));
        data.add(new Student(20202, "李四", false, "13800000001"));
        data.add(new Student(20203, "王五", true, "13800000002"));
    }

    public List<Student> all(){ return data; }

    public void add(Student s){ data.add(s); }
}