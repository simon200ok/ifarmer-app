package com.ifarmr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CropResponse>> addCrop(@RequestParam(value = "data") String data,
                                                            @RequestParam(value = "photo") MultipartFile photo) throws JsonProcessingException  {
        CropRequest cropRequest = objectMapper.readValue(data, CropRequest.class);
        ApiResponse<CropResponse> createdCrop = cropService.addCrop(cropRequest, photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCrop);
    }

    @GetMapping("/statistics/get_all_crops_by_user")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getCropsByUserId(@AuthenticationPrincipal User user) {
        ApiResponse<List<CropResponse>> response = cropService.getCropsByUserId(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status-count")
    public ResponseEntity<Map<CropStatus, Long>> getCropsCountByStatus() {
        return ResponseEntity.ok(cropService.getCropsCountByStatus());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCrop(@RequestParam Long cropId) {
        return ResponseEntity.ok(cropService.deleteCrop(cropId));
    }


}
