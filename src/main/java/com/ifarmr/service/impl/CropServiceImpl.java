package com.ifarmr.service.impl;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.Task;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.entity.enums.CropType;
import com.ifarmr.exception.customExceptions.DuplicateMerchandiseException;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.repository.CropDetailsRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CloudinaryService;
import com.ifarmr.service.CropService;
import com.ifarmr.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropDetailsRepository cropDetailsRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final CloudinaryService cloudinaryService;

    @Override
    @SneakyThrows
    public ApiResponse<CropResponse> addCrop(CropRequest cropRequest, MultipartFile photo) {
        User user = securityUtils.getLoggedInUser();

        boolean cropExists = cropDetailsRepository.existsByCropNameAndUser(cropRequest.getCropName(), user);
        if (cropExists) {
            throw new DuplicateMerchandiseException("Crop with the name '"+ cropRequest.getCropName() +"' already exists for this user.");
        }

        CropDetails crop = CropDetails.builder()
                .cropName(cropRequest.getCropName())
                .cropType(cropRequest.getCropType())
                .plantingSeason(cropRequest.getPlantingSeason())
                .harvestDate(cropRequest.getHarvestDate())
                .sowDate(cropRequest.getSowDate())
                .numberOfSeedlings(cropRequest.getNumberOfSeedlings())
                .costOfSeedlings(cropRequest.getCostOfSeedlings())
                .wateringFrequency(cropRequest.getWateringFrequency())
                .fertilizingFrequency(cropRequest.getFertilizingFrequency())
                .pestsAndDiseases(cropRequest.getPestsAndDiseases())
                .quantity(cropRequest.getQuantity())
                .location(cropRequest.getLocation())
                .cropStatus(cropRequest.getCropStatus())
                .description(cropRequest.getDescription())
                .photoFilePath(cloudinaryService.uploadFile(photo))
                .user(user)
                .build();
        return new ApiResponse<>("Crop Successfully added", mapToResponse(cropDetailsRepository.save(crop)));
    }

    @Override
    public ApiResponse<List<CropResponse>> getAllCrops() {
        // Retrieve the logged-in user to check access (if required)
        securityUtils.getLoggedInUser(); // Ensure the user is authenticated

        List<CropResponse> crops = cropDetailsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>("Successfully retrieved all crops", crops);
    }


    @Override
    public ApiResponse<List<CropResponse>> getCropsByUserId(Long userId) {
        // Retrieve the logged-in user from SecurityUtils if you want to filter by authenticated user
        User loggedInUser = securityUtils.getLoggedInUser();

        // Use the logged-in user's ID if you're fetching crops for the currently authenticated user
        if (userId == null || userId.equals(loggedInUser.getId())) {
            List<CropResponse> crops = cropDetailsRepository.findByUserId(loggedInUser.getId())
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            return new ApiResponse<>("Successfully retrieved crops for user", crops);
        } else {
            List<List<CropResponse>> emptyList = List.of();
            return new ApiResponse<List<CropResponse>>("Unauthorized", "You are not allowed to access crops for another user.", emptyList);
        }
    }

    @Override
    public List<CropResponse> getCropsForUser(long userId) {
        List<CropDetails> crops = cropDetailsRepository.findByUserId(userId);
        return crops.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Map<CropStatus, Long> getCropsCountByStatus() {
        List<Object[]> results = cropDetailsRepository.countCropsByStatus();

        Map<CropStatus, Long> cropsCount = new HashMap<>();
        for (Object[] result : results) {
            CropStatus status = (CropStatus) result[0];
            Long count = (Long) result[1];
            cropsCount.put(status,count);
        }
        return cropsCount;
    }

    @Override
    public String deleteCrop(Long cropId) {
        CropDetails crop = cropDetailsRepository.findById(cropId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop", cropId));

        cropDetailsRepository.delete(crop);
        return "Task with ID "+ crop.getId() +" has been deleted successfully.";
    }


    private CropResponse mapToResponse(CropDetails cropDetails) {
       return CropResponse.builder()
               .cropId(cropDetails.getId())
               .cropName(cropDetails.getCropName())
               .cropType(cropDetails.getCropType())
               .plantingSeason(cropDetails.getPlantingSeason())
               .harvestDate(cropDetails.getHarvestDate())
               .sowDate(cropDetails.getSowDate())
               .numberOfSeedlings(cropDetails.getNumberOfSeedlings())
               .costOfSeedlings(cropDetails.getCostOfSeedlings())
               .wateringFrequency(cropDetails.getWateringFrequency())
               .fertilizingFrequency(cropDetails.getFertilizingFrequency())
               .pestsAndDiseases(cropDetails.getPestsAndDiseases())
               .quantity(cropDetails.getQuantity())
               .location(cropDetails.getLocation())
               .cropStatus(cropDetails.getCropStatus())
               .description(cropDetails.getDescription())
               .photoFilePath(cropDetails.getPhotoFilePath())
               .userId(cropDetails.getUser().getId())
               .build();
    }

}
