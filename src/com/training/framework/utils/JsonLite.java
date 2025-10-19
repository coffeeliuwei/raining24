package com.training.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class JsonLite {
    // 极简JSON对象解析，仅支持一级键值：{"k":"v","n":123,"b":true}
    // 不处理转义字符与嵌套结构，满足教学与本项目POST体解析需求
    public static Map<String, String> parseObject(String json){
        Map<String, String> map = new HashMap<>();
        if(json == null) return map;
        int i = 0, n = json.length();
        // 跳过空白
        while(i < n && Character.isWhitespace(json.charAt(i))) i++;
        if(i < n && json.charAt(i) == '{') i++;
        while(i < n){
            // 跳过空白和逗号
            while(i < n && (Character.isWhitespace(json.charAt(i)) || json.charAt(i)==',')) i++;
            if(i >= n || json.charAt(i) == '}') break;
            // 读取key
            if(json.charAt(i) != '"') break; // 非严格校验
            i++; // 跳过开引号
            StringBuilder key = new StringBuilder();
            while(i < n && json.charAt(i) != '"'){ key.append(json.charAt(i)); i++; }
            if(i < n && json.charAt(i) == '"') i++; // 跳过闭引号
            // 跳过冒号与空白
            while(i < n && (json.charAt(i) == ':' || Character.isWhitespace(json.charAt(i)))) i++;
            // 读取value
            String val;
            if(i < n && json.charAt(i) == '"'){
                i++; // 开引号
                StringBuilder v = new StringBuilder();
                while(i < n && json.charAt(i) != '"'){ v.append(json.charAt(i)); i++; }
                if(i < n && json.charAt(i) == '"') i++; // 闭引号
                val = v.toString();
            } else {
                StringBuilder v = new StringBuilder();
                while(i < n && json.charAt(i) != ',' && json.charAt(i) != '}'){ v.append(json.charAt(i)); i++; }
                val = v.toString().trim();
            }
            map.put(key.toString(), val);
            // 下一个
            while(i < n && json.charAt(i) != ',' && json.charAt(i) != '}') i++;
            if(i < n && json.charAt(i) == ',') i++;
        }
        return map;
    }
}