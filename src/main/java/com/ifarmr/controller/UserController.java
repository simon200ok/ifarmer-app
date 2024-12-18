package com.ifarmr.controller;


import com.ifarmr.config.JwtService;
import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;
import com.ifarmr.exception.customExceptions.InvalidTokenException;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.response.LoginResponse;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
            (@RequestBody RegistrationRequest request){
        return ResponseEntity.ok(userService.register(request));
    }
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        TokenVerification tokenVerification = tokenVerificationService.validateToken(token);

        if (tokenVerification.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token expired. Please register again.");
        }

        User user = tokenVerification.getUser();
        user.setActive(true);
        userRepository.save(user);

        tokenVerificationService.deleteToken(tokenVerification);

        return ResponseEntity.ok("Account verified successfully! You can now log in.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDto updateUserRequest) {
        User updatedUser = userService.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token format");
        }

        String token = authHeader.substring(7);
        jwtService.blacklistToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }

}
