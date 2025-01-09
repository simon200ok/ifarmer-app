package com.ifarmr.service;


import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnimalService {
    ApiResponse<AnimalResponse>  addLivestock(AnimalRequest animalRequest);

    ApiResponse<List<AnimalResponse>> getAllAnimals();

    ApiResponse<List<AnimalResponse>> getAnimalsByUserId(Long userId);

}
