package com.ifarmr.repository;

import com.ifarmr.entity.Task;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.payload.response.TaskResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    boolean existsByTitle(String title);

    List<Task> findByUserIdAndDueDateAfter(long userId, LocalDateTime dueDate);

    List<Task> findByUserIdAndDueDateAfterAndCategory(long userId, LocalDateTime dueDate, Category category);

    boolean existsByTitleAndIdNot(String title, Long taskId);
}
