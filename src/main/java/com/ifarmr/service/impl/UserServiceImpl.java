package com.ifarmr.service.impl;

import com.ifarmr.config.JwtService;
import com.ifarmr.dto.*;
import com.ifarmr.entity.Farmer;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.repository.FarmerRepository;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegistrationRequestDto request) {

        if(farmerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email exists!");
        }

        Farmer newFarmer = Farmer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.FARMER)
                .build();

        farmerRepository.save(newFarmer);

        return null;
    }

    @Override
    public LoginResponse login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Farmer farmer = farmerRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(farmer);

        return LoginResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE)
                .loginInfo(LoginInfo.builder()
                        .email(farmer.getEmail())
                        .token(jwtToken)
                        .build())
                .build();
    }
}
