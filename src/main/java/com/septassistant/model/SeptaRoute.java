package com.septassistant.model;

public class SeptaRoute {
    private double cost;
    private Route route;
    
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
}
