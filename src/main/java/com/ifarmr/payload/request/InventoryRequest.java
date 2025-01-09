package com.ifarmr.payload.request;

import com.ifarmr.entity.enums.Category;
import com.ifarmr.entity.enums.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {
    @NotNull
    private ItemType item;

    @NotBlank
    private String name;

    @NotNull
    private String quantity;

    @NotNull
    private BigDecimal cost;

    private String location;

    private Category category;

    private String condition;

    @PastOrPresent
    private LocalDate dateAcquired;

    private String description;

    @NotBlank
    private String photoUpload;
}

