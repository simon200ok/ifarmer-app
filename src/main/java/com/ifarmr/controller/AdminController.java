package com.ifarmr.controller;


import com.ifarmr.entity.User;
import com.ifarmr.payload.request.ForgotPasswordRequest;
import com.ifarmr.payload.response.*;
import com.ifarmr.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CropService cropService;
    private final InventoryService inventoryService;
    private final AnimalService animalService;
    private final TaskService taskService;
    private final AdminService adminService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResponse response = userService.generateResetToken(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/crops")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getAllCrops() {
        ApiResponse<List<CropResponse>> response = cropService.getAllCrops();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/inventory")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/livestock")
    public ResponseEntity<ApiResponse<List<AnimalResponse>>> getAllAnimals() {
        ApiResponse<List<AnimalResponse>> response = animalService.getAllAnimals();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @GetMapping("/glance")
    public ResponseEntity<Map<String, Long>> getFarmAtAGlance() {
        return ResponseEntity.ok(adminService.getFarmAtAGlance());
    }

    @GetMapping("/user-growth")
    public ResponseEntity<List<Long>> getWeeklyNewUsers() {
        return ResponseEntity.ok(adminService.getWeeklyNewUsers());
    }

    @GetMapping("/weekly-active")
    public ResponseEntity<List<Long>> getWeeklyActiveUsers() {
        return ResponseEntity.ok(adminService.getWeeklyActiveUsers());
    }

    @GetMapping("/current-active-users")
    public ResponseEntity<Long> getCurrentActiveUsers() {
        return ResponseEntity.ok(adminService.getCurrentActiveUsers());
    }

    @GetMapping("/average-usage-time")
    public ResponseEntity<Map<String, Double>> getMonthlyAverageUsageTime(
            @RequestParam(value = "year", required = false) Integer year) {
        return ResponseEntity.ok(adminService.getMonthlyAverageUsageTime(year));
    }

    @GetMapping("/user-demographics")
    public ResponseEntity<Map<String, Long>> getUserDemographics() {
        return ResponseEntity.ok(adminService.getUserDemographics());
    }


}
