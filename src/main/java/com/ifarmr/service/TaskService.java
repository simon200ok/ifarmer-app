package com.ifarmr.service;

import com.ifarmr.entity.enums.Category;
import com.ifarmr.payload.request.CreateTaskRequest;
import com.ifarmr.payload.request.UpdateTaskRequest;
import com.ifarmr.payload.response.TaskDto;
import com.ifarmr.payload.response.TaskResponseDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(CreateTaskRequest request, Long userId, Category category);
    TaskResponseDto updateTask(Long taskId, UpdateTaskRequest request);
    List<TaskDto> getUserTasks(Long userId);
    List<TaskDto> getAllTasks();
    TaskDto getTaskById(Long taskId);
    String deleteTask(Long taskId);
    List<TaskResponseDto> getUpcomingTasks(long userId, Category category);
    List<TaskResponseDto> getTodayTasks(long userId, Category category);
    long countTodayTasks(long userId, Category category);
}
