package com.septassistant;

import java.io.IOException;
import java.util.List;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.septassistant.model.UberRoute;
import com.uber.sdk.core.client.ServerTokenSession;
import com.uber.sdk.core.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.PriceEstimate;
import com.uber.sdk.rides.client.model.PriceEstimatesResponse;
import com.uber.sdk.rides.client.services.RidesService;

import retrofit2.Call;
import retrofit2.Response;

public class Uber {
	
	private static SessionConfiguration config = new SessionConfiguration.Builder()
		    .setClientId("")
		    .setServerToken("")
		    .build();

	private static ServerTokenSession session = new ServerTokenSession(config);
	private static RidesService ridesService = UberRidesApi.with(session).build().createService();
	
	private static List<PriceEstimate> getPriceEstimates(float startLatitude, float startLongitude, float endLatitude, float endLongitude) {
		
		Call<PriceEstimatesResponse> response = ridesService.getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude);
		
		Response<PriceEstimatesResponse> priceEstimate = null;
		try {
			priceEstimate = response.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<PriceEstimate> p = priceEstimate.body().getPrices();

		return p;
	}
	
	public static UberRoute getUberRoute(String origin, String destination) {
	    LatLng originCoordinates = Maps.getCoordinates(origin);
        LatLng destinationCoordinates = Maps.getCoordinates(destination);
        
        List<PriceEstimate> priceEstimates = getPriceEstimates((float) originCoordinates.lat, (float) originCoordinates.lng, (float) destinationCoordinates.lat, (float) destinationCoordinates.lng);
        
        long durationInTraffic = Maps.getDurationInTraffic(origin, destination, TravelMode.DRIVING);
        
        UberRoute uberRoute = 
                new UberRoute()
                .priceEstimates(priceEstimates)
                .duration(durationInTraffic);
        
	    return uberRoute;
	}
}
