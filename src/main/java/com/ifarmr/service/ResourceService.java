package com.ifarmr.service;


import com.ifarmr.payload.response.TotalResourcesDTO;

public interface ResourceService {
    TotalResourcesDTO getTotalResources(Long userId);
}

