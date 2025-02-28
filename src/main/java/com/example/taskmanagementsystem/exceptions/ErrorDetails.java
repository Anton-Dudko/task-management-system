package com.example.taskmanagementsystem.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private LocalDateTime createdAt;
    private String status;
    private String message;
}