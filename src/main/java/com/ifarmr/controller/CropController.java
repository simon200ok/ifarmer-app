package com.ifarmr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.entity.enums.CropType;
import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.service.CropService;
import jakarta.validation.Valid;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(cropService.addCrop(cropRequest, photo));
    }

    @GetMapping("/statistics/get_all_crops_by_user")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getCropsByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cropService.getCropsByUserId(user.getId()));
    }

    @GetMapping("/status-count")
    public ResponseEntity<Map<CropStatus, Long>> getCropsCountByStatus(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cropService.getCropsCountByStatus(user.getId()));
    }

    @GetMapping("/type-count")
    public ResponseEntity<Map<CropType, Long>> getCropsCountByType(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cropService.getCropsCountByType(user.getId()));
    }

    @GetMapping("/type")
    public ResponseEntity<List<CropResponse>> getCropByType(@AuthenticationPrincipal User user,
                                                                   @RequestParam CropType type) {
        return ResponseEntity.ok(cropService.getCropByType(user.getId(), type));
    }

    @GetMapping("/status")
    public ResponseEntity<List<CropResponse>> getCropByStatus(@AuthenticationPrincipal User user,
                                                             @RequestParam CropStatus status) {
        return ResponseEntity.ok(cropService.getCropByStatus(user.getId(), status));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalCropNumber(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cropService.getTotalCropNumber(user.getId()));
    }

    @PutMapping(path = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CropResponse> updateAnimal(@RequestPart("data") @Valid String data,
                                                       @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                       @RequestParam Long cropId,
                                                       @AuthenticationPrincipal User user) throws JsonProcessingException {
        CropRequest cropRequest = objectMapper.readValue(data, CropRequest.class);
        return ResponseEntity.ok(cropService.updateCrop(cropRequest, photo, cropId, user.getId()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCrop(@AuthenticationPrincipal User user,
                                             @RequestParam Long cropId) {
        return ResponseEntity.ok(cropService.deleteCrop(user.getId(), cropId));
    }


}
