package com.wavemark.scheduler.schedule.constant;

import java.util.HashMap;
import java.util.Map;

public enum TaskType {

    AUTO_ORDER("Auto-Order"),
    SMART_ORDER("Smart-Order"),
    IMPLANT("Implant");

    private final String label;

    private static final Map<String, TaskType> LOOKUP = new HashMap<>();

    static {
        for (TaskType type : values())
            LOOKUP.put(type.label, type);
    }

    TaskType(String type) {
        this.label = type;
    }

    public static TaskType get(String type) {
        return LOOKUP.get(type);
    }

}