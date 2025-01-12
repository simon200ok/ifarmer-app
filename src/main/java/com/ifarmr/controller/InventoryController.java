package com.ifarmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifarmr.payload.request.InventoryRequest;
import com.ifarmr.payload.response.InventoryResponse;
import com.ifarmr.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;



    @PostMapping(value = "/inventory", consumes = "multipart/form-data")
    public ResponseEntity<InventoryResponse> addItemToInventory(
            @RequestPart("request") InventoryRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {


        InventoryResponse response = inventoryService.addItemToInventory(request, file);
        return ResponseEntity.ok(response);
    }


}
