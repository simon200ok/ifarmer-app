package com.ifarmr.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedResources {
    private List<AnimalResponse> livestock;
    private List<CropResponse> crops;
}
