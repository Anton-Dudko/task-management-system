package com.example.taskmanagementsystem.dto.request;

import com.example.taskmanagementsystem.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriorityRequest {

    @NotNull(message = "Приоритет не должен быть пустым")
    private Priority priority;
}
