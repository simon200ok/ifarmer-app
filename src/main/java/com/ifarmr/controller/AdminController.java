package com.ifarmr.controller;


import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.response.*;
import com.ifarmr.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CropService cropService;
    private final InventoryService inventoryService;
    private final AnimalService animalService;
    private final TaskService taskService;
    private final AdminService adminService;
    private final UserSessionService userSessionService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register
            (@RequestBody RegistrationRequest request,
             @RequestParam Gender gender){
        return ResponseEntity.ok(adminService.register(request, gender));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAdmin(@RequestParam String token) {
        return ResponseEntity.ok(adminService.verifyAdmin(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(adminService.login(request));
    }

    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateAdmin(@RequestBody UpdateUserRequestDto updateUserRequest) {
        return ResponseEntity.ok(adminService.updateAdmin(updateUserRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(adminService.logout(authHeader));
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


    @GetMapping("/weekly-logins")
    public ResponseEntity<Map<String, Long>> getWeeklyLogins(@RequestParam("startOfWeek") String startOfWeekStr) {
        LocalDateTime startOfWeek = LocalDateTime.parse(startOfWeekStr);
        return ResponseEntity.ok(userSessionService.getWeeklyUserLogins(startOfWeek));
    }


}
