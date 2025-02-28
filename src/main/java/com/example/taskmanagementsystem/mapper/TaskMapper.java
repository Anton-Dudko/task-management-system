package com.example.taskmanagementsystem.mapper;

import com.example.taskmanagementsystem.dto.request.CommentRequest;
import com.example.taskmanagementsystem.dto.request.TaskRequest;
import com.example.taskmanagementsystem.dto.response.CommentResponse;
import com.example.taskmanagementsystem.dto.response.TaskResponse;
import com.example.taskmanagementsystem.exceptions.UserNotFoundException;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;
    private final UserService userService;

    public Task taskRequestToTask(TaskRequest taskRequest) {

        Set<User> performers = new HashSet<>();
        for (String email : taskRequest.getPerformerEmails()) {
            performers.add(userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(String.format("Не найден пользователь с id %s", email))));
        }

        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .priority(taskRequest.getPriority())
                .author(userService.currentUser())
                .performers(performers)
                .build();
    }

    public TaskResponse taskToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(task.getAuthor().getEmail())
                .performers(task.getPerformers().stream().map(User::getEmail).collect(Collectors.toSet()))
                .comments(task.getComments().stream().map(this::commentToResponse).toList())
                .build();
    }

    public Comment requestToComment(CommentRequest request, User user, Task task) {
        return Comment.builder()
                .user(user)
                .task(task)
                .comment(request.getComment())
                .build();
    }

    public CommentResponse commentToResponse(Comment comment) {
        return CommentResponse.builder()
                .email(comment.getUser().getEmail())
                .comment(comment.getComment())
                .build();
    }
}
