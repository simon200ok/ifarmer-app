package com.ifarmr.repository;

import com.ifarmr.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByUserId(Long userId);

    boolean existsByTitle(String title);

    List<Task> findByUserIdAndDueDateAfter(Long userId, LocalDateTime now);
}
