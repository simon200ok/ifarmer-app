package com.ifarmr.repository;

import com.ifarmr.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

//    @Query("SELECT AVG(s.duration) FROM UserSession s WHERE s.duration IS NOT NULL")
//    Double findAverageSessionDuration();

    Optional<UserSession> findFirstByUserIdOrderByLoginTimeDesc(Long userId);
}
