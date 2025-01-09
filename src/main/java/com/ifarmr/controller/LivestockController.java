package com.ifarmr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.service.AnimalService;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/livestock")
@RequiredArgsConstructor
public class LivestockController {

    private final AnimalService animalService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AnimalResponse>> addLivestock(@RequestPart("data") @Valid String data,
                                                                    @RequestPart("photo") MultipartFile photo) throws JsonProcessingException {
        AnimalRequest animalRequest = objectMapper.readValue(data, AnimalRequest.class);
        ApiResponse<AnimalResponse> createdLivestock = animalService.addLivestock(animalRequest, photo);
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
