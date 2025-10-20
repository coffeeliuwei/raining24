package com.training;

import com.training.framework.core.HttpServerApp;
import com.training.framework.core.Router;
import com.training.framework.core.Injector;
import com.training.controllers.StudentController;
import com.training.controllers.CourseController;
import com.training.controllers.EnrollController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws Exception {
        Injector injector = new Injector();
        Router router = new Router();

        // 注册控制器并注入依赖
        StudentController sc = new StudentController(); injector.inject(sc); router.registerController(sc);
        CourseController cc = new CourseController(); injector.inject(cc); router.registerController(cc);
        EnrollController ec = new EnrollController(); injector.inject(ec); router.registerController(ec);

        // 计算 webRoot 相对路径，避免硬编码绝对路径
        Path webRoot = Paths.get(System.getProperty("user.dir")).resolve("web");
        if (!Files.exists(webRoot)) {
            // 兼容从 bin/ 或 jar 位置执行时的路径
            Path alt = Paths.get("").toAbsolutePath().getParent();
            if (alt != null) {
                Path candidate = alt.resolve("web");
                if (Files.exists(candidate)) {
                    webRoot = candidate;
                }
            }
        }

        HttpServerApp app = new HttpServerApp(router, webRoot);
        app.start(8080);
    }
}