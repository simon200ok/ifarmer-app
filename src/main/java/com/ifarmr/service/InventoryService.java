package com.ifarmr.service;

import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InventoryService {

    InventoryResponse addItemToInventory(InventoryRequest request, MultipartFile file, Long userId);

    List<InventoryResponse> getAllInventory();
    List<InventoryResponse> getUserInventory(Long userId);
}
