package com.ifarmr.service.impl;

import com.ifarmr.entity.Post;
import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;
import com.ifarmr.entity.UserSession;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.exception.customExceptions.AccountNotVerifiedException;
import com.ifarmr.exception.customExceptions.EmailAlreadyExistsException;
import com.ifarmr.exception.customExceptions.InvalidPasswordException;
import com.ifarmr.exception.customExceptions.InvalidTokenException;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.response.*;
import com.ifarmr.repository.PostRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.repository.UserSessionRepository;
import com.ifarmr.service.EmailService;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.service.UserService;
import com.ifarmr.utils.AccountUtils;
import com.ifarmr.utils.ExtractUserID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    private final UserSessionRepository userSessionRepository;
    private final PostRepository postRepository;

    @Override
    public AuthResponse register(RegistrationRequest request, Gender gender) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists, kindly log into your account");
        }

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
                .gender(gender)
                .userName(request.getUserName())
                .displayPhoto(request.getDisplayPhoto())
                .isActive(false)

                .build();

        User savedUser = userRepository.save(newUser);
        String token = tokenVerificationService.generateVerificationToken(savedUser);


        String verificationUrl = "http://localhost:8080/api/v1/auth/verify?token=" + token;
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

    private boolean isValidPassword(String password) {
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

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        if (!user.isActive()) {
            throw new AccountNotVerifiedException("Account not verified. Please check your email.");
        }
        user.setLastLoginTime(LocalDateTime.now());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtService.generateToken(authentication, user.getId());

        UserSession session = UserSession.builder()
                .user(user)
                .loginTime(LocalDateTime.now())
                .build();

        userSessionRepository.save(session);

        return LoginResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE)
                .loginInfo(LoginInfo.builder()
                        .firstName((user.getFirstName()))
                        .email(user.getEmail())
                        .token(jwtToken)
                        .build())
                .build();
    }

    @Override
    public AuthResponse updateUser(UpdateUserRequestDto request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        if (jwtService.isBlacklisted(token)) {
            throw new SecurityException("The token is invalid or has been blacklisted, Pls log back in");
        }

        String email = jwtService.getUserName(token);

        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        if (existingUser != null) {
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
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFirstName() +" "+ user.getLastName(),
                        user.getBusinessName(),
                        user.getEmail(),
                        user.getGender(),
                        user.getCreatedAt(),
                        user.getLastLogoutTime()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        userSessionRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
        return "User with ID " + userId + " has been deleted successfully.";
    }

    @Override
    public ForgotPasswordResponse generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email"));

        String token = tokenVerificationService.generateVerificationToken(user);
        String resetUrl = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;

        String emailMessageBody = String.format(
                """
                        Dear %s,

                        We received a request to reset your password.
                        If you did not make this request, you can safely ignore this email.

                        To reset your password, click the link below or copy and paste it into your browser:

                        %s

                        This link will expire in 24 hours.

                        If you have any questions, feel free to contact our support team.

                        Thank you,
                        The iFarmr Team
                        """,
                user.getFirstName(), resetUrl
        );
        EmailDetails resetEmail = EmailDetails.builder()
                .recipient(email)
                .subject("Reset Your iFarmr Password")
                .messageBody(emailMessageBody)
                .build();
        emailService.forgetPasswordAlert(resetEmail);

        return ForgotPasswordResponse.builder()
                .responseCode(AccountUtils.FORGOT_PASSWORD_SUCCESS_CODE)
                .responseMessage(AccountUtils.FORGOT_PASSWORD_SUCCESS_MESSAGE)
                .build();

    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
        TokenVerification resetToken = tokenVerificationService.validateToken(token);

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenVerificationService.deleteToken(resetToken);
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

        Long userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            throw new InvalidTokenException("Invalid token: User ID not found");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setLastLogoutTime(LocalDateTime.now());

        UserSession session = userSessionRepository.findFirstByUserIdOrderByLoginTimeDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Session", userId));

        session.setLogoutTime(LocalDateTime.now());
        session.setDuration(Duration.between(session.getLoginTime(), session.getLogoutTime()).getSeconds());
        userSessionRepository.save(session);

        jwtService.blacklistToken(token);

        return "Logged Out Successfully";
    }

}
