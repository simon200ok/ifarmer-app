package com.ifarmr.repository;

import com.ifarmr.entity.PushSubscription;
import com.ifarmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    List<PushSubscription> findByUserId(Long userId);

    Optional<PushSubscription> findByUserAndEndpoint(User user, String endpoint);

    List<PushSubscription> findByUser(User user);

    List<PushSubscription> findByUserIdAndEndpoint(Long userId, String endpoint);
}
