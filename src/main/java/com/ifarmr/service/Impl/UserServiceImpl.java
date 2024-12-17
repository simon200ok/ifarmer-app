package com.ifarmr.service.Impl;

import com.ifarmr.config.JwtService;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.exception.customExceptions.EmailAlreadyExistsException;
import com.ifarmr.exception.customExceptions.InvalidPasswordException;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.response.LoginInfo;
import com.ifarmr.payload.response.LoginResponse;
import com.ifarmr.payload.response.RegistrationInfo;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegistrationRequest request) {

        //check if Email Already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists, kindly log into your account");
        }

        // Validate the password
        if (!isValidPassword(request.getPassword())) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one special character.");
        }

        // Create and save the user
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

        //save the user to the database
        User savedUser = userRepository.save(newUser);

        // Generate JWT Token for the registered User
        String token = jwtService.generateToken(savedUser);


        //Build and return the response
        return AuthResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .registrationInfo(RegistrationInfo.builder()
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .email(savedUser.getEmail())
                        .build())
                .token(token) // i included the token in this response
                .build();
    }

    // Helper method to validate the password
    private boolean isValidPassword(String password) {
        // Example validation: Minimum 8 characters, at least one special character
        return password != null && password.length() >= 8 && password.matches(".*[!@#$%^&*()].*");
    }

    @Override
    public LoginResponse login(LoginRequestDto request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            return LoginResponse.builder()
                    .responseCode(AccountUtils.LOGIN_FAILED_CODE)
                    .responseMessage(AccountUtils.LOGIN_FAILED_MESSAGE)
                    .build();
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE)
                .loginInfo(LoginInfo.builder()
                        .email(user.getEmail())
                        .token(jwtToken)
                        .build())
                .build();
    }
}
