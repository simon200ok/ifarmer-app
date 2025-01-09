package com.ifarmr.service;

import com.ifarmr.entity.enums.NotificationStatus;
import com.ifarmr.payload.request.NotificationDto;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;

import java.util.List;

public interface NotificationService {

    void sendNotification(NotificationRequest notificationRequest);


    String subscribe(PushSubscriptionDTO subscriptionDTO);

    boolean checkSubscriptionStatus();

    String unsubscribe(PushSubscriptionDTO subscriptionDTO);

    boolean checkSubscriptionStatusWithEndpoint(String endpoint);

    List<NotificationDto> getAllNotifications(NotificationStatus status);

    List<NotificationDto> getNotificationsByUserId(NotificationStatus status);

    void markNotificationAsRead(Long notificationId);
}
