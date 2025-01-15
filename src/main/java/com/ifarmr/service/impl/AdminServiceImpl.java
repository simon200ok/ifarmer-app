package com.ifarmr.service.impl;

import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private  final UserRepository userRepository;

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

    private String getMonthName(int month) {  //numeric month to name
        return new DateFormatSymbols().getMonths()[month - 1];
    }

}
