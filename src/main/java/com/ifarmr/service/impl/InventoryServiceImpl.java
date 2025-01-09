package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.Inventory;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryInfo;
import com.ifarmr.payload.response.InventoryResponse;
import com.ifarmr.repository.InventoryRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CloudinaryService;
import com.ifarmr.service.InventoryService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    public InventoryResponse addItemToInventory(InventoryRequest request, MultipartFile file) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        // Token validation
        if (token == null || token.isEmpty() || !jwtService.validateToken(token)) {
            return InventoryResponse.builder()
                    .responseCode(AccountUtils.INVALID_TOKEN_CODE)
                    .responseMessage(AccountUtils.INVALID_TOKEN_MESSAGE)
                    .build();
        }

        Long userId = jwtService.extractUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Handle file upload
        String uploadedImageUrl = null;
        if (file != null && !file.isEmpty()) {
            uploadedImageUrl = cloudinaryService.uploadFile(file);
        }

        // Create inventory
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
                .user(user)
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        // Construct response
        return InventoryResponse.builder()
                .responseCode(AccountUtils.INVENTORY_SUCCESS_CODE)
                .responseMessage(AccountUtils.INVENTORY_SUCCESS_MESSAGE)
                .inventoryInfo(InventoryInfo.builder()
                        .name(savedInventory.getName())
                        .quantity(savedInventory.getQuantity())
                        .description(savedInventory.getDescription())
                        .category(savedInventory.getCategory())
                        .photoUpload(savedInventory.getPhotoUpload())
                        .build())
                .build();
    }




}
