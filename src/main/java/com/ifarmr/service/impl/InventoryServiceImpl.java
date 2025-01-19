package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.Inventory;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.entity.enums.ItemType;
import com.ifarmr.exception.customExceptions.DuplicateMerchandiseException;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryInfo;
import com.ifarmr.payload.response.InventoryResponse;
import com.ifarmr.repository.InventoryRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CloudinaryService;
import com.ifarmr.service.InventoryService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CloudinaryService cloudinaryService;

    private final HttpServletRequest servletRequest;



    @Override
    public InventoryResponse addItemToInventory(InventoryRequest request, MultipartFile file, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Handle file upload
        String uploadedImageUrl = null;
        if (file != null && !file.isEmpty()) {
            uploadedImageUrl = cloudinaryService.uploadFile(file);
        }

        boolean inventoryExists = inventoryRepository.existsByNameAndUser(request.getName(), user);
        if (inventoryExists) {
            throw new DuplicateMerchandiseException("Inventory with the name '"+ request.getName() +"' already exists for this user");
        }

        Inventory inventory = Inventory.builder()
                .item(request.getItem())
                .name(request.getName())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .dateAcquired(request.getDateAcquired())
                .category(request.getCategory())
                .condition(request.getCondition())
                .cost(request.getCost())
                .photoUpload(uploadedImageUrl)
                .location(request.getLocation())
                .user(user)
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return InventoryResponse.builder()
                .responseCode(AccountUtils.INVENTORY_SUCCESS_CODE)
                .responseMessage(AccountUtils.INVENTORY_SUCCESS_MESSAGE)
                .inventoryInfo(InventoryInfo.builder()
                        .id(savedInventory.getId())
                        .name(savedInventory.getName())
                        .quantity(savedInventory.getQuantity())
                        .description(savedInventory.getDescription())
                        .category(savedInventory.getCategory())
                        .photoUpload(savedInventory.getPhotoUpload())
                        .location(savedInventory.getLocation())
                        .cost(savedInventory.getCost())
                        .build())
                .build();
    }

    @Override
    public InventoryResponse updateInventory(InventoryRequest request,
                                             MultipartFile file,
                                             Long userId,
                                             Long inventoryId) {
        Inventory existingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", inventoryId));

        if (request.getName() != null &&
                inventoryRepository.existsByNameAndUserIdNotAndIdNot(request.getName(), userId, inventoryId)) {
            throw new IllegalArgumentException("Inventory with the name '" + request.getName() + "' already exists for this user.");
        }

        if (request.getItem() != null) existingInventory.setItem(request.getItem());
        if (request.getName() != null) existingInventory.setName(request.getName());
        if (request.getQuantity() != null) existingInventory.setQuantity(request.getQuantity());
        if (request.getCost() != null) existingInventory.setCost(request.getCost());
        if (request.getLocation() != null) existingInventory.setLocation(request.getLocation());
        if (request.getCategory() != null) existingInventory.setCategory(request.getCategory());
        if (request.getCondition() != null) existingInventory.setCondition(request.getCondition());
        if (request.getDateAcquired() != null) existingInventory.setDateAcquired(request.getDateAcquired());
        if (request.getDescription() != null) existingInventory.setDescription(request.getDescription());

        if (file != null && !file.isEmpty()) {
            String fileUrl = cloudinaryService.uploadFile(file);
            existingInventory.setPhotoUpload(fileUrl);
        }

        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return mapToResponse(updatedInventory);

    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getUserInventory(Long userId, Category category, ItemType itemType) {
        List<Inventory> inventories;

        if (category != null && itemType != null) {
            inventories = inventoryRepository.findByUserIdAndCategoryAndItem(userId, category, itemType);
        } else if (category != null) {
            inventories = inventoryRepository.findByUserIdAndCategory(userId, category);
        } else if (itemType != null) {
            inventories = inventoryRepository.findByUserIdAndItem(userId, itemType);
        } else {
            inventories = inventoryRepository.findByUserId(userId);
        }

        return inventories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getInventoryCount(Long userId) {
        return inventoryRepository.countByUserId(userId);
    }

    @Override
    public Long getInventoryCountByCategory(Long userId, Category category) {
        return inventoryRepository.countByUserIdAndCategory(userId, category);
    }

    @Override
    public Long getInventoryCountByItemType(Long userId, ItemType itemType) {
        return inventoryRepository.countByUserIdAndItem(userId, itemType);
    }

    @Override
    public List<InventoryResponse> getAllInventoryByCategory(Long userId, Category category) {
        return inventoryRepository.findByUserIdAndCategory(userId, category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getAllInventoryByItemType(Long userId, ItemType itemType) {
        return inventoryRepository.findByUserIdAndItem(userId, itemType).stream().
                map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteInventory(Long userId, Long inventoryId) {
        Inventory inventory = inventoryRepository.findByUserIdAndId(userId, inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", inventoryId));

        inventoryRepository.delete(inventory);
        return "Inventory with ID "+ inventory.getId() +" has been deleted successfully";
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .responseCode(AccountUtils.INVENTORY_SUCCESS_CODE)
                .responseMessage(AccountUtils.INVENTORY_RETRIEVED_SUCCESS_MESSAGE)
                .inventoryInfo(InventoryInfo.builder()
                        .id(inventory.getId())
                        .name(inventory.getName())
                        .quantity(inventory.getQuantity())
                        .description(inventory.getDescription())
                        .category(inventory.getCategory())
                        .photoUpload(inventory.getPhotoUpload())
                        .location(inventory.getLocation())
                        .cost(inventory.getCost())
                        .build())
                .build();

    }




}
