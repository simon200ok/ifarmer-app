package com.ifarmr.service.impl;

import com.ifarmr.entity.User;
import com.ifarmr.payload.response.AnimalResponse;
import com.ifarmr.payload.response.CropResponse;
import com.ifarmr.payload.response.DetailedResources;
import com.ifarmr.payload.response.TotalResourcesDTO;
import com.ifarmr.repository.AnimalDetailsRepository;
import com.ifarmr.repository.CropDetailsRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.AnimalService;
import com.ifarmr.service.CropService;
import com.ifarmr.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public DetailedResources getUserDetailedResources(long userId) {
        List<AnimalResponse> livestock = animalService.getLivestockForUser(userId);
        List<CropResponse> crops = cropService.getCropsForUser(userId);

        return DetailedResources.builder()
                .livestock(livestock)
                .crops(crops)
                .build();
    }

}
