package com.ifarmr.service;

import com.ifarmr.payload.response.UserResponse;

import java.util.List;
import java.util.Map;

public interface AdminService {
    Map<String, Long> getFarmAtAGlance();

    List<Long> getWeeklyNewUsers();

    List<Long> getWeeklyActiveUsers();

    Map<String, Long> getUserDemographics();

    Map<String, Double> getMonthlyAverageUsageTime(Integer year);

    Long getCurrentActiveUsers();
}
