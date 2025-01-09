package com.ifarmr.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ifarmr.entity.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NotificationDto {

    private Long userId;

    private String title;

    private String message;

    private Long notificationId;

    @JsonFormat(pattern = "HH:mm dd-MM")
    private LocalDateTime timestamp;

    private NotificationStatus status;
}
