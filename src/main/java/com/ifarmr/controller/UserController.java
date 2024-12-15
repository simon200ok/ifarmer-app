package com.ifarmr.controller;

import com.ifarmr.dto.AuthResponse;
import com.ifarmr.dto.LoginRequestDto;
import com.ifarmr.dto.LoginResponse;
import com.ifarmr.dto.RegistrationRequestDto;
import com.ifarmr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@RequestBody RegistrationRequestDto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

}
