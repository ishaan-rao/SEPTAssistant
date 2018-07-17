package com.septassistant;

import com.uber.sdk.rides.client.model.PriceEstimate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.septassistant.model.Route;

public class Application {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter your starting address: ");
		String origin = sc.nextLine();
		
		System.out.println("Enter your destination address: ");
		String destination = sc.nextLine();
	
		sc.close();

		Date date = new Date();	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		boolean isWeekday = !(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
		
		uber(origin, destination);
		septa(origin, destination, isWeekday, date);	
	}
	
	public static void uber(String origin, String destination) {	
		LatLng originCoordinates = Maps.getCoordinates(origin);
		LatLng destinationCoordinates = Maps.getCoordinates(destination);
		
		PriceEstimate priceEstimate = Uber.getPriceEstimate((float) originCoordinates.lat, (float) originCoordinates.lng, (float) destinationCoordinates.lat, (float) destinationCoordinates.lng);
		
		long durationInTraffic = Maps.getDurationInTraffic(origin, destination, TravelMode.DRIVING);
		
		System.out.println(priceEstimate.getDisplayName());
		System.out.println(priceEstimate.getEstimate());
		System.out.println(durationInTraffic);
	}
	
	public static void septa(String origin, String destination, boolean isWeekday, Date dateTime) {
		Septa.init();
		String[] stationResults = Septa.getNearestStation(origin, destination);
		
		String originStation = stationResults[0];
		String destinationStation = stationResults[2];
		
		Calendar gc = new GregorianCalendar();
		gc.setTime(dateTime);
		gc.add(Calendar.SECOND, Integer.valueOf(stationResults[1]));
		gc.add(Calendar.MINUTE, 1);
		Date newDate = gc.getTime();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
		String format = dateFormat.format(newDate); 
		
		String fullTime = format.split(" ")[1];
		String half = format.split(" ")[2];
		String time = fullTime.substring(0, fullTime.lastIndexOf(":")) + half;
				
		Route route = Septa.getRoute(originStation, destinationStation, time);
		double cost = Septa.getCost(originStation, destinationStation, true, route);
		
		if (route.getIsDirect()) {
			System.out.println("Board train " + route.getOrigTrain() + " on the " + route.getOrigLine() + " line at " + route.getOrigStation() + " at " + route.getOrigDepartureTime());
			System.out.println("Arrive at " + route.getDestStation() + " at " + route.getOrigArrivalTime());
		} else {
			System.out.println("Board train " + route.getOrigTrain() + " on the " + route.getOrigLine() + " line at " + route.getOrigStation() + " at " + route.getOrigDepartureTime());
			System.out.println("Arrive at " + route.getConnection() + " at " + route.getOrigArrivalTime());
			System.out.println("Board connection train " + route.getTermTrain() + " on the " + route.getTermLine() + " line at " + route.getConnection() + " at " + route.getTermDepartTime());
			System.out.println("Arrive at " + route.getDestStation() + " at " + route.getTermArrivalTime());
		}
		
		System.out.println(cost);
	}
}
