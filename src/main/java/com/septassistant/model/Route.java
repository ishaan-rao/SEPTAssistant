package com.septassistant.model;

public class Route {
	private boolean isDirect;
	private String origDepartureTime;
	private String origLine;
	private String origTrain;
	private String origArrivalTime;
	
	private String connection;
	private String termDepartTime;
	private String termLine;
	private String termTrain;
	private String termArrivalTime;
	
	private String origStation;
	private String destStation;
	
	public Route() {
		
	}
	
	public Route(boolean isDirect, String origDepartureTime, String origLine, String origTrain, String origArrivalTime, String origStation, String destStation) {
		this.isDirect = isDirect;
		this.origDepartureTime = origDepartureTime;
		this.origLine = origLine;
		this.origTrain = origTrain;
		this.origArrivalTime = origArrivalTime;
		
		this.origStation = origStation;
		this.destStation = destStation;
	}
	
	public Route(boolean isDirect, String origDepartureTime, String origLine, String origTrain, String origArrivalTime, String connection, 
				String termDepartTime, String termLine, String termTrain, String termArrivalTime, String origStation, String destStation) {
		this.isDirect = isDirect;
		this.origDepartureTime = origDepartureTime;
		this.origLine = origLine;
		this.origTrain = origTrain;
		this.origArrivalTime = origArrivalTime;
		
		this.connection = connection;
		this.termDepartTime = termDepartTime;
		this.termLine = termLine;
		this.termTrain = termTrain;
		this.termArrivalTime = termArrivalTime;
		
		this.origStation = origStation;
		this.destStation = destStation;
	}
	
	public boolean getIsDirect() {
		return isDirect;
	}
	
	public void setIsDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}
	
	public String getOrigDepartureTime() {
		return origDepartureTime;
	}
	
	public void setOrigDepartureTime(String origDepartureTime) {
		this.origDepartureTime = origDepartureTime;
	}
	
	public String getOrigLine() {
		return origLine;
	}
	
	public void setOrigLine(String origLine) {
		this.origLine = origLine;
	}
	
	public String getOrigTrain() {
		return origTrain;
	}
	
	public void setOrigTrain(String origTrain) {
		this.origTrain = origTrain;
	}
	
	public String getOrigArrivalTime() {
		return origArrivalTime;
	}
	
	public void setOrigArrivalTime(String origArrivalTime) {
		this.origArrivalTime = origArrivalTime;
	}
	
	public String getConnection() {
		return connection;
	}
	
	public void setConnection(String connection) {
		this.connection = connection;
	}
	
	public String getTermDepartTime() {
		return termDepartTime;
	}
	
	public void setTermDepartTime(String termDepartTime) {
		this.termDepartTime = termDepartTime;
	}
	
	public String getTermLine() {
		return termLine;
	}
	
	public void setTermLine(String termLine) {
		this.termLine = termLine;
	}
	
	public String getTermTrain() {
		return termTrain;
	}
	
	public void setTermTrain(String termTrain) {
		this.termTrain = termTrain;
	}
	
	public String getTermArrivalTime() {
		return termArrivalTime;
	}
	
	public void setTermArrivalTime(String termArrivalTime) {
		this.termArrivalTime = termArrivalTime;
	}
	
	public String getOrigStation() {
		return origStation;
	}
	
	public void setOrigStation(String origStation) {
		this.origStation = origStation;
	}
	
	public String getDestStation() {
		return destStation;
	}
	
	public void setDestStation(String destStation) {
		this.destStation = destStation;
	}
	
	
}
