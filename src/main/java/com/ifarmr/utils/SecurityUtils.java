package com.ifarmr.utils;

import com.ifarmr.entity.User;
import com.ifarmr.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * @Author Freedom
     * @return current logged in user after reading the user details from Security context holder
     *
     */
    public User getLoggedInUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userEmail =  userDetails.getUsername();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found: " + userEmail));
    }
}
