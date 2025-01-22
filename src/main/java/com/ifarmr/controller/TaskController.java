package com.ifarmr.controller;

import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.payload.request.CreateTaskRequest;
import com.ifarmr.payload.request.UpdateTaskRequest;
import com.ifarmr.payload.response.TaskDto;
import com.ifarmr.payload.response.TaskResponseDto;
import com.ifarmr.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskResponseDto> createTask(@AuthenticationPrincipal User user,
                                                      @RequestBody CreateTaskRequest taskRequest,
                                                      @RequestParam Category category) {
        TaskResponseDto createdTask = taskService.createTask(taskRequest, user.getId(), category);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/taskId")
    public ResponseEntity<TaskDto> getTaskById(@RequestParam Long taskId) {
        TaskDto task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/userTasks")
    public ResponseEntity<List<TaskDto>> getUserTasks(@AuthenticationPrincipal User user) {
        List<TaskDto> userTasks = taskService.getUserTasks(user.getId());
        return ResponseEntity.ok(userTasks);
    }


    @PutMapping("/updateTasks")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestParam Long taskId, @RequestBody UpdateTaskRequest taskRequest) {
        TaskResponseDto updatedTask = taskService.updateTask(taskId, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }


    @DeleteMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId) {
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<TaskResponseDto>> getUpcomingTasks(@AuthenticationPrincipal User user,
                                                                  @RequestParam(value = "category", required = false) Category category) {
        return ResponseEntity.ok(taskService.getUpcomingTasks(user.getId(), category));
    }

    @GetMapping("/today")
    public ResponseEntity<List<TaskResponseDto>> getTodayTasks(@AuthenticationPrincipal User user,
                                                               @RequestParam(value = "category", required = false) Category category) {
        return ResponseEntity.ok(taskService.getTodayTasks(user.getId(), category));
    }

    @GetMapping("/count-today")
    public ResponseEntity<Map<String, Long>> countTodayTasks(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "category", required = false) Category category) {
        long taskCount = taskService.countTodayTasks(user.getId(), category);
        Map<String, Long> response = new HashMap<>();
        response.put("totalTasks", taskCount);
        return ResponseEntity.ok(response);
    }

}
