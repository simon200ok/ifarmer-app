package com.ifarmr.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DetailedResources {
    private List<AnimalResponse> livestock;
    private List<CropResponse> crops;
}
