package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.TokenVerification;
import com.ifarmr.entity.User;
import com.ifarmr.entity.UserSession;
import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import com.ifarmr.exception.customExceptions.*;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.ResetPasswordRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.*;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.repository.UserSessionRepository;
import com.ifarmr.service.AdminService;
import com.ifarmr.service.EmailService;
import com.ifarmr.service.TokenVerificationService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final JwtService jwtService;
    private final EmailService emailService;
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest servletRequest;
    private final UserSessionRepository userSessionRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TokenVerificationService tokenVerificationService;

    @Override
    public AuthResponse register(RegistrationRequest request, Gender gender) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists, kindly log into your account");
        }

        if (!isValidPassword(request.getPassword())) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one special character.");
        }

        User admin = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.ADMIN)
                .businessName(request.getBusinessName())
                .gender(gender)
                .userName(request.getUserName())
                .displayPhoto(request.getDisplayPhoto())
                .isActive(false)
                .build();

        User savedAdmin = userRepository.save(admin);
        String token = tokenVerificationService.generateVerificationToken(savedAdmin);

        String verificationUrl = "http://localhost:8080/api/v2/admin/verify?token=" + token;
        String emailMessageBody = String.format(
                "Dear %s,\n" +
                        "\n" +
                        "Welcome to iFarmr! We are excited to have you as part of the team dedicated to empowering farmers and transforming agriculture.\n" +
                        "\n" +
                        "To activate your admin account and gain access to your dashboard, please verify your email address by clicking the link below:\n" +
                        "\n" +
                        "Verification Link: %s\n" +
                        "\n" +
                        "If the link doesn’t work, copy and paste the URL into your browser.\n" +
                        "\n" +
                        "Verifying your email ensures the security of your account and access to admin functionalities.\n" +
                        "\n" +
                        "If you did not register as an admin on iFarmr, please disregard this email.\n" +
                        "\n" +
                        "For any assistance, feel free to contact us at support@ifarmr.com.\n" +
                        "\n" +
                        "Thank you for being part of the iFarmr mission!\n" +
                        "\n" +
                        "Best regards,\n" +
                        "iFarmr Team\n",
                savedAdmin.getFirstName(),
                verificationUrl
        );

        EmailDetails registrationToken = EmailDetails.builder()
                .recipient(request.getEmail())
                .subject("Verify Your iFarmr Admin Account")
                .messageBody(emailMessageBody)
                .build();
        emailService.sendEmailToken(registrationToken);

        return AuthResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .registrationInfo(RegistrationInfo.builder()
                        .firstName(savedAdmin.getFirstName())
                        .lastName(savedAdmin.getLastName())
                        .email(savedAdmin.getEmail())
                        .build())
                .token(null)
                .build();

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

        User admin = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + request.getEmail()));

        if (!admin.isActive()) {
            throw new AccountNotVerifiedException("Admin Account not verified. Please check your email.");
        }
        admin.setLastLoginTime(LocalDateTime.now());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtService.generateToken(authentication, admin.getId());

        UserSession session = UserSession.builder()
                .user(admin)
                .loginTime(LocalDateTime.now())
                .build();
        userSessionRepository.save(session);

        return LoginResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE)
                .loginInfo(LoginInfo.builder()
                        .firstName((admin.getFirstName()))
                        .email(admin.getEmail())
                        .token(jwtToken)
                        .build())
                .build();
    }

    @Override
    public AuthResponse updateAdmin(UpdateUserRequestDto request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        if (jwtService.isBlacklisted(token)) {
            throw new SecurityException("The token is invalid or has been blacklisted, Pls log back in");
        }

        String email = jwtService.getUserName(token);

        User existingAdmin = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Admin Account not found"));

        if (existingAdmin != null) {
            Optional.ofNullable(request.getFirstName()).ifPresent(existingAdmin::setFirstName);
            Optional.ofNullable(request.getLastName()).ifPresent(existingAdmin::setLastName);
            Optional.ofNullable(request.getUserName()).ifPresent(existingAdmin::setUserName);
            Optional.ofNullable(request.getBusinessName()).ifPresent(existingAdmin::setBusinessName);
            Optional.ofNullable(request.getDisplayPhoto()).ifPresent(existingAdmin::setDisplayPhoto);

            User savedAdmin = userRepository.save(existingAdmin);

            return AuthResponse.builder()
                    .responseCode(AccountUtils.UPDATE_USER_SUCCESSFUL_CODE)
                    .responseMessage(AccountUtils.UPDATE_USER_SUCCESSFUL_MESSAGE)
                    .registrationInfo(RegistrationInfo.builder()
                            .firstName(savedAdmin.getFirstName())
                            .lastName(savedAdmin.getLastName())
                            .email(savedAdmin.getEmail())
                            .build())
                    .token(null)
                    .build();
        }
        return null;
    }

    @Override
    public String verifyAdmin(String token) {
        TokenVerification verifyToken =
                tokenVerificationService.validateToken(token);


        if (verifyToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token expired. Please register again.");
        }

        User admin = verifyToken.getUser();
        admin.setActive(true);
        userRepository.save(admin);

        tokenVerificationService.deleteToken(verifyToken);

        return "Admin Account Successfully Verified and Activated.";
    }

    @Override
    public String logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid token format");
        }

        String token = authHeader.substring(7);

        Long adminId = jwtService.extractUserIdFromToken(token);
        if (adminId == null) {
            throw new InvalidTokenException("Invalid token: Admin ID not found");
        }
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", adminId));
        admin.setLastLogoutTime(LocalDateTime.now());

        UserSession session = userSessionRepository.findFirstByUserIdOrderByLoginTimeDesc(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Session", adminId));

        session.setLogoutTime(LocalDateTime.now());
        session.setDuration(Duration.between(session.getLoginTime(), session.getLogoutTime()).getSeconds());
        userSessionRepository.save(session);

        jwtService.blacklistToken(token);

        return "Admin Logged Out Successfully";
    }

    @Override
    public Map<String, Long> getFarmAtAGlance() {
        long totalUsers = userRepository.getTotalUsers();
        long activeUsers = userRepository.getActiveUsers();
        long newUsers = userRepository.getNewUsersInLast24Hours(LocalDateTime.now().minusHours(24));

        Map<String, Long> glance = new HashMap<>();
        glance.put("totalUsers", totalUsers);
        glance.put("activeUsers", activeUsers);
        glance.put("newUsers", newUsers);

        return glance;
    }

    @Override
    public List<Long> getWeeklyNewUsers() {
        List<Long> weeklyNewUsers = new ArrayList<>();
        LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);

        for (int i = 0; i < 7; i++) {
            LocalDateTime dayStart = startOfWeek.plusDays(i);
            LocalDateTime dayEnd = dayStart.plusDays(1);
            weeklyNewUsers.add(userRepository.getWeeklyNewUsers(dayStart, dayEnd));
        }

        return weeklyNewUsers;
    }

    @Override
    public List<Long> getWeeklyActiveUsers() {
        List<Long> weeklyActiveUsers = new ArrayList<>();
        LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);

        for (int i = 0; i < 7; i++) {
            LocalDateTime dayStart = startOfWeek.plusDays(i);
            LocalDateTime dayEnd = dayStart.plusDays(1);
            weeklyActiveUsers.add(userRepository.getWeeklyActiveUsers(dayStart, dayEnd));
        }

        return weeklyActiveUsers;
    }

    @Override
    public Map<String, Double> getMonthlyAverageUsageTime(Integer year) {
        int selectedYear = (year != null) ? year : LocalDateTime.now().getYear();

        List<Double[]> results = userRepository.getMonthlyAverageUsageTime(selectedYear);
        Map<String, Double> monthlyUsage = new LinkedHashMap<>();

        for (int i = 1; i <= 12; i++) {
            monthlyUsage.put(getMonthName(i), 0.0);
        }

        for (Double[] result : results) {
            int month = result[0].intValue();
            double averageTime = result[1] != null ? Math.max(result[1], 0.0) : 0.0;
            averageTime = Math.round(averageTime * 100.0) / 100.0;
            monthlyUsage.put(getMonthName(month), averageTime);
        }

        return monthlyUsage;
    }

    @Override
    public Long getCurrentActiveUsers() {
        return userRepository.countCurrentActiveUsers();
    }

    @Override
    public Map<String, Long> getUserDemographics() {
        long male = userRepository.getMaleUserCount();
        long female = userRepository.getFemaleUserCount();

        Map<String, Long> demographics = new HashMap<>();
        demographics.put("male", male);
        demographics.put("female", female);

        return demographics;
    }

    private String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.matches(".*[!@#$%^&*()].*");
    }

}
