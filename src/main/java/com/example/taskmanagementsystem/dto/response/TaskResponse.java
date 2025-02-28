package com.example.taskmanagementsystem.dto.response;

import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String author;
    private Set<String> performers;
    private List<CommentResponse> comments;
}
