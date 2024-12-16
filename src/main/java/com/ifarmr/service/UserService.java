package com.ifarmr.service;

import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.request.RegistrationRequest;

public interface UserService {

    AuthResponse register(RegistrationRequest request);
    LoginResponse login(LoginRequestDto request);
}

