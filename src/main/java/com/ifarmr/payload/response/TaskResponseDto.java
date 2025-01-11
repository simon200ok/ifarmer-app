package com.ifarmr.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
}
