package com.ifarmr.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalResourcesDTO {

    private Long totalCrops;
    private Long totalLivestock;

}

