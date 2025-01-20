package com.ifarmr.service;

import com.ifarmr.entity.User;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.impl.TokenVerificationServiceImpl;
import com.ifarmr.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserServiceImpl userServiceimpl;

    private final PasswordEncoder passwordEncoder;

//    public void sendPasswordResetEmail(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (user.isPresent()) {
//            userServiceimpl.generateResetToken(email);
//        }
//    }
//
//    public void resetPassword(String token, String newPassword) {
//        Optional<User> user = userRepository.findByResetToken(token);
//        if (user.isPresent()) {
//            user.get().setPassword(passwordEncoder.encode(newPassword));
//            user.get().setResetPasswordToken(null);
//            userRepository.save(user.get());
//
//        } else {
//            throw new IllegalArgumentException("Invalid token.");
//        }
//    }
}
