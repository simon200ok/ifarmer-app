package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.exception.customExceptions.AccountNotVerifiedException;
import com.ifarmr.exception.customExceptions.EmailAlreadyExistsException;
import com.ifarmr.exception.customExceptions.InvalidPasswordException;
import com.ifarmr.exception.customExceptions.InvalidTokenException;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.*;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.EmailService;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenVerificationService tokenVerificationService;
    private final HttpServletRequest servletRequest;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
        Authentication authentication = null;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Fetch the user from the database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // Check if the user is active (verified)
        if (!user.isActive()) {
            throw new AccountNotVerifiedException("Account not verified. Please check your email.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtService.generateToken(authentication, user.getId());


        // Return login response
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
    public AuthResponse updateUser(UpdateUserRequestDto request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        if (jwtService.isBlacklisted(token)) {
            throw new SecurityException("The token is invalid or has been blacklisted, Pls log back in");
        }


        String email = jwtService.getUserName(token);

        User existingUser = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Account not found"));

        if (existingUser != null){
            Optional.ofNullable(request.getFirstName()).ifPresent(existingUser::setFirstName);
            Optional.ofNullable(request.getLastName()).ifPresent(existingUser::setLastName);
            Optional.ofNullable(request.getUserName()).ifPresent(existingUser::setUserName);
            Optional.ofNullable(request.getBusinessName()).ifPresent(existingUser::setBusinessName);
            Optional.ofNullable(request.getDisplayPhoto()).ifPresent(existingUser::setDisplayPhoto);



            User savedUser = userRepository.save(existingUser);

            return AuthResponse.builder()
                    .responseCode(AccountUtils.UPDATE_USER_SUCCESSFUL_CODE)
                    .responseMessage(AccountUtils.UPDATE_USER_SUCCESSFUL_MESSAGE)
                    .registrationInfo(RegistrationInfo.builder()
                            .firstName(savedUser.getFirstName())
                            .lastName(savedUser.getLastName())
                            .email(savedUser.getEmail())
                            .build())
                    .token(null)
                    .build();
        }
        return null;
    }

    @Override
    public ForgotPasswordResponse generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (!Roles.ADMIN.equals(user.getRole())) {
            throw new IllegalArgumentException("User is not an admin");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        String resetToken = jwtService.generateToken(authentication, user.getId());


        // Update user with reset token and expiry
        user.setResetPasswordToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String forgetPasswordToken = resetToken;
        String forgetPasswordUrl = "http://localhost:8080/api/v1/admin/forgot-password" + resetToken;
        String emailForgetPassword = String.format(
                "Dear %s,\n" +
                        "\n" +
                        "It seems you requested to reset your password for your iFarmr account. No worries—we’re here to help!\n" +
                        "\n" +
                        "Click the link below to create a new password:\n" +
                        "\n" +
                        "Reset Password Link: %s\n" +
                        "\n" +
                        "If the link doesn’t work, copy and paste the URL into your browser.\n" +
                        "\n" +
                        "For further support, feel free to reach us at support@ifarmr.com.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "iFarmr Team\n" +
                        "Let me know if you need any changes!",
                user.getFirstName(),
                forgetPasswordUrl

        );

        EmailDetails forgetPasswordAlert = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Forgot Your iFarmr Password")
                .messageBody(emailForgetPassword)
                .build();
        emailService.forgetPasswordAlert(forgetPasswordAlert);


        return ForgotPasswordResponse.builder()
                .responseCode(AccountUtils.FORGOT_PASSWORD_SUCCESS_CODE)
                .responseMessage(AccountUtils.FORGOT_PASSWORD_SUCCESS_MESSAGE)
                .build();

    }


    @Override
    public String verifyUser(String token) {
        TokenVerification verificationToken = tokenVerificationService.validateToken(token);


        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token expired. Please register again.");
        }

        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        tokenVerificationService.deleteToken(verificationToken);

        return "User account successfully verified and activated.";
    }

    @Override
    public String logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid token format");
        }

        String token = authHeader.substring(7);
        jwtService.blacklistToken(token);

        return "Logged Out Successfully";
    }


//    private void validateEmailUniqueness(Long userId, String email) {
//        userRepository.findByEmail(email).ifPresent(existingUser -> {
//            if (!Objects.equals(existingUser.getId(), userId)) {
//                throw new EmailAlreadyExistsException("Email already exists, please choose another one");
//            }
//        });
//    }


}
