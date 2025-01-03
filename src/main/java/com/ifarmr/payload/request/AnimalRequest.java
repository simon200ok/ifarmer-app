package com.ifarmr.payload.request;

import com.ifarmr.entity.enums.AnimalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalRequest {

    private String AnimalName;
    private AnimalType animalType;
    private String breed;
    private Integer quantity;
    private String age;
    private String feedingSchedule;
    private String wateringFrequency;
    private Integer vaccinationSchedule;
    private String healthIssues;
    private String photoFilePath;
}
