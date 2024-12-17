package com.ifarmr.service;

import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.LoginResponse;

public interface UserService {

    AuthResponse register(RegistrationRequest request, Gender gender);

    LoginResponse login(LoginRequestDto request);

    User updateUser(Long userId, UpdateUserRequestDto request);

}

