package com.ifarmr.service;

import com.ifarmr.payload.response.DetailedResources;
import com.ifarmr.payload.response.TotalResourcesDTO;


public interface ResourceService {

    TotalResourcesDTO getTotalResources(Long userId);

    DetailedResources getUserDetailedResources(Long userId);

}

