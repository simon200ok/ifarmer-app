package com.ifarmr.payload.response;

import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.entity.enums.CropType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CropResponse {

    private Long id;
    private String cropName;
    private CropType cropType;
    private String plantingSeason;
    private LocalDate harvestDate;
    private LocalDate sowDate;
    private Integer numberOfSeedlings;
    private BigDecimal costOfSeedlings;
    private String wateringFrequency;
    private String fertilizingFrequency;
    private String pestsAndDiseases;
    private String quantity;
    private String location;
    private CropStatus cropStatus;
    private String description;
    private String photoFilePath;

}
