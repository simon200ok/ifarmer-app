package com.ifarmr.service.Impl;

import com.ifarmr.entity.Task;
import com.ifarmr.entity.User;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.CreateTaskRequest;
import com.ifarmr.payload.request.UpdateTaskRequest;
import com.ifarmr.payload.response.TaskDetailsDto;
import com.ifarmr.payload.response.TaskDto;
import com.ifarmr.payload.response.TaskResponseDto;
import com.ifarmr.repository.TaskRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponseDto createTask(CreateTaskRequest request, Long userId) {
        // Fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        // Create and save the task
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);

        // Convert to TaskResponseDto
        return TaskResponseDto.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .completed(false) // Assuming new tasks are not completed by default
                .build();
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, UpdateTaskRequest request) {
        // Fetch the task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

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
                .completed(false) // Adjust based on your completed status logic
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
                        task.getDueDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllTasks() {
        // Fetch all tasks and map them to TaskDto
        return taskRepository.findAll().stream()
                .map(task -> new TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        // Map to TaskDto
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate()
        );
    }

    @Override
    public String deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        taskRepository.delete(task);
        return "Task deleted successfully.";
    }
}
