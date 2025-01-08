package com.ifarmr.service;

import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;

public interface TokenVerificationService {
    String generateVerificationToken(User user);
    TokenVerification validateToken(String token);

    void deleteToken(TokenVerification token);
}

