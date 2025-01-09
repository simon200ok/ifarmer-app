package com.ifarmr.service.impl;

import com.ifarmr.payload.response.*;
import com.ifarmr.repository.AnimalDetailsRepository;
import com.ifarmr.repository.CropDetailsRepository;
import com.ifarmr.service.AnimalService;
import com.ifarmr.service.CropService;
import com.ifarmr.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final CropDetailsRepository cropDetailsRepository;
    private final AnimalDetailsRepository animalDetailsRepository;
    private final AnimalService animalService;
    private final CropService cropService;

    @Override
    public TotalResourcesDTO getTotalResources(Long userId) {

        long totalCrops = cropDetailsRepository.countByUserId(userId);
        long totalLivestock = animalDetailsRepository.countByUserId(userId);

        return TotalResourcesDTO.builder()
                .totalCrops(totalCrops)
                .totalLivestock(totalLivestock)
                .build();
    }

    @Override
    public DetailedResources getUserDetailedResources(Long userId) {
        List<AnimalResponse> livestock = animalService.getLivestockForUser(userId);
        List<CropResponse> crops = cropService.getCropsForUser(userId);

        return DetailedResources.builder()
                .livestock(livestock)
                .crops(crops)
                .build();
    }
}
