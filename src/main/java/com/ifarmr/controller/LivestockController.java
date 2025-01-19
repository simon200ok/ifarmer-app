package com.ifarmr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/livestock")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class LivestockController {

    private final AnimalService animalService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AnimalResponse>> addLivestock(@RequestPart("data") @Valid String data,
                                                                    @RequestPart("photo") MultipartFile photo) throws JsonProcessingException {
        AnimalRequest animalRequest = objectMapper.readValue(data, AnimalRequest.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.addLivestock(animalRequest, photo));
    }

    @GetMapping("/statistic/get_animal_by_id")
    public ResponseEntity<ApiResponse<List<AnimalResponse>>> getAnimalsByUserId(@AuthenticationPrincipal User user) {
        ApiResponse<List<AnimalResponse>> response = animalService.getAnimalsByUserId(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic/total")
    public ResponseEntity<Long> getTotalLivestockNumber(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(animalService.getTotalLivestockNumber(user.getId()));
    }

    @GetMapping("/statistic/type")
    public ResponseEntity<List<AnimalResponse>> getLivestockByType(@AuthenticationPrincipal User user,
                                                                   @RequestParam AnimalType type) {
        return ResponseEntity.ok(animalService.getLivestockByType(user.getId(), type));
    }

    @GetMapping("/statistic/status")
    public ResponseEntity<List<AnimalResponse>> getLivestockByStatus(@AuthenticationPrincipal User user,
                                                                     @RequestParam AnimalStatus status) {
        return ResponseEntity.ok(animalService.getLivestockByStatus(user.getId(), status));
    }

    @GetMapping("/statistic/count/type")
    public ResponseEntity<Long> getLivestockCountByType(@AuthenticationPrincipal User user,
                                                        @RequestParam AnimalType type) {
        return ResponseEntity.ok(animalService.getLivestockCountByType(user.getId(), type));
    }

    @GetMapping("/statistic/count/status")
    public ResponseEntity<Long> getLivestockCountByStatus(@AuthenticationPrincipal User user,
                                                          @RequestParam AnimalStatus status) {
        return ResponseEntity.ok(animalService.getLivestockCountByStatus(user.getId(), status));
    }

    @PutMapping(path = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnimalResponse> updateAnimal(@RequestPart("data") @Valid String data,
                                                       @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                       @RequestParam Long animalId,
                                                       @AuthenticationPrincipal User user) throws JsonProcessingException {
        AnimalRequest animalRequest = objectMapper.readValue(data, AnimalRequest.class);
        return ResponseEntity.ok(animalService.updateAnimal(animalRequest, photo, animalId, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnimal(@AuthenticationPrincipal User user,
                                               @PathVariable Long id) {
        return ResponseEntity.ok(animalService.deleteAnimal(user.getId(), id));
    }

    @PatchMapping("/updateLivestock")
    public ResponseEntity<ApiResponse<AnimalResponse>> updateLivestock(@RequestParam Long animalId, @RequestPart("data") @Valid String data) throws JsonProcessingException {
        AnimalRequest animalRequest = objectMapper.readValue(data, AnimalRequest.class);
        ApiResponse<AnimalResponse> updatedLivestock = animalService.updateLivestock(animalId, animalRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedLivestock);
    }
}
