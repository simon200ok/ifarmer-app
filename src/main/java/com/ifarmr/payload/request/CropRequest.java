package com.ifarmr.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CropRequest {

    private String cropName;
    private CropType cropType;
    private String plantingSeason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate harvestDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
//    private String photoFilePath;

}
