package com.septassistant.model;

import com.google.maps.model.LatLng;

public class SeptaStation {
	private String name;
	private Zone zone;
	private LatLng coordinates;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public SeptaStation name(String name) {
	    this.name = name;
	    return this;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	public SeptaStation zone(Zone zone) {
        this.zone = zone;
        return this;
    }
	
	public LatLng getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
	}
	
	public SeptaStation coordinates(LatLng coordinates) {
	    this.coordinates = coordinates;
	    return this;
	}
}
