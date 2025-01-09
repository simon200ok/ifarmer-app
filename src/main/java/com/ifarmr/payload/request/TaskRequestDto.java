package com.ifarmr.payload.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TaskRequestDto {
    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private boolean completed;
}
