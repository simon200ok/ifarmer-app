package com.ifarmr.payload.response;

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
    private LocalDate plantingSeason;
    private LocalDate harvestTime;
    private Integer numberOfSeedlings;
    private BigDecimal costOfSeedlings;
    private Integer wateringFrequency;
    private Integer fertilizingFrequency;
    private String pestsAndDiseases;
    private String photoFilePath;
}
