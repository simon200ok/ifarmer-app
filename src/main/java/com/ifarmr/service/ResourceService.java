package com.ifarmr.service;


import com.ifarmr.payload.response.TotalResourcesDTO;

import java.util.Map;

public interface ResourceService {
    TotalResourcesDTO getTotalResources(Long userId);
}

