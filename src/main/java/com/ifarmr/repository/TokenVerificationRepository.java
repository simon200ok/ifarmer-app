package com.ifarmr.repository;

import com.ifarmr.entity.TokenVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenVerificationRepository extends JpaRepository<TokenVerification, Long> {
    Optional<TokenVerification> findByToken(String token);

    @Modifying
    @Query("DELETE FROM TokenVerification t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}