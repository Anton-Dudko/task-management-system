package com.example.taskmanagementsystem.model;

import lombok.Getter;

@Getter
public enum Priority {
    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private final String priority;

    Priority(String priority) {
        this.priority = priority;
    }
}
