package com.ifarmr.entity;

import com.ifarmr.entity.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

