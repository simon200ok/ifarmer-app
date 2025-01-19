package com.ifarmr.service;

import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.ForgotPasswordResponse;
import com.ifarmr.payload.response.LoginResponse;
import com.ifarmr.payload.response.UserResponse;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface UserService {

    AuthResponse register(RegistrationRequest request, Gender gender);

    LoginResponse login(LoginRequestDto request);

    AuthResponse updateUser(UpdateUserRequestDto request);

    ForgotPasswordResponse generateResetToken(String email);

    List<PostDto> getUserPosts(long id);

    PostDetailsDto getPostDetails(Long postId);

    String verifyUser(String token);

    String logout(String authHeader);

    //ForgotPasswordResponse resetPassword (String token, String newPassword, String confirmPassword);

    List<UserResponse> getAllUsers();

    String deleteUser(Long userId);

    boolean verifyResetToken(String token);

    void resetPassword(String token, String newPassword);

    UserResponse getUserProfile(String jwt) throws Exception;
}

