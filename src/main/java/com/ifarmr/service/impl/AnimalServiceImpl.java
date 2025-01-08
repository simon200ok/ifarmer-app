package com.ifarmr.service.impl;

import com.ifarmr.entity.AnimalDetails;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.repository.AnimalDetailsRepository;
import com.ifarmr.service.AnimalService;
import com.ifarmr.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalDetailsRepository animalDetailsRepository;
    private final SecurityUtils securityUtils;



    @Override
    public ApiResponse<AnimalResponse> addLivestock(AnimalRequest animalRequest) {

        User user = securityUtils.getLoggedInUser();
        AnimalDetails livestock = AnimalDetails.builder()
                .animalName(animalRequest.getAnimalName())
                .animalType(animalRequest.getAnimalType())
                .breed(animalRequest.getBreed())
                .quantity(animalRequest.getQuantity())
                .age(animalRequest.getAge())
                .location(animalRequest.getLocation())
                .animalStatus(animalRequest.getAnimalStatus())
                .feedingSchedule(animalRequest.getFeedingSchedule())
                .wateringFrequency(animalRequest.getWateringFrequency())
                .vaccinationSchedule(animalRequest.getVaccinationSchedule())
                .healthIssues(animalRequest.getHealthIssues())
                .description(animalRequest.getDescription())
                .photoFilePath(animalRequest.getPhotoFilePath())
                .user(user)
                .build();

        return new ApiResponse<>("Livestock Successfully added", mapToResponse(animalDetailsRepository.save(livestock)));
    }

    private AnimalResponse mapToResponse(AnimalDetails animalDetails) {
        return AnimalResponse.builder()
                .id(animalDetails.getId())
                .animalName(animalDetails.getAnimalName())
                .animalType(animalDetails.getAnimalType())
                .breed(animalDetails.getBreed())
                .quantity(animalDetails.getQuantity())
                .age(animalDetails.getAge())
                .location(animalDetails.getLocation())
                .animalStatus(animalDetails.getAnimalStatus())
                .feedingSchedule(animalDetails.getFeedingSchedule())
                .wateringFrequency(animalDetails.getWateringFrequency())
                .vaccinationSchedule(animalDetails.getVaccinationSchedule())
                .healthIssues(animalDetails.getHealthIssues())
                .description(animalDetails.getDescription())
                .photoFilePath(animalDetails.getPhotoFilePath())
                .build();
    }
}
