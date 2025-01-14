package com.ifarmr.service.impl;

import com.ifarmr.entity.Task;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.CreateTaskRequest;
import com.ifarmr.payload.request.UpdateTaskRequest;
import com.ifarmr.payload.response.TaskDto;
import com.ifarmr.payload.response.TaskResponseDto;
import com.ifarmr.repository.TaskRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.repository.UserSessionRepository;
import com.ifarmr.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    @Override
    public TaskResponseDto createTask(CreateTaskRequest request, Long userId, Category category) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (taskRepository.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("Task with the title '" + request.getTitle() + "' already exists.");
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .location(request.getLocation())
                .category(category)
                .type(request.getType())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);

        return TaskResponseDto.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .category(savedTask.getCategory())
                .type(savedTask.getType())
                .location(savedTask.getLocation())
                .build();
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, UpdateTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        if (taskRepository.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("Task with the title '" + request.getTitle() + "' already exists.");
        }

        // Update task fields
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        Task updatedTask = taskRepository.save(task);

        // Convert to TaskResponseDto
        return TaskResponseDto.builder()
                .id(updatedTask.getId())
                .title(updatedTask.getTitle())
                .description(updatedTask.getDescription())
                .build();
    }


    @Override
    public List<TaskDto> getUserTasks(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Map tasks to TaskDto
        return taskRepository.findByUserId(userId).stream()
                .map(task -> new TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> TaskDto.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .dueDate(task.getDueDate())
                        .creationDate(task.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public TaskDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getCreatedAt());
    }

    @Override
    public String deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        taskRepository.delete(task);
        return "Task with ID "+ task.getId() +" has been deleted successfully.";
    }

    @Override
    public List<TaskResponseDto> getUpcomingTasks(long userId, Category category) {
        if (category != null) {
            return taskRepository.findByUserIdAndDueDateAfterAndCategory(userId, LocalDateTime.now(), category)
                    .stream()
                    .map(task -> new TaskResponseDto(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getCategory(),
                            task.getType(),
                            task.getLocation()
                    ))
                    .collect(Collectors.toList());
        } else {
            return taskRepository.findByUserIdAndDueDateAfter(userId, LocalDateTime.now())
                    .stream()
                    .map(task -> new TaskResponseDto(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getCategory(),
                            task.getType(),
                            task.getLocation()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
