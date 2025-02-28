package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.request.*;
import com.example.taskmanagementsystem.dto.response.TaskResponse;
import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Создать задачу",
            description = "Создает новую задачу на основе предоставленных данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "403", description = "Нет доступа для выполнения запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void createTask(
            @Valid @RequestBody TaskRequest taskRequest) {
        taskService.createTask(taskRequest);
    }

    @GetMapping()
    @Operation(summary = "Получить список задач",
            description = "Возвращает список задач на основе необязательных фильтров, таких как статус, приоритет.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public List<TaskResponse> getTasks(
            @Parameter(description = "Фильтр задач по статусу")
            @RequestParam(required = false) String status,

            @Parameter(description = "Фильтр задач по приоритету")
            @RequestParam(required = false) String priority,

            @Parameter(description = "Номер страницы для пагинации", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Количество задач на странице", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        TaskFilter taskFilter = TaskFilter.builder()
                .status(status)
                .priority(priority)
                .build();
        return taskService.getTasks(taskFilter, page, size);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу",
            description = "Удаляет задачу по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен для удаления задачи"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void deleteTask(
            @Parameter(description = "ID задачи")
            @PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/authors/{authorName}")
    @Operation(summary = "Получить задачи по автору",
            description = "Возвращает список задач, созданных указанным автором.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public List<TaskResponse> getTasksByAuthor(
            @Parameter(description = "Имя автора")
            @PathVariable String authorName) {
        return taskService.getTasksByAuthorName(authorName);
    }

    @GetMapping("/performers/{performerName}")
    @Operation(summary = "Получить задачи по исполнителю",
            description = "Возвращает список задач, назначенных указанному исполнителю.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public List<TaskResponse> getTasksByPerformer(
            @Parameter(description = "Имя исполнителя")
            @PathVariable String performerName) {
        return taskService.getTasksByPerformerName(performerName);
    }

    @PostMapping("/performers")
    @Operation(summary = "Добавить исполнителя",
            description = "Добавляет нового исполнителя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Исполнитель успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void addPerformer(@Valid @RequestBody AddOrDeletePerformerRequest request) {
        taskService.addPerformer(request);
    }

    @DeleteMapping("/performers")
    @Operation(summary = "Удалить исполнителя",
            description = "Удаляет исполнителя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Исполнитель успешно удален"),
            @ApiResponse(responseCode = "404", description = "Исполнитель не найден"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void deletePerformer(@Valid @RequestBody AddOrDeletePerformerRequest request) {
        taskService.deletePerformer(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу",
            description = "Обновляет задачу по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "404 ", description = "Задача не найдена"),
            @ApiResponse(responseCode = "404 ", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void updateTask(
            @Parameter(description = "ID задачи для обновления")
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(id, request);
    }

    @PutMapping("/status/{id}")
    @Operation(summary = "Обновить статус задачи",
            description = "Обновляет статус задачи по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void updateStatus(@AuthenticationPrincipal User user,
                             @Parameter(description = "ID задачи для обновления статуса")
                             @PathVariable Long id,
                             @Valid @RequestBody StatusRequest request) {
        taskService.updateStatus(user, id, request);
    }

    @PutMapping("/priority/{id}")
    @Operation(summary = "Обновить приоритет задачи",
            description = "Обновляет приоритет задачи по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приоритет задачи успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void updatePriority(
            @Parameter(description = "ID задачи для обновления приоритета")
            @PathVariable Long id,
            @Valid @RequestBody PriorityRequest request) {
        taskService.updatePriority(id, request);
    }

    @PostMapping("/comments/{taskId}")
    @Operation(summary = "Добавить комментарий к задаче",
            description = "Добавляет комментарий к задаче по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void addComment(@AuthenticationPrincipal User user,
                           @Parameter(description = "ID задачи")
                           @PathVariable Long taskId,
                           @Valid @RequestBody CommentRequest request) {
        taskService.addComment(user, taskId, request);
    }
}
