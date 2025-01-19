package com.ifarmr.service;

import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.ForgotPasswordResponse;
import com.ifarmr.payload.response.LoginResponse;
import com.ifarmr.payload.response.UserResponse;

import java.util.List;

public interface UserService {

    AuthResponse register(RegistrationRequest request, Gender gender);

    LoginResponse login(LoginRequestDto request);

    AuthResponse updateUser(UpdateUserRequestDto request);

    String verifyUser(String token);

    String logout(String authHeader);

    List<UserResponse> getAllUsers();

    String deleteUser(Long userId);

    ForgotPasswordResponse generateResetToken(String email);

    void resetPassword(String token, ResetPasswordRequest request);
}

