package com.ifarmr.service;

import com.ifarmr.dto.AuthResponse;
import com.ifarmr.dto.RegistrationRequestDto;

public interface UserService {

    AuthResponse register(RegistrationRequestDto request);
}
