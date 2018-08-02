package com.septassistant.model;

public class NearestStationResult {
    private String originStation;
    private long originDuration;
    private String destinationStation;
    private long destinationDuration;
    
    public String getOriginStation() {
        return originStation;
    }
    
    public void setOriginStation(String originStation) {
        this.originStation = originStation;
    }
    
    public NearestStationResult originStation(String originStation) {
        this.originStation = originStation;
        return this;
    }
    
    public long getOriginDuration() {
        return originDuration;
    }
    
    public void setOriginDuration(long originDuration) {
        this.originDuration = originDuration;
    }
    
    public NearestStationResult originDuration(long originDuration) {
        this.originDuration = originDuration;
        return this;
    }
    
    public String getDestinationStation() {
        return destinationStation;
    }
    
    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }
    
    public NearestStationResult destinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
        return this;
    }
    
    public long getDestinationDuration() {
        return destinationDuration;
    }
    
    public void setDestinationDuration(long destinationDuration) {
        this.destinationDuration = destinationDuration;
    }
    
    public NearestStationResult destinationDuration(long destinationDuration) {
        this.destinationDuration = destinationDuration;
        return this;
    }
}
