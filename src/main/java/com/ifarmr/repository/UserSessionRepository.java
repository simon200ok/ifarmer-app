package com.ifarmr.repository;

import com.ifarmr.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findFirstByUserIdOrderByLoginTimeDesc(Long userId);

    void deleteByUserId(Long userId);
}
