package com.ifarmr.controller;


import com.ifarmr.entity.User;
import com.ifarmr.payload.response.TotalResourcesDTO;
import com.ifarmr.service.ResourceService;
import com.ifarmr.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/total")
    public ResponseEntity<TotalResourcesDTO> getTotalResources(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(resourceService.getTotalResources(user.getId()));
    }
}

