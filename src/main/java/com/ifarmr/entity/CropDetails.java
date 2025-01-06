package com.ifarmr.entity;

import com.ifarmr.entity.enums.CropType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table (name = "crop_tbl")
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class CropDetails extends BaseClass {

    @Column(name = "crop_name", nullable = false)
    private String cropName;

    @Column(name = "crop_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CropType cropType;

    @Column(name = "planting_season", nullable = false)
    private LocalDate plantingSeason;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

    @Column(name = "sow_date", nullable = false)
    private LocalDate sowDate;

    @Column(name = "number_of_seedlings", nullable = false)
    private Integer numberOfSeedlings;

    @Column(name = "cost_of_seedlings", nullable = false)
    private BigDecimal costOfSeedlings;

    @Column(name = "watering_frequency", nullable = false)
    private Integer wateringFrequency;

    @Column(name = "fertilizing_frequency", nullable = false)
    private Integer fertilizingFrequency;

    @Column(name = "pests_and_diseases", length = 500)
    private String pestsAndDiseases;

    @Column(name = "quantity", nullable = false)
    private LocalDate quantity;

    @Column(name = "location", nullable = false)
    private LocalDate location;

    @Column(name = "status", nullable = false)
    private LocalDate status;

    @Column(name = "description", nullable = false)
    private LocalDate description;

    @Column(name = "photo_file_path")
    private String photoFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
