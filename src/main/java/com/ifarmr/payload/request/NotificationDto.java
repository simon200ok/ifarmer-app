package com.ifarmr.payload.request;

import com.ifarmr.entity.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NotificationDto {

    private Long userId;

    private String title;

    private String message;

    private Long notificationId;

    private NotificationStatus status;
}
