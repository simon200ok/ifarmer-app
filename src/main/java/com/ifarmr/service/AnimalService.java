package com.ifarmr.service;


import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnimalService {
    ApiResponse<AnimalResponse>  addLivestock(AnimalRequest animalRequest, MultipartFile photo);

    ApiResponse<List<AnimalResponse>> getAllAnimals();

    ApiResponse<List<AnimalResponse>> getAnimalsByUserId(Long userId);

    List<AnimalResponse> getLivestockForUser(long userId);

    Long getTotalLivestockNumber(Long userId);

    List<AnimalResponse> getLivestockByType(Long userId, AnimalType type);

    List<AnimalResponse> getLivestockByStatus(Long userId, AnimalStatus status);

    Long getLivestockCountByType(Long userId, AnimalType type);

    Long getLivestockCountByStatus(Long userId, AnimalStatus status);

    AnimalResponse updateAnimal(AnimalRequest request, MultipartFile photo, Long animalId, Long userId);

    String deleteAnimal(Long userId, Long id);



    ApiResponse<AnimalResponse> updateLivestock(Long animalId, AnimalRequest animalRequest);

}
