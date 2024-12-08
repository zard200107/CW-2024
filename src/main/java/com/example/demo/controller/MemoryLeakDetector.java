package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.WeakHashMap;

public class MemoryLeakDetector {
    private static final WeakHashMap<Object, String> instances = new WeakHashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void register(Object obj) {
        String info = String.format("%s @ %d created at %s",
                obj.getClass().getSimpleName(),
                System.identityHashCode(obj),
                LocalDateTime.now().format(formatter));
        instances.put(obj, info);
        System.out.println("注册新实例: " + info);
    }

    public static void printActiveInstances() {
        System.out.println("\n=== 活动实例 ===");
        instances.forEach((obj, info) -> {
            System.out.println(info);
        });
        System.out.println("==============\n");
    }
}