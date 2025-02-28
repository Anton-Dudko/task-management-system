package com.example.taskmanagementsystem.dto.request;

import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    @NotBlank(message = "Название не должно быть пустым")
    private String title;

    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    private Status status = Status.WAITING;

    private Priority priority = Priority.LOW;

    @UniqueElements(message = "Исполнители не могут дублироваться")
    private Set<String> performerEmails;
}
