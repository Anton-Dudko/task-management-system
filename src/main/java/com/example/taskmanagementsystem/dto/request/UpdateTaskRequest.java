package com.example.taskmanagementsystem.dto.request;

import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import jakarta.validation.constraints.Pattern;
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
public class UpdateTaskRequest {

    private String title;

    private String description;

    private Status status;

    private Priority priority;

    @UniqueElements(message = "Исполнители не могут дублироваться")
    private Set<String> performerEmails;
}
