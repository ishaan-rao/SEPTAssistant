package com.septassistant.model;

import com.google.maps.model.LatLng;

public class SeptaStation {
	private String name;
	private Zone zone;
	private LatLng coordinates;
	private ZoneDirection zoneDirection;
	
	public SeptaStation() {
		
	}
	
	public SeptaStation(String name, Zone zone, LatLng coordinates, ZoneDirection zoneDirection) {
		this.name = name;
		this.zone = zone;
		this.coordinates = coordinates;
		this.zoneDirection = zoneDirection;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	public LatLng getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
	}
	
	public ZoneDirection getZoneDirection() {
		return zoneDirection;
	}
	
	public void setZoneDirection(ZoneDirection zoneDirection) {
		this.zoneDirection = zoneDirection;
	}
}
