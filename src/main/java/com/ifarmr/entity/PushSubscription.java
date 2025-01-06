package com.ifarmr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_dtls")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PushSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String endpoint;

    @Column(length = 1024)
    private String p256DH;

    @Column(length = 1024)
    private String auth;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
