package com.ifarmr.controller;


import com.ifarmr.payload.response.TotalResourcesDTO;
import com.ifarmr.service.ResourceService;
import com.ifarmr.payload.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/total")
    public ResponseEntity<?> getTotalResources(@AuthenticationPrincipal Long userId) {
        try {
            TotalResourcesDTO totalResources = resourceService.getTotalResources(userId);
            return ResponseEntity.ok(new ApiResponse<>());

//                    new ApiResponse(true, totalResources, "Successfully fetched total resources.")
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse(false, null, "An error occurred while fetching resources."));
        }
    }
}

