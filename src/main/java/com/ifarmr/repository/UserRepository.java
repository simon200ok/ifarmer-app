package com.ifarmr.repository;

import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    Optional<User> findByEmail(String username);

    @Query(value = "SELECT EXTRACT(MONTH FROM last_login_time) AS month, " +
            "AVG(EXTRACT(EPOCH FROM (last_logout_time - last_login_time)) / 60) AS average_time " +
            "FROM user_tbl " +
            "WHERE last_logout_time IS NOT NULL " +
            "  AND last_login_time IS NOT NULL " +
            "  AND last_logout_time >= last_login_time " +
            "  AND EXTRACT(YEAR FROM last_login_time) = :year " +
            "GROUP BY EXTRACT(MONTH FROM last_login_time) " +
            "ORDER BY month", nativeQuery = true)
    List<Double[]> getMonthlyAverageUsageTime(@Param("year") int year);

    @Query("SELECT COUNT(u) FROM User u WHERE u.gender = 'MALE'")
    long getMaleUserCount();

    @Query("SELECT COUNT(u) FROM User u WHERE u.gender = 'FEMALE'")
    long getFemaleUserCount();

    @Query(value = "SELECT COUNT(*) FROM user_tbl", nativeQuery = true)
    long getTotalUsers();

    @Query(value = "SELECT COUNT(*) FROM user_tbl WHERE is_active = true", nativeQuery = true)
    long getActiveUsers();

    @Query(value = "SELECT COUNT(*) FROM user_tbl WHERE created_at >= :startTime", nativeQuery = true)
    long getNewUsersInLast24Hours(@Param("startTime") LocalDateTime startTime);

    @Query(value = "SELECT COUNT(*) FROM user_tbl WHERE created_at BETWEEN :startOfWeek AND :endOfWeek", nativeQuery = true)
    long getWeeklyNewUsers(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query(value = "SELECT COUNT(*) FROM user_tbl WHERE last_logout_time BETWEEN :startOfWeek AND :endOfWeek", nativeQuery = true)
    long getWeeklyActiveUsers(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);


}


