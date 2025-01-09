package com.ifarmr.service;


import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;

import java.util.List;

public interface AnimalService {
    ApiResponse<AnimalResponse>  addLivestock(AnimalRequest animalRequest);

    List<AnimalResponse> getLivestockForUser(Long userId);
}
