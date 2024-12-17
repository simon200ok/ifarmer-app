package com.ifarmr.service;

import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.response.LoginResponse;

public interface UserService {

    AuthResponse register(RegistrationRequest request);

    LoginResponse login(LoginRequestDto request);
}

