package com.ifarmr.controller;

import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;
import com.ifarmr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok("Notification sent.");
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


}
