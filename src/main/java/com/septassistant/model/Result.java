package com.septassistant.model;

public class Result {
    private UberRoute uberRoute;
    private SeptaRoute septaRoute;
    
    public UberRoute getUberRoute() {
        return uberRoute;
    }
    
    public void setUberRoute(UberRoute uberRoute) {
        this.uberRoute = uberRoute;
    }
    
    public Result uberRoute(UberRoute uberRoute) {
        this.uberRoute = uberRoute;
        return this;
    }
    
    public SeptaRoute getSeptaRoute() {
        return septaRoute;
    }
    
    public void setSeptaRoute(SeptaRoute septaRoute) {
        this.septaRoute = septaRoute;
    }
    
    public Result septaRoute(SeptaRoute septaRoute) {
        this.septaRoute = septaRoute;
        return this;
    }
}
