package com.ifarmr.service.impl;

import com.ifarmr.entity.Task;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.CreateTaskRequest;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.request.UpdateTaskRequest;
import com.ifarmr.payload.response.TaskDto;
import com.ifarmr.payload.response.TaskResponseDto;
import com.ifarmr.repository.TaskRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.repository.UserSessionRepository;
import com.ifarmr.service.NotificationService;
import com.ifarmr.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final NotificationService notificationService;


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
                .dueDate(savedTask.getDueDate())
                .build();
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, UpdateTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        if (request.getTitle() != null &&
                taskRepository.existsByTitleAndIdNot(request.getTitle(), taskId)) {
            throw new IllegalArgumentException("Task with the title '" + request.getTitle() + "' already exists.");
        }

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        Task updatedTask = taskRepository.save(task);

        return TaskResponseDto.builder()
                .id(updatedTask.getId())
                .title(updatedTask.getTitle())
                .description(updatedTask.getDescription())
                .category(task.getCategory())
                .type(task.getType())
                .location(task.getLocation())
                .dueDate(task.getDueDate())
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
                            task.getLocation(),
                            task.getDueDate()
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
                            task.getLocation(),
                            task.getDueDate()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleDueDateNotifications() {
        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            LocalDateTime dueDate = task.getDueDate();
            LocalDateTime now = LocalDateTime.now(); // Current time

            if (dueDate.minusHours(1).isBefore(now) && dueDate.minusHours(1).isAfter(now.minusMinutes(1))) {
                sendDueDateNotification(task, "1 hour remaining until the task is due.");
            }

            if (dueDate.minusMinutes(30).isBefore(now) && dueDate.minusMinutes(30).isAfter(now.minusMinutes(1))) {
                sendDueDateNotification(task, "30 minutes remaining until the task is due.");
            }

            if (dueDate.minusMinutes(15).isBefore(now) && dueDate.minusMinutes(15).isAfter(now.minusMinutes(1))) {
                sendDueDateNotification(task, "15 minutes remaining until the task is due.");
            }

            if (dueDate.minusMinutes(1).isBefore(now) && dueDate.minusMinutes(1).isAfter(now.minusSeconds(60))) {
                sendDueDateNotification(task, "1 minute remaining until the task is due.");
            }
        }
    }

    private void sendDueDateNotification(Task task, String message) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(task.getUser().getId());
        notificationRequest.setEventType("TASK_DUE_DATE");
        notificationRequest.setEventDetails(Map.of(
                "taskTitle", task.getTitle(),
                "message", message,
                "dueDate", task.getDueDate().toString()
        ));

        notificationService.sendNotification(notificationRequest);
    }

    @Override
    public List<TaskResponseDto> getTodayTasks(long userId, Category category) {
        LocalDate today = LocalDate.now();

        if (category != null) {
            return taskRepository.findByUserIdAndCategory(userId, category)
                    .stream()
                    .filter(task -> task.getDueDate().toLocalDate().isEqual(today))
                    .map(task -> new TaskResponseDto(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getCategory(),
                            task.getType(),
                            task.getLocation(),
                            task.getDueDate()
                    ))
                    .collect(Collectors.toList());
        } else {
            return taskRepository.findByUserId(userId)
                    .stream()
                    .filter(task -> task.getDueDate().toLocalDate().isEqual(today))
                    .map(task -> new TaskResponseDto(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getCategory(),
                            task.getType(),
                            task.getLocation(),
                            task.getDueDate()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public long countTodayTasks(long userId, Category category) {
        LocalDate today = LocalDate.now();

        if (category != null) {
            return taskRepository.findByUserIdAndCategory(userId, category)
                    .stream()
                    .filter(task -> task.getDueDate().toLocalDate().isEqual(today))
                    .count();
        } else {
            return taskRepository.findByUserId(userId)
                    .stream()
                    .filter(task -> task.getDueDate().toLocalDate().isEqual(today))
                    .count();
        }
    }


}