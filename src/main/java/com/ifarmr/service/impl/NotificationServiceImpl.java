package com.ifarmr.service.impl;

import com.ifarmr.entity.Notifications;
import com.ifarmr.entity.PushSubscription;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.NotificationStatus;
import com.ifarmr.payload.request.NotificationDto;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.PushSubscriptionDTO;
import com.ifarmr.repository.NotificationRepository;
import com.ifarmr.repository.SubscriptionRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.NotificationService;
import com.ifarmr.utils.ExtractUserID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final PushService pushService;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ExtractUserID extractUserID;
    private final HttpServletRequest httpServletRequest;

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

                Notifications notification = new Notifications();
                notification.setTitle(title);
                notification.setMessage(body);
                notification.setStatus(NotificationStatus.UNREAD);
                notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));


                User user = userRepository.findById(notificationRequest.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                notification.setUser(user);


                notificationRepository.save(notification);

                JSONObject notificationPayload = new JSONObject();
                notificationPayload.put("title", title);
                notificationPayload.put("body", body);

                Notification pushNotification = new Notification(
                        subscription.getEndpoint(),
                        subscription.getP256DH(),
                        subscription.getAuth(),
                        notificationPayload.toString()
                );

                pushService.send(pushNotification);
                System.out.println("Notifications sent to user " + notificationRequest.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public String subscribe(PushSubscriptionDTO subscriptionDTO) {
        Long userId = extractUserID.getUserIdFromToken(httpServletRequest);

        Optional<User> userOptional = userRepository.findById(userId);
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

    public boolean checkSubscriptionStatus() {
        Long userId = extractUserID.getUserIdFromToken(httpServletRequest);

        if (userId == null) {
            return false;
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            List<PushSubscription> subscriptions = subscriptionRepository.findByUser(userOptional.get());
            return !subscriptions.isEmpty();
        }
        return false;
    }

    public boolean checkSubscriptionStatusWithEndpoint(String endpoint) {
        Long userId = extractUserID.getUserIdFromToken(httpServletRequest);

        if (userId == null || endpoint == null) {
            return false;
        }

        List<PushSubscription> subscriptions = subscriptionRepository.findByUserIdAndEndpoint(userId, endpoint);
        return !subscriptions.isEmpty();
    }

    @Override
    public List<NotificationDto> getAllNotifications(NotificationStatus status) {
        List<Notifications> notifications;
        if (status != null) {
            notifications = notificationRepository.findAllByStatus(status);
        } else {
            notifications = notificationRepository.findAll();
        }

        return notifications.stream()
                .map(notification -> NotificationDto.builder()
                        .userId(notification.getUser().getId())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .status(notification.getStatus())
                        .notificationId(notification.getId())
                        .timestamp(notification.getTimestamp().toLocalDateTime())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getNotificationsByUserId(NotificationStatus status) {
        Long userId = extractUserID.getUserIdFromToken(httpServletRequest);
        List<Notifications> notifications;
        if (status != null) {
            notifications = notificationRepository.findByUserIdAndStatus(userId, status);
        } else {
            notifications = notificationRepository.findByUserId(userId);
        }

        return notifications.stream()
                .map(notification -> NotificationDto.builder()
                        .userId(notification.getUser().getId())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .status(notification.getStatus())
                        .notificationId(notification.getId())
                        .timestamp(notification.getTimestamp().toLocalDateTime())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public void markNotificationAsRead(Long notificationId) {

        Notifications notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus(NotificationStatus.READ);

        notificationRepository.save(notification);
    }



    @Override
    public String unsubscribe(PushSubscriptionDTO subscriptionDTO) {
        Long userId = extractUserID.getUserIdFromToken(httpServletRequest);

        Optional<User> userOptional = userRepository.findById(userId);
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
                return "Notifications";
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
                Long followerId = Long.parseLong(details.getOrDefault("followerId", "0"));
                Optional<User> follower = userRepository.findById(followerId);

                String followerFirstName = follower.map(User::getFirstName).orElse("Someone");
                String followerLastName = follower.map(User::getLastName).orElse("");

                return followerFirstName + " " + followerLastName + " is now following you!";

            case "POST_LIKE":
                Long likerId = Long.parseLong(details.getOrDefault("likerId", "0"));
                Optional<User> liker = userRepository.findById(likerId);

                String likerFirstName = liker.map(User::getFirstName).orElse("Someone");
                String likerLastName = liker.map(User::getLastName).orElse("");

                return likerFirstName + " " + likerLastName + " liked your post.";
            default:
                return "You have a new notification.";
        }
    }

    private String escapeJsonString(String input) {
        if (input == null) return "";
        return input.replaceAll("\\s+", " ") // Replace all whitespace with a single space
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\\", "\\\\"); // Escape backslashes
    }
}
