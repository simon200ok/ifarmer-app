package com.ifarmr.payload.request;

import com.ifarmr.entity.enums.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalRequest {

    private String animalName;
    private AnimalType animalType;
    private String breed;
    private Integer quantity;
    private String age;
    private LocalDate location;
    private LocalDate status;
    private String feedingSchedule;
    private String wateringFrequency;
    private Integer vaccinationSchedule;
    private String healthIssues;
    private String description;
    private String photoFilePath;
}
