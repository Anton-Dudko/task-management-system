package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.request.*;
import com.example.taskmanagementsystem.dto.response.TaskResponse;
import com.example.taskmanagementsystem.exceptions.*;
import com.example.taskmanagementsystem.mapper.TaskMapper;
import com.example.taskmanagementsystem.model.Role;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    public void createTask(TaskRequest taskRequest) {
        Optional.ofNullable(taskRequest)
                .map(taskMapper::taskRequestToTask)
                .map(taskRepository::save)
                .orElseThrow(()-> new TaskNotCreatedException("Задача не создана"));
    }

    public List<TaskResponse> getTasks(TaskFilter taskFilter, int page, int size) {

            var pageable = PageRequest.of(page, size);
            var a =  taskRepository.findAll(createSpecification(taskFilter), pageable)
                    .stream()
                    .map(taskMapper::taskToResponse)
                    .toList();
            return a;

    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(String.format("Task not found with id %s", id));
        }
        taskRepository.deleteById(id);
    }

    public List<TaskResponse> getTasksByAuthorName(String authorName) {
        return taskRepository.findByAuthor_Email(authorName).stream()
                .map(taskMapper::taskToResponse).toList();
    }

    public List<TaskResponse> getTasksByPerformerName(String performerName) {
        return taskRepository.findByPerformers_Email(performerName).stream()
                .map(taskMapper::taskToResponse).toList();
    }

    public void addPerformer(AddOrDeletePerformerRequest request) {
        var task = getTaskIfPresent(request);

        task.addPerformer(getUserIfPresent(request));
        taskRepository.save(task);
    }

    public void deletePerformer(AddOrDeletePerformerRequest request) {
        var task = getTaskIfPresent(request);

        task.removePerformer(getUserIfPresent(request));
        taskRepository.save(task);
    }

    public void updateTask(Long id, UpdateTaskRequest request) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task not found with id %s", id)));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPerformerEmails() != null) {

            Set<User> performers = new HashSet<>();
            for (String email : request.getPerformerEmails()) {
                performers.add(userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(String.format("User not found with name %s", email))));
            }
            task.setPerformers(performers);
        }
        taskRepository.save(task);
    }

    public void updateStatus(User user, Long id, StatusRequest request) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task not found with id %s", id)));

        if (task.getPerformers().contains(user) || user.getRole() == Role.ADMIN) {
            task.setStatus(request.getStatus());
            taskRepository.save(task);
        } else {
            throw new PermissionDeniedException("You do not have permission to change the status of this task");
        }
    }

    public void updatePriority(Long id, PriorityRequest request) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task not found with id %s", id)));

        task.setPriority(request.getPriority(

        ));
        taskRepository.save(task);
    }

    public void addComment(User user, Long taskId, CommentRequest request) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task not found with id %s", taskId)));

        if (task.getPerformers().contains(user) || user.getRole() == Role.ADMIN) {
            task.addComment(taskMapper.requestToComment(request, user, task));
            taskRepository.save(task);
        } else {
            throw new PermissionDeniedException("You do not have permission to change the status of this task");
        }
    }

    private User getUserIfPresent(AddOrDeletePerformerRequest request) {
        return Optional.ofNullable(request.getPerformerName())
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    private Task getTaskIfPresent(AddOrDeletePerformerRequest request) {
        return Optional.ofNullable(request)
                .map(AddOrDeletePerformerRequest::getTaskId)
                .flatMap(taskRepository::findById)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
    }

    private Specification<Task> createSpecification(TaskFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getPriority() != null && !filter.getPriority().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), filter.getPriority()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
