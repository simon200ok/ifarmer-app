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

    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public TaskDetailsDto(Long id, String title, String description, LocalDateTime dueDate, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
