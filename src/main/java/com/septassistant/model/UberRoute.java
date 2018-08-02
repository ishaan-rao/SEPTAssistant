package com.septassistant.model;

import java.util.List;

import com.uber.sdk.rides.client.model.PriceEstimate;

public class UberRoute {

    private List<PriceEstimate> priceEstimates;
    private long duration;
    
    public List<PriceEstimate> getPriceEstimates() {
        return priceEstimates;
    }
    
    public void setPriceEstimates(List<PriceEstimate> priceEstimates) {
        this.priceEstimates = priceEstimates;
    }
    
    public UberRoute priceEstimates(List<PriceEstimate> priceEstimates) {
        this.priceEstimates = priceEstimates;
        return this;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public UberRoute duration(long duration) {
        this.duration = duration;
        return this;
    }
    
}
