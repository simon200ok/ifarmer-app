package com.ifarmr.controller;


import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.LoginResponse;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenVerificationService tokenVerificationService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register
            (@RequestBody RegistrationRequest request,
             @RequestParam Gender gender,
             @RequestParam Roles role){
        return ResponseEntity.ok(userService.register(request, gender, role));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyUser(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody UpdateUserRequestDto updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.logout(authHeader));
    }


}

