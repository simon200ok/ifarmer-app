//package com.ifarmr.controller;
//
//import com.ifarmr.payload.response.AnimalResponse;
//import com.ifarmr.payload.response.ApiResponse;
//import com.ifarmr.payload.response.CropResponse;
//import com.ifarmr.service.AnimalService;
//import com.ifarmr.service.CropService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1user")
//@RequiredArgsConstructor
//
//public class ResourcesController {
//    private final AnimalService animalService;
//    private final CropService cropService;
//
//    @GetMapping("/resources")
//    public ApiResponse<Map<String, List<?>>> getUserDetailedResources() {
//        List<AnimalResponse> livestock = animalService.getLivestockForUser(userId);
//        List<CropResponse> crops = cropService.getCropsForUser(userId);
//
//        Map<String, List<?>> detailedResources = new HashMap<>();
//        detailedResources.put("livestock", livestock);
//        detailedResources.put("crops", crops);
//
//        return new ApiResponse<>("Resources retrieved successfully", detailedResources);
//    }
//}
