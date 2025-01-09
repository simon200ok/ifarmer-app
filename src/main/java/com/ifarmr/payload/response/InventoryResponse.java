package com.ifarmr.payload.response;

import com.ifarmr.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    private String responseCode;
    private String responseMessage;
    private InventoryInfo inventoryInfo;
}

