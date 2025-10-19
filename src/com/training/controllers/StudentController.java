package com.training.controllers;

import com.training.entities.Student;
import com.training.framework.annotations.Controller;
import com.training.framework.annotations.Inject;
import com.training.framework.annotations.Route;
import com.training.framework.core.Json;
import com.training.framework.core.RequestContext;
import com.training.framework.utils.JsonLite;
import com.training.services.StudentService;

import java.util.List;
import java.util.Map;

@Controller
public class StudentController {
    @Inject
    private StudentService studentService;

    @Route(path="/api/students", method="GET")
    public String list(RequestContext ctx){
        List<Student> all = studentService.all();
        return Json.ok(all);
    }

    @Route(path="/api/students", method="POST")
    public String add(RequestContext ctx){
        try{
            String body = ctx.readBody();
            Map<String,String> m = JsonLite.parseObject(body);
            int id = Integer.parseInt(m.getOrDefault("id", "0"));
            String name = m.getOrDefault("name", "");
            boolean sex = Boolean.parseBoolean(m.getOrDefault("sex", "true"));
            String phone = m.getOrDefault("phone", "");
            Student s = new Student(id, name, sex, phone);
            studentService.add(s);
            return Json.ok(s);
        }catch (Exception e){
            return Json.error(400, e.getMessage());
        }
    }
}