package com.ifarmr.controller;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.service.CropService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CropResponse>> addCrop(
            @RequestBody CropRequest cropRequest){
        ApiResponse<CropResponse> createdCrop = cropService.addCrop(cropRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCrop);
    }

    @GetMapping("/get_all_crops")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getAllCrops() {
        ApiResponse<List<CropResponse>> response = cropService.getAllCrops();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/get_all_crops_by_user")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getCropsByUserId(@PathVariable Long userId) {
        ApiResponse<List<CropResponse>> response = cropService.getCropsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
