package com.ifarmr.service.impl;

import com.ifarmr.entity.User;
import com.ifarmr.payload.response.TotalResourcesDTO;
import com.ifarmr.repository.AnimalDetailsRepository;
import com.ifarmr.repository.CropDetailsRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final CropDetailsRepository cropDetailsRepository;
    private final AnimalDetailsRepository animalDetailsRepository;

    @Override
    public TotalResourcesDTO getTotalResources(Long userId) {

        long totalCrops = cropDetailsRepository.countByUserId(userId);
        long totalLivestock = animalDetailsRepository.countByUserId(userId);

        return TotalResourcesDTO.builder()
                .totalCrops(totalCrops)
                .totalLivestock(totalLivestock)
                .build();
    }
}
