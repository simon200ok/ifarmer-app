package com.ifarmr.service;

import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CropService {

    ApiResponse<CropResponse> addCrop(CropRequest cropRequest, MultipartFile photo);

    public ApiResponse<List<CropResponse>> getAllCrops();

    ApiResponse<List<CropResponse>> getCropsByUserId(Long userId);

    List<CropResponse> getCropsForUser(long userId);

    Map<CropStatus, Long> getCropsCountByStatus();

    String deleteCrop(Long cropId);
}
