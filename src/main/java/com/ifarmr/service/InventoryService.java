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

    Long getInventoryCountByCategory(long id, Category category);

    Long getInventoryCountByItemType(long id, ItemType itemType);

    List<InventoryResponse> getAllInventoryByCategory(long id, Category category);

    List<InventoryResponse> getAllInventoryByItemType(long id, ItemType itemType);
}
