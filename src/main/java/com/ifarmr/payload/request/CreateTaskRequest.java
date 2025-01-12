package com.ifarmr.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Task title is required.")
    private String title;

    @NotBlank(message = "Task description is required.")
    private String description;

    @NotNull(message = "Task due date is required.")
    private LocalDateTime dueDate;

}
