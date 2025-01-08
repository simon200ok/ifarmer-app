package com.ifarmr.service.impl;

import com.ifarmr.entity.User;
import com.ifarmr.payload.response.TotalResourcesDTO;
import com.ifarmr.repository.AnimalDetailsRepository;
import com.ifarmr.repository.CropDetailsRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private CropDetailsRepository cropDetailsRepository;

    @Autowired
    private AnimalDetailsRepository animalDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TotalResourcesDTO getTotalResources(Long userId) {
        // Fetch the user object to ensure the user exists
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Get total crops and livestock for the user
        int totalCrops = cropDetailsRepository.countByUserId(userId);
        int totalLivestock = animalDetailsRepository.countByUserId(userId);

        // Return the result in a DTO
        return new TotalResourcesDTO(totalCrops, totalLivestock);
    }
}
