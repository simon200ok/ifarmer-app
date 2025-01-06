package com.ifarmr.service;

import com.ifarmr.entity.enums.NotificationStatus;
import com.ifarmr.payload.request.NotificationDto;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;

import java.util.List;

public interface NotificationService {

    void sendNotification(NotificationRequest notificationRequest);


    String subscribe(PushSubscriptionDTO subscriptionDTO);

    boolean checkSubscriptionStatus(Long userId);

    String unsubscribe(PushSubscriptionDTO subscriptionDTO);

    boolean checkSubscriptionStatusWithEndpoint(Long userId, String endpoint);

    List<NotificationDto> getAllNotifications(NotificationStatus status);

    List<NotificationDto> getNotificationsByUserId(Long userId, NotificationStatus status);

    void markNotificationAsRead(Long notificationId);
}
