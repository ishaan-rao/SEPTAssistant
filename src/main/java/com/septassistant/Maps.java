package com.septassistant;

import org.joda.time.DateTime;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;

public class Maps {
	private static final String API_KEY = "";
	
	public static long getDistance(String origin, String destination, TravelMode mode) {
		GeoApiContext.Builder builder = new GeoApiContext.Builder().apiKey(API_KEY);
		GeoApiContext geoApiContext = builder.build();
	
		String[] origins = new String[1];
		String[] destinations = new String[1];
		
		origins[0] = origin;
		destinations[0] = destination;
				
		DistanceMatrixApiRequest request = DistanceMatrixApi.getDistanceMatrix(geoApiContext, origins, destinations);
		request.mode(mode);
		
		DistanceMatrix dm = null;
		try {
			dm = request.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dm.rows[0].elements[0].distance.inMeters;
	}	
	
	public static long getDuration(String origin, String destination, TravelMode mode) {
		GeoApiContext.Builder builder = new GeoApiContext.Builder().apiKey(API_KEY);
		GeoApiContext geoApiContext = builder.build();
	
		String[] origins = new String[1];
		String[] destinations = new String[1];
		
		origins[0] = origin;
		destinations[0] = destination;
				
		DistanceMatrixApiRequest request = DistanceMatrixApi.getDistanceMatrix(geoApiContext, origins, destinations);
		request.mode(mode);
		
		DistanceMatrix response = null;
		try {
			response = request.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response.rows[0].elements[0].duration.inSeconds;
	}
	
	public static long getDurationInTraffic(String origin, String destination, TravelMode mode) {
		GeoApiContext.Builder builder = new GeoApiContext.Builder().apiKey(API_KEY);
		GeoApiContext geoApiContext = builder.build();
	
		String[] origins = new String[1];
		String[] destinations = new String[1];
		
		origins[0] = origin;
		destinations[0] = destination;
		
		DistanceMatrixApiRequest request = DistanceMatrixApi.getDistanceMatrix(geoApiContext, origins, destinations);
		request.mode(mode);
		request.departureTime(new DateTime());
		
		DistanceMatrix response = null;
		try {
			response = request.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response.rows[0].elements[0].durationInTraffic.inSeconds;
	}
	
	public static LatLng getCoordinates(String location) {
		GeoApiContext.Builder builder = new GeoApiContext.Builder().apiKey(API_KEY);
		GeoApiContext geoApiContext = builder.build();
				
		GeocodingApiRequest request = GeocodingApi.geocode(geoApiContext, location);
		
		GeocodingResult[] response = null;
		try {
			response = request.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LatLng latLng = response[0].geometry.location;
		
		return latLng;
	}
}
