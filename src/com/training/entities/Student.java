package com.training.entities;

import com.training.framework.annotations.FieldFormat;
import java.util.Date;

public class Student {
    public int id;
    public String name;
    public boolean sex;
    public String phone;
    @FieldFormat("yyyy-MM-dd HH:mm:ss")
    public Date brithday; // 与示例保持同名拼写

    public Student(){}
    public Student(int id, String name, boolean sex, String phone){
        this.id = id; this.name = name; this.sex = sex; this.phone = phone;
        this.brithday = new Date();
    }
}