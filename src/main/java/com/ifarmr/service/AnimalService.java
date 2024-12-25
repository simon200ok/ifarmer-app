package com.ifarmr.service;


import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnimalService {
ResponseEntity<ApiResponse<List<AnimalResponse>>> addAnimal(AnimalRequest animalRequest);

}
