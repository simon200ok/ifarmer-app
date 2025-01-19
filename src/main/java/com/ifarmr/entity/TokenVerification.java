package com.ifarmr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "token_tbl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    private boolean revoked;

    private LocalDateTime expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
