package com.example.taskmanagementsystem.dto.request;

import com.example.taskmanagementsystem.model.Status;
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
public class StatusRequest {

    @NotNull(message = "Статус не должен быть пустым")
    private Status status;
}
