package com.ifarmr.payload.response;

import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalResponse {

    private Long animalId;
    private String animalName;
    private AnimalType animalType;
    private String breed;
    private String quantity;
    private String age;
    private String location;
    private AnimalStatus animalStatus;
    private String feedingSchedule;
    private String wateringFrequency;
    private String vaccinationSchedule;
    private String healthIssues;
    private String description;
    private String photoFilePath;
    private Long userId;
    private LocalDateTime createdAt;
}
