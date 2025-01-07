package com.ifarmr.repository;

import com.ifarmr.entity.Notifications;
import com.ifarmr.entity.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByUserId(Long userId);

    @Query("SELECT n FROM Notifications n ORDER BY n.timestamp DESC")
    List<Notifications> findAllOrderByTimestamp();


    List<Notifications> findAllByStatus(NotificationStatus status);


    List<Notifications> findByUserIdAndStatus(Long userId, NotificationStatus status);



}

