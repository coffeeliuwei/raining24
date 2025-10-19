package com.training.controllers;

import com.training.entities.Course;
import com.training.framework.annotations.Controller;
import com.training.framework.annotations.Inject;
import com.training.framework.annotations.Route;
import com.training.framework.core.Json;
import com.training.framework.core.RequestContext;
import com.training.framework.utils.JsonLite;
import com.training.services.CourseService;

import java.util.List;
import java.util.Map;

@Controller
public class CourseController {
    @Inject
    private CourseService courseService;

    @Route(path="/api/courses", method="GET")
    public String list(RequestContext ctx){
        List<Course> all = courseService.all();
        return Json.ok(all);
    }

    @Route(path="/api/courses", method="POST")
    public String add(RequestContext ctx){
        try{
            String body = ctx.readBody();
            Map<String,String> m = JsonLite.parseObject(body);
            String name = m.getOrDefault("name", "");
            String url = m.getOrDefault("url", "");
            Course c = new Course(name, url);
            courseService.add(c);
            return Json.ok(c);
        }catch (Exception e){
            return Json.error(400, e.getMessage());
        }
    }
}