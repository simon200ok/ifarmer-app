package com.ifarmr.service.impl;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.User;
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

    private CropResponse mapToResponse(CropDetails cropDetails) {
       return CropResponse.builder()
               .id(cropDetails.getId())
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
                .build();
    }
}
