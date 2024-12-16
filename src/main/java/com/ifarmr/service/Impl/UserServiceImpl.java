package com.ifarmr.service.Impl;

import com.ifarmr.config.JwtService;
import com.ifarmr.dto.AuthResponse;
import com.ifarmr.dto.RegistrationInfo;
import com.ifarmr.dto.RegistrationRequestDto;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
//    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegistrationRequestDto request) {


        if (farmerRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists, kindly log into your account");
        }

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.FARMER)
                .businessName(request.getBusinessName())
                .gender(Gender.valueOf(request.getGender().toUpperCase()))
                .userName(request.getUserName())

                .displayPhoto(request.getDisplayPhoto())

                .build();

        User saveNewUser = farmerRepository.save(newUser);



        return AuthResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .registrationInfo(RegistrationInfo.builder()
                        .firstName(saveNewUser.getFirstName())
                        .lastName(saveNewUser.getLastName())
                        .email(saveNewUser.getEmail())
                        .build())
                .build();
    }
}
