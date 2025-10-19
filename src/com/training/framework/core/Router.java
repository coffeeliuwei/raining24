package com.training.framework.core;

import com.training.framework.annotations.Controller;
import com.training.framework.annotations.Route;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Router {
    private final Map<String, BiFunction<RequestContext, Map<String,String>, String>> routes = new HashMap<>();

    public void registerController(Object controller){
        if(!controller.getClass().isAnnotationPresent(Controller.class)){
            throw new IllegalArgumentException("Not a @Controller: " + controller.getClass().getName());
        }
        for(Method m : controller.getClass().getDeclaredMethods()){
            if(m.isAnnotationPresent(Route.class)){
                Route r = m.getAnnotation(Route.class);
                String key = key(r.method(), r.path());
                m.setAccessible(true);
                routes.put(key, (ctx, params) -> {
                    try {
                        if(m.getParameterCount() == 1 && m.getParameterTypes()[0] == RequestContext.class){
                            Object ret = m.invoke(controller, ctx);
                            return (String) ret;
                        } else if(m.getParameterCount() == 2 && m.getParameterTypes()[0] == RequestContext.class){
                            Object ret = m.invoke(controller, ctx, params);
                            return (String) ret;
                        } else {
                            Object ret = m.invoke(controller);
                            return (String) ret;
                        }
                    } catch (Exception e){
                        return Json.error(500, e.getMessage());
                    }
                });
            }
        }
    }

    public String dispatch(String method, String path, RequestContext ctx){
        String key = key(method, path);
        BiFunction<RequestContext, Map<String,String>, String> fn = routes.get(key);
        if(fn == null){
            return Json.error(404, "Not Found: " + path);
        }
        Map<String,String> params = ctx.queryParams();
        return fn.apply(ctx, params);
    }

    private String key(String method, String path){
        return method.toUpperCase() + " " + path;
    }
}