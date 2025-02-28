package com.example.taskmanagementsystem.model;

import lombok.Getter;

@Getter
public enum Status {

    IN_PROGRESS("in_progress"),
    WAITING("waiting"),
    COMPLETED("completed");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
