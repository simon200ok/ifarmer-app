package com.ifarmr.service;

import com.ifarmr.entity.enums.Gender;
import com.ifarmr.payload.request.LoginRequestDto;
import com.ifarmr.payload.request.RegistrationRequest;
import com.ifarmr.payload.request.UpdateUserRequestDto;
import com.ifarmr.payload.response.AuthResponse;
import com.ifarmr.payload.response.LoginResponse;


import java.util.List;
import java.util.Map;

public interface AdminService {

    AuthResponse register(RegistrationRequest request, Gender gender);

    LoginResponse login(LoginRequestDto request);

    AuthResponse updateAdmin(UpdateUserRequestDto request);

    String verifyAdmin(String token);

    String logout(String authHeader);

    Map<String, Long> getFarmAtAGlance();

    List<Long> getWeeklyNewUsers();

    List<Long> getWeeklyActiveUsers();

    Map<String, Long> getUserDemographics();

    Map<String, Double> getMonthlyAverageUsageTime(Integer year);

    Long getCurrentActiveUsers();

}
