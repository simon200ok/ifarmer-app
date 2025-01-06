package com.ifarmr.service.impl;

import com.ifarmr.entity.PushSubscription;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;
import com.ifarmr.repository.SubscriptionRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final PushService pushService;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        List<PushSubscription> subscriptions = subscriptionRepository.findByUserId(notificationRequest.getUserId());
        if (subscriptions.isEmpty()) {
            System.out.println("No subscriptions found for user " + notificationRequest.getUserId());
            return;
        }

        String title = generateTitle(notificationRequest.getEventType(), notificationRequest.getEventDetails());
        String body = generateBody(notificationRequest.getEventType(), notificationRequest.getEventDetails());

        for (PushSubscription subscription : subscriptions) {
            try {
                JSONObject notificationPayload = new JSONObject();
                notificationPayload.put("title", title);
                notificationPayload.put("body", body);

                Notification notification = new Notification(
                        subscription.getEndpoint(),
                        subscription.getP256DH(),
                        subscription.getAuth(),
                        notificationPayload.toString()
                );

                pushService.send(notification);
                System.out.println("Notification sent to user " + notificationRequest.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String subscribe(PushSubscriptionDTO subscriptionDTO) {
        if (subscriptionDTO.getUserId() == null) {
            return "User ID is missing in the subscription request.";
        }

        Optional<User> userOptional = userRepository.findById(subscriptionDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<PushSubscription> existingSubscription = subscriptionRepository.findByUserAndEndpoint(user, subscriptionDTO.getEndpoint());
            if (existingSubscription.isPresent()) {
                return "User is already subscribed with this endpoint.";
            }

            PushSubscription subscription = new PushSubscription();
            subscription.setEndpoint(subscriptionDTO.getEndpoint());
            subscription.setP256DH(subscriptionDTO.getP256dh());
            subscription.setAuth(subscriptionDTO.getAuth());
            subscription.setUser(user);

            subscriptionRepository.save(subscription);
            return "Subscription saved.";
        } else {
            return "User not found.";
        }
    }

    public boolean checkSubscriptionStatus(Long userId) {
        if (userId == null) {
            return false;
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            List<PushSubscription> subscriptions = subscriptionRepository.findByUser(userOptional.get());
            return !subscriptions.isEmpty();  // If there are any subscriptions, return true
        }
        return false; // User not found
    }

    public boolean checkSubscriptionStatusWithEndpoint(Long userId, String endpoint) {
        if (userId == null || endpoint == null) {
            return false;
        }

        List<PushSubscription> subscriptions = subscriptionRepository.findByUserIdAndEndpoint(userId, endpoint);
        return !subscriptions.isEmpty();  // If subscription exists for the endpoint, return true
    }


    @Override
    public String unsubscribe(PushSubscriptionDTO subscriptionDTO) {
        if (subscriptionDTO.getUserId() == null) {
            return "User ID is missing in the unsubscribe request.";
        }

        Optional<User> userOptional = userRepository.findById(subscriptionDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<PushSubscription> subscription = subscriptionRepository.findByUserAndEndpoint(user, subscriptionDTO.getEndpoint());
            if (subscription.isPresent()) {
                subscriptionRepository.delete(subscription.get());
                return "Unsubscribed successfully.";
            } else {
                return "No subscription found for the user with the specified endpoint.";
            }
        } else {
            return "User not found.";
        }
    }

    private String generateTitle(String eventType, Map<String, String> details) {
        switch (eventType) {
            case "NEW_COMMENT":
                return "New Comment Alert!";
            case "NEW_FOLLOWER":
                return "You Have a New Follower!";
            case "POST_LIKE":
                return "Your Post Got a Like!";
            default:
                return "Notification";
        }
    }

    private String generateBody(String eventType, Map<String, String> details) {
        switch (eventType) {
            case "NEW_COMMENT":
                Long commenterId = Long.parseLong(details.getOrDefault("commenterId", "0"));
                Optional<User> commenter = userRepository.findById(commenterId);

                String commenterFirstName = commenter.map(User::getFirstName).orElse("Someone");
                String commenterLastName = commenter.map(User::getLastName).orElse("");
                String commentText = details.getOrDefault("commentText", "No comment provided");

                return commenterFirstName + " " + commenterLastName + " commented on your post: \""
                        + escapeJsonString(commentText) + "\"";

            case "NEW_FOLLOWER":
                return details.getOrDefault("followerName", "Someone") + " is now following you!";
            case "POST_LIKE":
                return details.getOrDefault("likerName", "Someone") + " liked your post.";
            default:
                return "You have a new notification.";
        }
    }

    private String escapeJsonString(String input) {
        if (input == null) return "";
        return input.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
