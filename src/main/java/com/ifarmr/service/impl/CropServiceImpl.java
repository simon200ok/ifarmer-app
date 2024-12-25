package com.ifarmr.service.impl;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.CropRequest;
import com.ifarmr.payload.response.ApiResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.repository.CropDetailsRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CropService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class CropServiceImpl implements CropService {

    private final CropDetailsRepository cropDetailsRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<CropResponse> addCrop(CropRequest cropRequest) {

        User user = userRepository.findByEmail(cropRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found: " + cropRequest.getEmail()));

//        CropDetails crop = new CropDetails();
        CropDetails crop = CropDetails.builder()
                .cropName(cropRequest.getCropName())
                .cropType(cropRequest.getCropType())
                .plantingSeason(cropRequest.getPlantingSeason())
                .harvestTime(cropRequest.getHarvestTime())
                .numberOfSeedlings(cropRequest.getNumberOfSeedlings())
                .costOfSeedlings(cropRequest.getCostOfSeedlings())
                .wateringFrequency(cropRequest.getWateringFrequency())
                .fertilizingFrequency(cropRequest.getFertilizingFrequency())
                .pestsAndDiseases(cropRequest.getPestsAndDiseases())
                .photoFilePath(cropRequest.getPhotoFilePath())
                .user(user)
                .build();
        return new ApiResponse<>("Success", mapToResponse(cropDetailsRepository.save(crop)));
    }

    private CropResponse mapToResponse(CropDetails cropDetails) {
       return CropResponse.builder()
               .id(cropDetails.getId())
               .cropName(cropDetails.getCropName())
               .cropType(cropDetails.getCropType())
               .plantingSeason(cropDetails.getPlantingSeason())
               .harvestTime(cropDetails.getHarvestTime())
                .numberOfSeedlings(cropDetails.getNumberOfSeedlings())
                .costOfSeedlings(cropDetails.getCostOfSeedlings())
                .wateringFrequency(cropDetails.getWateringFrequency())
                .fertilizingFrequency(cropDetails.getFertilizingFrequency())
                .photoFilePath(cropDetails.getPhotoFilePath())
                .build();
    }
}
