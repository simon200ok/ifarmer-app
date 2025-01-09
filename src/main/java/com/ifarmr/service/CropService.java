package com.ifarmr.service;

import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CropService {

    ApiResponse<CropResponse> addCrop(CropRequest cropRequest);

    public ApiResponse<List<CropResponse>> getAllCrops();

    ApiResponse<List<CropResponse>> getCropsByUserId(Long userId);
}
