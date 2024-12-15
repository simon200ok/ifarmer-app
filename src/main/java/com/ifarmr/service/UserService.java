package com.ifarmr.service;

import com.ifarmr.dto.LoginRequestDto;
import com.ifarmr.dto.LoginResponse;

public interface UserService {

    LoginResponse login(LoginRequestDto request);
}

