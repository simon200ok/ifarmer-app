package com.ifarmr.service.impl;

import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;
import com.ifarmr.repository.TokenVerificationRepository;
import com.ifarmr.service.TokenVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenVerificationServiceImpl implements TokenVerificationService {
    private final TokenVerificationRepository tokenVerificationRepository;

    @Transactional
    @Override
    public String generateVerificationToken(User user) {

        tokenVerificationRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        TokenVerification tokenVerification = new TokenVerification();
        tokenVerification.setToken(token);
        tokenVerification.setUser(user);
        tokenVerification.setExpirationTime(LocalDateTime.now().plusHours(24));
        tokenVerificationRepository.save(tokenVerification);
        return token;
    }

    @Override
    public TokenVerification validateToken(String token) {
        return tokenVerificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    @Override
    public void deleteToken(TokenVerification token) {
        tokenVerificationRepository.delete(token);
    }

}

