package com.example.taskmanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddOrDeletePerformerRequest {
    @NotNull
    private Long taskId;
    @NotNull
    private String performerName;
}
