package com.ifarmr.service;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.entity.enums.CropType;
import com.ifarmr.payload.request.AnimalRequest;
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

    List<CropResponse> getCropsForUser(Long userId);

    Map<CropStatus, Long> getCropsCountByStatus(Long userId);

    String deleteCrop(Long userId, Long cropId);

    Map<CropType, Long> getCropsCountByType(Long userId);

    Long getTotalCropNumber(Long userId);

    List<CropResponse> getCropByType(Long userId, CropType type);

    List<CropResponse> getCropByStatus(Long userId, CropStatus status);

    CropResponse updateCrop(CropRequest cropRequest, MultipartFile photo, Long cropId, Long userId);
}
