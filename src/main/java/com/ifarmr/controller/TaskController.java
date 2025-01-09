package com.ifarmr.controller;

import com.ifarmr.payload.request.CreateTaskRequest;
import com.ifarmr.payload.request.UpdateTaskRequest;
import com.ifarmr.payload.response.TaskDetailsDto;
import com.ifarmr.payload.response.TaskDto;
import com.ifarmr.payload.response.TaskResponseDto;
import com.ifarmr.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create")
    public ResponseEntity<TaskResponseDto> createTask(
            @RequestParam Long userId,
            @RequestBody CreateTaskRequest taskRequest
    ) {
        TaskResponseDto createdTask = taskService.createTask(taskRequest, userId);
        return ResponseEntity.ok(createdTask);
    }


    @GetMapping("/alltasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/taskid")
    public ResponseEntity<TaskDto> getTaskById(@RequestParam Long taskId) {
        TaskDto task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/user_task")
    public ResponseEntity<List<TaskDto>> getUserTasks(@RequestParam Long userId) {
        List<TaskDto> userTasks = taskService.getUserTasks(userId);
        return ResponseEntity.ok(userTasks);
    }


    @PutMapping("/update_task")
    public ResponseEntity<TaskResponseDto> updateTask(
            @RequestParam Long taskId,
            @RequestBody UpdateTaskRequest taskRequest
    ) {
        TaskResponseDto updatedTask = taskService.updateTask(taskId, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }


    @DeleteMapping("/deletetask")
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId) {
        String response = taskService.deleteTask(taskId);
        return ResponseEntity.ok(response);
    }



}
