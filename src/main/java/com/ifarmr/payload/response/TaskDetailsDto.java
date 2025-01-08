package com.ifarmr.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;  // Import LocalDateTime

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsDto {

    private Long id; // Unique identifier for the task

    private String title; // Title of the task

    private String description; // Description of the task

    private LocalDateTime dueDate; // Due date for the task, now using LocalDateTime

    private LocalDateTime createdAt; // Date and time when the task was created

    private LocalDateTime updatedAt; // Date and time when the task was last updated

    // Constructor with the new signature (long, string, string, LocalDateTime, long)
    public TaskDetailsDto(Long id, String title, String description, LocalDateTime dueDate, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();  // or use actual data for createdAt
        this.updatedAt = LocalDateTime.now();  // or use actual data for updatedAt
    }
}
