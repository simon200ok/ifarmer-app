package com.ifarmr.payload.response;

import com.ifarmr.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private String type;
    private String location;
    private LocalDateTime dueDate;
}
