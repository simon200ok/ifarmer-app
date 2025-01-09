package com.ifarmr.service;


import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnimalService {
    ApiResponse<AnimalResponse>  addLivestock(AnimalRequest animalRequest, MultipartFile photo);

}
