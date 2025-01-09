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
}
