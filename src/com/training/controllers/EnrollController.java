package com.training.controllers;

import com.training.framework.annotations.Controller;
import com.training.framework.annotations.Inject;
import com.training.framework.annotations.Route;
import com.training.framework.core.Json;
import com.training.framework.core.RequestContext;
import com.training.framework.utils.JsonLite;
import com.training.services.EnrollService;

import java.util.Map;
import java.util.Set;

@Controller
public class EnrollController {
    @Inject
    private EnrollService enrollService;

    @Route(path="/api/enroll", method="POST")
    public String enroll(RequestContext ctx){
        try{
            Map<String,String> m = JsonLite.parseObject(ctx.readBody());
            int studentId = Integer.parseInt(m.getOrDefault("studentId", "0"));
            String courseName = m.getOrDefault("courseName", "");
            enrollService.enroll(studentId, courseName);
            return Json.ok("success");
        }catch (Exception e){
            return Json.error(400, e.getMessage());
        }
    }

    @Route(path="/api/enroll", method="GET")
    public String ofStudent(RequestContext ctx){
        try{
            String q = ctx.getUri().getQuery();
            int studentId = 0;
            if(q != null){
                for(String p : q.split("&")){
                    String[] kv = p.split("=");
                    if(kv.length > 1 && kv[0].equals("studentId")){
                        studentId = Integer.parseInt(kv[1]);
                    }
                }
            }
            Set<String> courses = enrollService.ofStudent(studentId);
            return Json.ok(courses);
        }catch (Exception e){
            return Json.error(400, e.getMessage());
        }
    }
}