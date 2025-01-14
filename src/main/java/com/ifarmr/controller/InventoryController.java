package com.ifarmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifarmr.entity.User;
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
    public ResponseEntity<InventoryResponse> addItemToInventory(
            @RequestPart("request") InventoryRequest request,
            @RequestPart(value = "file") MultipartFile file,
            @AuthenticationPrincipal User user) {
        InventoryResponse response = inventoryService.addItemToInventory(request, file, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<InventoryResponse>> getUserInventory(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(inventoryService.getUserInventory(user.getId()));
    }
}
