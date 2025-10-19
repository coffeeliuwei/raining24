package com.training.framework.core;

import com.training.framework.annotations.FieldFormat;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Json {
    public static String ok(Object data){
        String s = serialize(data);
        return "{\"error\":0,\"reason\":\"ok\",\"data\":" + s + "}";
    }

    public static String error(int code, String reason){
        reason = reason == null ? "error" : escape(reason);
        return "{\"error\":" + code + ",\"reason\":\"" + reason + "\"}";
    }

    public static String serialize(Object obj){
        if(obj == null) return "null";
        if(obj instanceof String) return "\"" + escape((String)obj) + "\"";
        if(obj instanceof Number || obj instanceof Boolean) return String.valueOf(obj);
        if(obj instanceof Map){
            @SuppressWarnings("unchecked") Map<String,Object> map = (Map<String,Object>) obj;
            String inner = map.entrySet().stream()
                    .map(e -> "\"" + escape(e.getKey()) + "\":" + serialize(e.getValue()))
                    .collect(Collectors.joining(","));
            return "{" + inner + "}";
        }
        if(obj instanceof Iterable){
            String inner = ((Iterable<?>)obj).iterator().hasNext() ?
                toArrayString((Iterable<?>)obj) : "";
            return "[" + inner + "]";
        }
        return pojo(obj);
    }

    private static String toArrayString(Iterable<?> it){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Object o : it){
            if(!first) sb.append(',');
            sb.append(serialize(o));
            first = false;
        }
        return sb.toString();
    }

    private static String pojo(Object obj){
        Class<?> cls = obj.getClass();
        Map<String,Object> map = new LinkedHashMap<>();
        for(Field f : cls.getDeclaredFields()){
            try{
                f.setAccessible(true);
                Object v = f.get(obj);
                if(v == null) continue;
                if(v instanceof Date){
                    String fmt = "yyyy-MM-dd";
                    FieldFormat ff = f.getAnnotation(FieldFormat.class);
                    if(ff != null) fmt = ff.value();
                    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
                    v = sdf.format((Date)v);
                }
                map.put(f.getName(), v);
            }catch (Exception ignored){}
        }
        return serialize(map);
    }

    private static String escape(String s){
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n","\\n");
    }
}