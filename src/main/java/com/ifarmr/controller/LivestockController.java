package com.ifarmr.controller;

import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/livestock")
@RequiredArgsConstructor
public class LivestockController {

    private final AnimalService animalService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AnimalResponse>> addLivestock(
            @RequestBody AnimalRequest animalRequest) {
        ApiResponse<AnimalResponse> createdLivestock = animalService.addLivestock(animalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLivestock);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnimalResponse>>> getAllAnimals() {
        ApiResponse<List<AnimalResponse>> response = animalService.getAllAnimals();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic/get_animal_by_id")
    public ResponseEntity<ApiResponse<List<AnimalResponse>>> getAnimalsByUserId(@RequestParam Long userId) {
        ApiResponse<List<AnimalResponse>> response = animalService.getAnimalsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
