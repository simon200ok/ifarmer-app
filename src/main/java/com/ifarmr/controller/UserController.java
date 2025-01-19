package com.ifarmr.controller;


import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.ForgotPasswordResponse;
import com.ifarmr.payload.response.LoginResponse;
import com.ifarmr.payload.response.UserResponse;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getuserprofile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "").trim();
            System.out.println("Extracted JWT: " + jwt);
            UserResponse userProfile = userService.getUserProfile(jwt);
            if (userProfile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User profile not found.");
            }
            System.out.println(userProfile);
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.logout(authHeader));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getUserPosts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserPosts(user.getId()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailsDto> getPostDetails(@PathVariable Long postId) {
        return ResponseEntity.ok(userService.getPostDetails(postId));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(userService.generateResetToken(request.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ForgotPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(ForgotPasswordResponse.builder()
                .responseCode(AccountUtils.FORGOT_PASSWORD_SUCCESS_CODE)
                .responseMessage(AccountUtils.RESET_PASSWORD_SUCCESS_MESSAGE)
                .build());
    }

    @PostMapping("/verify-reset-token")
    public ResponseEntity<String> verifyResetToken(@RequestBody TokenVerification token) {
        boolean isValid = userService.verifyResetToken(token.getToken());
        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }
}

