package com.septassistant.model;

public class SeptaRoute {
    private double cost;
    private Route route;
    private long originDuration;
    private long destinationDuration;
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public SeptaRoute cost(double cost) {
        this.cost = cost;
        return this;
    }
    
    public Route getRoute() {
        return route;
    }
    
    public void setRoute(Route route) {
        this.route = route;
    }
    
    public SeptaRoute route(Route route) {
        this.route = route;
        return this;
    }
    
    public long getOriginDuration() {
        return originDuration;
    }
    
    public void setOriginDuration(long originDuration) {
        this.originDuration = originDuration;
    }
    
    public SeptaRoute originDuration(long originDuration) {
        this.originDuration = originDuration;
        return this;
    }
    
    public long getDestinationDuration() {
        return destinationDuration;
    }
    
    public void setDestinationDuration(long destinationDuration) {
        this.destinationDuration = destinationDuration;
    }
    
    public SeptaRoute destinationDuration(long destinationDuration) {
        this.destinationDuration = destinationDuration;
        return this;
    }
}
