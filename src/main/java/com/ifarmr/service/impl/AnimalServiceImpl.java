package com.ifarmr.service.impl;

import com.ifarmr.entity.AnimalDetails;
import com.ifarmr.entity.User;
import com.ifarmr.exception.customExceptions.DuplicateMerchandiseException;
import com.ifarmr.payload.request.AnimalRequest;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.repository.AnimalDetailsRepository;
import com.ifarmr.service.AnimalService;
import com.ifarmr.service.CloudinaryService;
import com.ifarmr.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalDetailsRepository animalDetailsRepository;
    private final SecurityUtils securityUtils;
    private final CloudinaryService cloudinaryService;



    @Override
    public ApiResponse<AnimalResponse> addLivestock(AnimalRequest animalRequest, MultipartFile photo) {

        User user = securityUtils.getLoggedInUser();

        boolean animalExists = animalDetailsRepository.existsByAnimalNameAndUser(animalRequest.getAnimalName(), user);
        if (animalExists) {
            throw new DuplicateMerchandiseException("Animal with the name '"+ animalRequest.getAnimalName() +"' already exists for this user");
        }

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
                .photoFilePath(cloudinaryService.uploadFile(photo))
                .user(user)
                .build();

        return new ApiResponse<>("Livestock Successfully added", mapToResponse(animalDetailsRepository.save(livestock)));
    }

    @Override
    public ApiResponse<List<AnimalResponse>> getAllAnimals() {

        User loggedInUser = securityUtils.getLoggedInUser();


        List<AnimalResponse> animals = animalDetailsRepository.findByUserId(loggedInUser.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new ApiResponse<>("Successfully retrieved animals for the user", animals);
    }


    @Override
    public ApiResponse<List<AnimalResponse>> getAnimalsByUserId(Long userId) {
        User loggedInUser = securityUtils.getLoggedInUser();


        if (userId == null || userId.equals(loggedInUser.getId())) {
            List<AnimalResponse> animals = animalDetailsRepository.findByUserId(loggedInUser.getId())
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new ApiResponse<>("Successfully retrieved animals for user", animals);
        } else {
            List<List<AnimalResponse>> emptyList = List.of();
            return new ApiResponse<List<AnimalResponse>>("Unauthorized", "You are not allowed to access crops for another user.", emptyList);
        }
    }

    @Override
    public List<AnimalResponse> getLivestockForUser(long userId) {
        List<AnimalDetails> livestock = animalDetailsRepository.findByUserId(userId);
        return livestock.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AnimalResponse mapToResponse(AnimalDetails animalDetails) {
        return AnimalResponse.builder()
                .animalId(animalDetails.getId())
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
                .userId(animalDetails.getUser().getId())
                .build();
    }
}
