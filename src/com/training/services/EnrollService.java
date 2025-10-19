package com.training.services;

import java.util.*;

public class EnrollService {
    // studentId -> courseNames
    private final Map<Integer, Set<String>> map = new HashMap<>();

    public void enroll(int studentId, String courseName){
        map.computeIfAbsent(studentId, k -> new LinkedHashSet<>()).add(courseName);
    }

    public Set<String> ofStudent(int studentId){
        return map.getOrDefault(studentId, Collections.emptySet());
    }
}