package com.septassistant;

import java.io.IOException;

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
		    .setClientId("ICfD5Ez9WdiXWQd4JVZfN5lyXsjpSDwW")
		    .setServerToken("NofPx0xr519nOPBkHsYX9VcaKZ4tgxWlTTt5vWRn")
		    .build();

	private static ServerTokenSession session = new ServerTokenSession(config);
	private static RidesService ridesService = UberRidesApi.with(session).build().createService();
	
	public static PriceEstimate getPriceEstimate(float startLatitude, float startLongitude, float endLatitude, float endLongitude) {
		
		Call<PriceEstimatesResponse> response = ridesService.getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude);
		
		Response<PriceEstimatesResponse> priceEstimate = null;
		try {
			priceEstimate = response.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PriceEstimate p = priceEstimate.body().getPrices().get(0);

		return p;
	}
}
