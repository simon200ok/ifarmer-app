package com.ifarmr.service;

import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface InventoryService {



//    InventoryResponse addItemToInventory(InventoryRequest request, Long userId);

    InventoryResponse addItemToInventory(InventoryRequest request, MultipartFile file);
}
