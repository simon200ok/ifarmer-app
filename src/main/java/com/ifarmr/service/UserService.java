package com.ifarmr.service;

import com.ifarmr.dto.AuthResponse;
import com.ifarmr.dto.LoginRequestDto;
import com.ifarmr.dto.LoginResponse;
import com.ifarmr.dto.RegistrationRequestDto;

public interface UserService {

    AuthResponse register(RegistrationRequestDto request);

    LoginResponse login(LoginRequestDto request);
}

