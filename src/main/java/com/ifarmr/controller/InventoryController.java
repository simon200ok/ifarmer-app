package com.ifarmr.controller;

import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.entity.enums.ItemType;
import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryResponse;
import com.ifarmr.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<InventoryResponse> addItemToInventory(@RequestPart("request") InventoryRequest request,
                                                                @RequestPart(value = "file") MultipartFile file,
                                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(inventoryService.addItemToInventory(request, file, user.getId()));
    }

    @PatchMapping(value = "/update")
    public ResponseEntity<InventoryResponse> updateInventory(@RequestBody InventoryRequest request,
                                                             @AuthenticationPrincipal User user,
                                                             @RequestParam Long inventoryId) {
        return ResponseEntity.ok(inventoryService.updateInventory(request, user.getId(), inventoryId));
    }


    @GetMapping(value = "/user")
    public ResponseEntity<List<InventoryResponse>> getUserInventory(@AuthenticationPrincipal User user,
                                                                    @RequestParam(required = false) Category category,
                                                                    @RequestParam(required = false) ItemType itemType) {
        return ResponseEntity.ok(inventoryService.getUserInventory(user.getId(), category, itemType));
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Long> getInventoryCount(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(inventoryService.getInventoryCount(user.getId()));
    }

    @GetMapping(value = "/count-by-category")
    public ResponseEntity<Long> getInventoryCountByCategory(@AuthenticationPrincipal User user,
                                                            @RequestParam Category category) {
        return ResponseEntity.ok(inventoryService.getInventoryCountByCategory(user.getId(), category));
    }

    @GetMapping(value = "/count-by-itemType")
    public ResponseEntity<Long> getInventoryCountByItemType(
            @AuthenticationPrincipal User user,
            @RequestParam ItemType itemType) {
        return ResponseEntity.ok(inventoryService.getInventoryCountByItemType(user.getId(), itemType));
    }

    @GetMapping(value = "/all-by-category")
    public ResponseEntity<List<InventoryResponse>> getAllInventoryByCategory(@AuthenticationPrincipal User user,
                                                                             @RequestParam Category category) {
        return ResponseEntity.ok(inventoryService.getAllInventoryByCategory(user.getId(), category));
    }

    @GetMapping(value = "/all-by-itemType")
    public ResponseEntity<List<InventoryResponse>> getAllInventoryByItemType(@AuthenticationPrincipal User user,
                                                                             @RequestParam ItemType itemType) {
        return ResponseEntity.ok(inventoryService.getAllInventoryByItemType(user.getId(), itemType));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteInventory(@AuthenticationPrincipal User user,
                                                  @RequestParam Long inventoryId) {
        return ResponseEntity.ok(inventoryService.deleteInventory(user.getId(), inventoryId));
    }
}
