package com.ifarmr.service;

import com.ifarmr.entity.enums.Category;
import com.ifarmr.entity.enums.ItemType;
import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InventoryService {

    InventoryResponse addItemToInventory(InventoryRequest request, MultipartFile file, Long userId);

    List<InventoryResponse> getAllInventory();
    List<InventoryResponse> getUserInventory(Long userId, Category category, ItemType itemType);

    Long getInventoryCountByCategory(Long id, Category category);

    Long getInventoryCountByItemType(Long id, ItemType itemType);

    List<InventoryResponse> getAllInventoryByCategory(Long id, Category category);

    List<InventoryResponse> getAllInventoryByItemType(Long id, ItemType itemType);

    String deleteInventory(Long userId, Long inventoryId);

    Long getInventoryCount(Long userId);

    InventoryResponse updateInventory(InventoryRequest request, Long id, Long inventoryId);
}
