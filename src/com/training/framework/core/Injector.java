package com.training.framework.core;

import com.training.framework.annotations.Inject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Injector {
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    public <T> T getOrCreate(Class<T> type){
        Object existing = singletons.get(type);
        if(existing != null) return type.cast(existing);
        try {
            T obj = type.getDeclaredConstructor().newInstance();
            singletons.put(type, obj);
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate service: " + type.getName(), e);
        }
    }

    public void inject(Object target){
        for(Field f : target.getClass().getDeclaredFields()){
            if(f.isAnnotationPresent(Inject.class)){
                Class<?> fieldType = f.getType();
                Object bean = getOrCreate(fieldType);
                try {
                    f.setAccessible(true);
                    f.set(target, bean);
                } catch (Exception e) {
                    throw new RuntimeException("Inject failed: " + target.getClass().getName() + "." + f.getName(), e);
                }
            }
        }
    }
}