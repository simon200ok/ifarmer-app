package com.ifarmr.controller;

import com.ifarmr.entity.enums.NotificationStatus;
import com.ifarmr.payload.request.NotificationDto;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;
import com.ifarmr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody PushSubscriptionDTO subscriptionDTO) {
        String response = notificationService.subscribe(subscriptionDTO);
        return ResponseEntity.ok(response);
    }


    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody PushSubscriptionDTO subscriptionDTO) {
        String response = notificationService.unsubscribe(subscriptionDTO);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.sendNotification(notificationRequest);
        return ResponseEntity.ok("Notifications sent.");
    }

    @GetMapping("/subscription-status")
    public ResponseEntity<Map<String, Boolean>> checkSubscriptionStatus(@RequestParam Long userId) {
        boolean status = notificationService.checkSubscriptionStatus(userId);
        return ResponseEntity.ok(Map.of("isSubscribed", status));
    }

    @GetMapping("/check-subscription")
    public ResponseEntity<Map<String, Boolean>> checkSubscriptionStatusWithEndpoint(
            @RequestParam("userId") Long userId,
            @RequestParam("endpoint") String endpoint) {
        boolean isSubscribed = notificationService.checkSubscriptionStatusWithEndpoint(userId, endpoint);
        return ResponseEntity.ok(Map.of("exists", isSubscribed));
    }

    // Endpoint to get all notifications (without any specific user filtering)
    @GetMapping("/get-all-notifications")
    public ResponseEntity<List<NotificationDto>> getAllNotifications(
            @RequestParam(value = "status", required = false) NotificationStatus status) {
        List<NotificationDto> notifications = notificationService.getAllNotifications(status);
        return ResponseEntity.ok(notifications);
    }

    // Endpoint to get notifications by userId (userId is passed as a query parameter)
    @GetMapping("/get-notifications-by-user")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserId(
            @RequestParam Long userId,
            @RequestParam(value = "status", required = false) NotificationStatus status) {
        List<NotificationDto> notifications = notificationService.getNotificationsByUserId(userId, status);
        return ResponseEntity.ok(notifications);
    }

    // Endpoint to mark a notification as read (notificationId passed as query parameter)
    @CrossOrigin(origins = "http://localhost:5173")
    @PatchMapping("/read")
    public ResponseEntity<Void> markNotificationAsRead(@RequestParam Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }


}
