package com.ifarmr.repository;

import com.ifarmr.entity.Task;
import com.ifarmr.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(long userId);

    boolean existsByTitle(String title);

    List<Task> findByUserIdAndDueDateAfter(long userId, LocalDateTime dueDate);

    List<Task> findByUserIdAndDueDateAfterAndCategory(long userId, LocalDateTime dueDate, Category category);

    boolean existsByTitleAndIdNot(String title, Long taskId);

    List<Task> findByUserIdAndCategory(long userId, Category category);
}
