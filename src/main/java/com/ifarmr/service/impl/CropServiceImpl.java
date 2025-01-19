package com.ifarmr.service.impl;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.Task;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.entity.enums.CropType;
import com.ifarmr.exception.customExceptions.DuplicateMerchandiseException;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.AnimalRequest;
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


@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropDetailsRepository cropDetailsRepository;
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
    public List<CropResponse> getCropsForUser(Long userId) {
        List<CropDetails> crops = cropDetailsRepository.findByUserId(userId);
        return crops.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Map<CropStatus, Long> getCropsCountByStatus(Long userId) {
        List<Object[]> results = cropDetailsRepository.countCropsByStatusForUser(userId);

        Map<CropStatus, Long> cropsCount = new HashMap<>();
        for (Object[] result : results) {
            CropStatus status = (CropStatus) result[0];
            Long count = (Long) result[1];
            cropsCount.put(status,count);
        }
        return cropsCount;
    }

    @Override
    public Map<CropType, Long> getCropsCountByType(Long userId) {
        List<Object[]> results = cropDetailsRepository.countCropsByCropTypeForUser(userId);

        Map<CropType, Long> cropsCount = new HashMap<>();
        for (Object[] result : results) {
            CropType type = (CropType) result[0];
            Long count = (Long) result[1];
            cropsCount.put(type, count);
        }
        return cropsCount;
    }

    @Override
    public Long getTotalCropNumber(Long userId) {
        return cropDetailsRepository.countByUserId(userId);
    }

    @Override
    public List<CropResponse> getCropByType(Long userId, CropType type) {
        return cropDetailsRepository.findByUserIdAndCropType(userId, type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CropResponse> getCropByStatus(Long userId, CropStatus status) {
        return cropDetailsRepository.findByUserIdAndCropStatus(userId, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CropResponse updateCrop(CropRequest cropRequest,
                                   MultipartFile photo,
                                   Long cropId,
                                   Long userId) {
        CropDetails cropDetails = cropDetailsRepository.findById(cropId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop", cropId));

        if (cropRequest.getCropName() != null &&
                cropDetailsRepository.existsByCropNameAndUserIdNotAndIdNot(cropRequest.getCropName(), userId, cropId)) {
            throw new IllegalArgumentException("A crop with the name '" + cropRequest.getCropName() + "' already exists for this user.");
        }

        if (cropRequest.getCropName() != null) cropDetails.setCropName(cropRequest.getCropName());
        if (cropRequest.getCropType() != null) cropDetails.setCropType(cropRequest.getCropType());
        if (cropRequest.getPlantingSeason() != null) cropDetails.setPlantingSeason(cropRequest.getPlantingSeason());
        if (cropRequest.getHarvestDate() != null) cropDetails.setHarvestDate(cropRequest.getHarvestDate());
        if (cropRequest.getSowDate() != null) cropDetails.setSowDate(cropRequest.getSowDate());
        if (cropRequest.getNumberOfSeedlings() != null) cropDetails.setNumberOfSeedlings(cropRequest.getNumberOfSeedlings());
        if (cropRequest.getCostOfSeedlings() != null) cropDetails.setCostOfSeedlings(cropRequest.getCostOfSeedlings());
        if (cropRequest.getWateringFrequency() != null) cropDetails.setWateringFrequency(cropRequest.getWateringFrequency());
        if (cropRequest.getFertilizingFrequency() != null) cropDetails.setFertilizingFrequency(cropRequest.getFertilizingFrequency());
        if (cropRequest.getPestsAndDiseases() != null) cropDetails.setPestsAndDiseases(cropRequest.getPestsAndDiseases());
        if (cropRequest.getQuantity() != null) cropDetails.setQuantity(cropRequest.getQuantity());
        if (cropRequest.getLocation() != null) cropDetails.setLocation(cropRequest.getLocation());
        if (cropRequest.getCropStatus() != null) cropDetails.setCropStatus(cropRequest.getCropStatus());
        if (cropRequest.getDescription() != null) cropDetails.setDescription(cropRequest.getDescription());

        if (photo != null && !photo.isEmpty()) {
            String fileUrl = cloudinaryService.uploadFile(photo);
            cropDetails.setPhotoFilePath(fileUrl);
        }
        CropDetails updatedCrop = cropDetailsRepository.save(cropDetails);
        return mapToResponse(updatedCrop);
    }

    @Override
    public String deleteCrop(Long userId, Long cropId) {
        CropDetails crop = cropDetailsRepository.findByUserIdAndId(userId, cropId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop", cropId));

        cropDetailsRepository.delete(crop);
        return "Task with ID " + crop.getId() + " has been deleted successfully.";
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
