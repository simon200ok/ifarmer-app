package com.ifarmr.service.impl;

import com.ifarmr.entity.AnimalDetails;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
import com.ifarmr.exception.customExceptions.DuplicateMerchandiseException;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
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

    @Override
    public Long getTotalLivestockNumber(Long userId) {
        return animalDetailsRepository.countByUserId(userId);
    }

    @Override
    public List<AnimalResponse> getLivestockByType(Long userId, AnimalType type) {
        return  animalDetailsRepository.findByAnimalType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimalResponse> getLivestockByStatus(Long userId, AnimalStatus status) {
        return animalDetailsRepository.findByAnimalStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getLivestockCountByType(Long userId, AnimalType type) {
        return animalDetailsRepository.countByUserIdAndAnimalType(userId, type);
    }

    @Override
    public Long getLivestockCountByStatus(Long userId, AnimalStatus status) {
        return animalDetailsRepository.countByUserIdAndAnimalStatus(userId, status);
    }

    @Override
    public String deleteAnimal(Long userId, Long animalId) {
        AnimalDetails animalDetails = animalDetailsRepository.findByUserIdAndId(userId, animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal", animalId));

        animalDetailsRepository.delete(animalDetails);
        return "Animal with ID "+ animalDetails.getId() +" has been deleted successfully";
    }

    @Override
    public AnimalResponse updateAnimal(AnimalRequest request,
                                       MultipartFile photo,
                                       Long animalId,
                                       Long userId) {
        AnimalDetails animalDetails = animalDetailsRepository.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal", animalId));

        if (request.getAnimalName() != null &&
                animalDetailsRepository.existsByAnimalNameAndUserIdNotAndIdNot(request.getAnimalName(),userId, animalId)) {
            throw new IllegalArgumentException("Inventory with the name '" + request.getAnimalName() + "' already exists for this user.");
        }

        if (request.getAnimalName() != null) animalDetails.setAnimalName(request.getAnimalName());
        if (request.getAnimalType() != null) animalDetails.setAnimalType(request.getAnimalType());
        if (request.getBreed() != null) animalDetails.setBreed(request.getBreed());
        if (request.getQuantity() != null) animalDetails.setQuantity(request.getQuantity());
        if (request.getAge() != null) animalDetails.setAge(request.getAge());
        if (request.getLocation() != null) animalDetails.setLocation(request.getLocation());
        if (request.getAnimalStatus() != null) animalDetails.setAnimalStatus(request.getAnimalStatus());
        if (request.getFeedingSchedule() != null) animalDetails.setFeedingSchedule(request.getFeedingSchedule());
        if (request.getWateringFrequency() != null) animalDetails.setWateringFrequency(request.getWateringFrequency());
        if (request.getVaccinationSchedule() != null) animalDetails.setVaccinationSchedule(request.getVaccinationSchedule());
        if (request.getHealthIssues() != null) animalDetails.setHealthIssues(request.getHealthIssues());
        if (request.getDescription() != null) animalDetails.setDescription(request.getDescription());

        if (photo != null && !photo.isEmpty()) {
            String fileUrl = cloudinaryService.uploadFile(photo);
            animalDetails.setPhotoFilePath(fileUrl);
        }

        AnimalDetails updatedAnimal = animalDetailsRepository.save(animalDetails);
        return mapToResponse(updatedAnimal);

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
