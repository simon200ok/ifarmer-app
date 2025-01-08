package com.ifarmr.service;

import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.ForgotPasswordResponse;
import com.ifarmr.payload.response.LoginResponse;

import java.util.List;

public interface UserService {

    AuthResponse register(RegistrationRequest request, Gender gender, Roles role);

    LoginResponse login(LoginRequestDto request);

    AuthResponse updateUser(UpdateUserRequestDto request);

    ForgotPasswordResponse generateResetToken(String email);

    List<PostDto> getUserPosts(long id);

    PostDetailsDto getPostDetails(Long postId);

    String verifyUser(String token);

    String logout(String authHeader);

}

