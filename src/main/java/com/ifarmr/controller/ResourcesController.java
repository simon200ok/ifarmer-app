package com.ifarmr.controller;

import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.service.AnimalService;
import com.ifarmr.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class ResourcesController {
    private final AnimalService animalService;
    private final CropService cropService;

    @GetMapping("/resources")
    public ApiResponse<Map<String, List<?>>> getUserFarmDetails() {
        List<AnimalResponse> livestock = animalService.getLivestockForUser();
        List<CropResponse> crops = cropService.getCropsForUser();

        Map<String, List<?>> details = new HashMap<>();
        details.put("livestock", livestock);
        details.put("crops", crops);

        return new ApiResponse<>("Resources retrieved successfully", details);
    }
}
