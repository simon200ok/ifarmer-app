package com.ifarmr.service.impl;

import com.ifarmr.config.JwtService;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.exception.customExceptions.AccountNotVerifiedException;
import com.ifarmr.exception.customExceptions.EmailAlreadyExistsException;
import com.ifarmr.exception.customExceptions.InvalidPasswordException;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.*;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.EmailService;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenVerificationService tokenVerificationService;

    @Override
    public AuthResponse register(RegistrationRequest request, Gender gender, Roles role) {

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
                .role(role)
                .businessName(request.getBusinessName())
                .gender(gender)
                .userName(request.getUserName())
                .displayPhoto(request.getDisplayPhoto())
                .isActive(false)

                .build();

        //save the user to the database
        User savedUser = userRepository.save(newUser);

        // Generate JWT Token for the registered User
        String token = tokenVerificationService.generateVerificationToken(savedUser);


        // URL for token verification
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify?token=" + token;
        // Send an email containing the token
        String emailMessageBody = String.format(
                "Dear %s \n" +
                        "\n" +
                        "Thank you for registering on iFarmr, the platform that connects and empowers farmers worldwide. To complete your registration, please verify your email address by clicking the link below:\n" +
                        "\n" +
                        "Verification Link: %s\n" +
                        "\n" +
                        "If the link doesn’t work, copy and paste the URL into your browser.\n" +
                        "\n" +
                        "This step helps us ensure the security of your account. \n" +
                        "\n" +
                        "If you did not create an account on iFarmr, please disregard this email.\n" +
                        "\n" +
                        "For any assistance, feel free to reach out to us at support@ifarmr.com.\n" +
                        "\n" +
                        "Welcome to the iFarmr community!\n" +
                        "\n" +
                        "Best regards,\n" +
                        "iFarmr Team\n",
                savedUser.getFirstName(),
                verificationUrl
        );

        EmailDetails sendTokenForRegistration = EmailDetails.builder()
                .recipient(request.getEmail())
                .subject("Verify Your iFarmr Account")
                .messageBody(emailMessageBody)
                .build();
        emailService.sendEmailToken(sendTokenForRegistration);



        //Build and return the response
        return AuthResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .registrationInfo(RegistrationInfo.builder()
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .email(savedUser.getEmail())
                        .build())
                .token(null)
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

        if (!user.isActive()) {
            throw new AccountNotVerifiedException("Account not verified. Please check your email.");
        }

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
    // Method to update user details
    @Override
    public User updateUser(Long userId, UpdateUserRequestDto request) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update fields if provided in the request
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getEmail() != null) {
            // Check if the new email is unique
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent() && existingUser.get().getId() !=userId) {
                throw new RuntimeException("Email already exists, please choose another one");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            // Encode the password before saving it
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getBusinessName() != null) {
            user.setBusinessName(request.getBusinessName());
        }
        if (request.getDisplayPhoto() != null) {
            user.setDisplayPhoto(request.getDisplayPhoto());
        }

        // Save the updated user to the database
        return userRepository.save(user);
    }

    @Override
    public ForgotPasswordResponse generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Ensure the user is an admin
        if (!Roles.ADMIN.equals(user.getRole())) {
            throw new IllegalArgumentException("User is not an admin");
        }

        // Generate JWT reset token
        String resetToken = jwtService.generateToken(user);

        // Update user with reset token and expiry
        user.setResetPasswordToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        return ForgotPasswordResponse.builder()
                .responseCode(AccountUtils.FORGOT_PASSWORD_SUCCESS_CODE)
                .responseMessage(AccountUtils.FORGOT_PASSWORD_SUCCESS_MESSAGE)
                .build();

//        return new ForgotPasswordResponse(resetToken, "Reset token generated successfully.");
    }


}
