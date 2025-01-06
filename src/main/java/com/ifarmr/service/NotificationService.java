package com.ifarmr.service;

import com.ifarmr.entity.PushSubscription;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;

public interface NotificationService {

    public void sendNotification(NotificationRequest notificationRequest);


    String subscribe(PushSubscriptionDTO subscriptionDTO);

    boolean checkSubscriptionStatus(Long userId);

    String unsubscribe(PushSubscriptionDTO subscriptionDTO);

    public boolean checkSubscriptionStatusWithEndpoint(Long userId, String endpoint);
}
