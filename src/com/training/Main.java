package com.training;

import com.training.framework.core.HttpServerApp;
import com.training.framework.core.Router;
import com.training.framework.core.Injector;
import com.training.controllers.StudentController;
import com.training.controllers.CourseController;
import com.training.controllers.EnrollController;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        Injector injector = new Injector();
        Router router = new Router();

        // 注册控制器并注入依赖
        StudentController sc = new StudentController(); injector.inject(sc); router.registerController(sc);
        CourseController cc = new CourseController(); injector.inject(cc); router.registerController(cc);
        EnrollController ec = new EnrollController(); injector.inject(ec); router.registerController(ec);

        HttpServerApp app = new HttpServerApp(router, Paths.get("d:/2022java/shixun/training24/web"));
        app.start(8080);
    }
}