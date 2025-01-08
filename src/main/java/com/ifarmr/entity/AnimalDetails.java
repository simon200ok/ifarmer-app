package com.ifarmr.entity;

import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "animal_tbl")
public class AnimalDetails extends BaseClass{

    @Column(name = "animal_name", nullable = false)
    private String animalName;

    @Column(name = "animal_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "quantity", nullable = false)
    private String quantity;

    @Column(name = "age", nullable = false)
    private String age;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "animal_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalStatus animalStatus;

    @Column(name = "feeding_schedule", nullable = false)
    private String feedingSchedule;

    @Column(name = "watering_frequency", nullable = false)
    private String wateringFrequency;

    @Column(name = "vaccination_schedule", nullable = false)
    private String vaccinationSchedule;

    @Column(name = "health_issues", length = 500)
    private String healthIssues;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "photo_file_path")
    private String photoFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
