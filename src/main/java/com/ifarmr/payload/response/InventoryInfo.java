package com.ifarmr.payload.response;

import com.ifarmr.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class InventoryInfo {

    private Long id;
    private String name;
    private String quantity;
    private String description;
    private Category category;
    private String photoUpload;
}
