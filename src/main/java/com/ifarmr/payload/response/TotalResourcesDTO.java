package com.ifarmr.payload.response;


public class TotalResourcesDTO {
    private int totalCrops;
    private int totalLivestock;

    // Constructor
    public TotalResourcesDTO(int totalCrops, int totalLivestock) {
        this.totalCrops = totalCrops;
        this.totalLivestock = totalLivestock;
    }

    // Getters and Setters
    public int getTotalCrops() {
        return totalCrops;
    }

    public void setTotalCrops(int totalCrops) {
        this.totalCrops = totalCrops;
    }

    public int getTotalLivestock() {
        return totalLivestock;
    }

    public void setTotalLivestock(int totalLivestock) {
        this.totalLivestock = totalLivestock;
    }
}

