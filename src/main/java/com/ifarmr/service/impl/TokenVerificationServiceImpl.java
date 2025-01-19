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
        return tokenVerificationRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));
    }

    @Override
    public void deleteToken(TokenVerification token) {
        token.setRevoked(true);
        tokenVerificationRepository.save(token);
        System.out.println("Token with ID " + token.getId() + " marked as revoked.");

        tokenVerificationRepository.delete(token);
    }

}

