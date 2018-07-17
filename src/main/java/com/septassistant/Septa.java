package com.septassistant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.septassistant.model.Route;
import com.septassistant.model.SeptaStation;
import com.septassistant.model.Zone;
import com.septassistant.model.ZoneDirection;

import org.json.JSONObject;
import org.json.JSONArray;

public class Septa {
	private static Map<String, SeptaStation> septaStations = new HashMap<String, SeptaStation>();
	private static Map<Zone, Double> weekdayCosts = new HashMap<Zone, Double>();
	private static Map<Zone, Double> weekendCosts = new HashMap<Zone, Double>();

	private static final double OUTLYING_COST = 3.75;
	private static final double VIA_CCP_COST = 9.25;
	
	public static void init() {
		FileReader fr = null;
		try {
			fr = new FileReader("src/main/resources/stations.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(fr);
		String next = null;
		try {
			next = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(next != null) {
			String[] splitted = next.split(",");
			double lat = Double.parseDouble(splitted[0]);
			double lng = Double.parseDouble(splitted[1]);
			String zoneInput = splitted[2];
			String zoneDirectionInput = splitted[3];
			String name = splitted[4];
						
			Zone zone = null;
			ZoneDirection zoneDirection = null;
			
			if (zoneDirectionInput.equals("CC")) {
				zone = Zone.CENTER_CITY;
				zoneDirection = ZoneDirection.CENTER_CITY;
			} else if (zoneDirectionInput.equals("Top")) {
				switch(zoneInput) {
					case "1":
						zone = Zone.ONE;
						zoneDirection = ZoneDirection.TOP;
						break;
					case "2":
						zone = Zone.TWO;
						zoneDirection = ZoneDirection.TOP;
						break;
					case "3":
						zone = Zone.THREE;
						zoneDirection = ZoneDirection.TOP;
						break;
					case "4":
						zone = Zone.FOUR;
						zoneDirection = ZoneDirection.TOP;
						break;
					case "NJ":
						zone = Zone.NJ;
						zoneDirection = ZoneDirection.TOP;
						break;
					default:
						break;
				} 
			} else {
				switch(zoneInput) {
					case "1":
						zone = Zone.ONE;
						zoneDirection = ZoneDirection.BOTTOM;
						break;
					case "2":
						zone = Zone.TWO;
						zoneDirection = ZoneDirection.BOTTOM;
						break;
					case "3":
						zone = Zone.THREE;
						zoneDirection = ZoneDirection.BOTTOM;
						break;
					case "4":
						zone = Zone.FOUR;
						zoneDirection = ZoneDirection.BOTTOM;
						break;
					case "NJ":
						zone = Zone.NJ;
						zoneDirection = ZoneDirection.BOTTOM;
						break;
					default: 
						break;
				}
			}
			
			SeptaStation septaStation = new SeptaStation();
			septaStation.setName(name);
			septaStation.setZone(zone);
			septaStation.setZoneDirection(zoneDirection);
			septaStation.setCoordinates(new LatLng(lat, lng));
			septaStations.put(name, septaStation);	
			
			try {
				next = br.readLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		weekdayCosts.put(Zone.CENTER_CITY, 5.25);
		weekdayCosts.put(Zone.ONE, 5.25);
		weekdayCosts.put(Zone.TWO, 5.25);
		weekdayCosts.put(Zone.THREE, 6.00);
		weekdayCosts.put(Zone.FOUR, 6.75);
		weekdayCosts.put(Zone.NJ, 9.25);
		
		weekendCosts.put(Zone.CENTER_CITY, 4.25);
		weekendCosts.put(Zone.ONE, 4.25);
		weekendCosts.put(Zone.TWO, 4.25);
		weekendCosts.put(Zone.THREE, 5.25);
		weekendCosts.put(Zone.FOUR, 5.25);
		weekendCosts.put(Zone.NJ, 9.25);
	}
	
	public static String[] getNearestStation(String origin, String destination) {			
		long originMinTime = Long.MAX_VALUE;
		long destinationMinTime = Long.MAX_VALUE;
		String originNearestStation = null;
		String destinationNearestStation = null;
		
		for (String stationName : septaStations.keySet()) {
			String coords = septaStations.get(stationName).getCoordinates().toString();
						
			long origTime = Maps.getDuration(origin, coords, TravelMode.WALKING);
			long destTime = Maps.getDuration(destination, coords, TravelMode.WALKING);
			
			if (origTime < originMinTime) {
				originMinTime = origTime;
				originNearestStation = stationName;
			}
			
			if (destTime < destinationMinTime) {
				destinationMinTime = destTime;
				destinationNearestStation = stationName;
			}
			
		}
		
		String[] results = new String[4];
		results[0] = originNearestStation;
		results[1] = String.valueOf(originMinTime);
		results[2] = destinationNearestStation;
		results[3] = String.valueOf(destinationMinTime);
				
		return results;
	}
	
	public static Route getRoute(String stationOrigin, String stationDestination, String time) {
		String stationOrig = stationOrigin.replaceAll(" ", "%20");
		String stationDest = stationDestination.replaceAll(" ", "%20");
		
		
		URL url = null;
		HttpURLConnection httpUrlConnection = null;
		try {
			url = new URL("http://www3.septa.org/hackathon/NextToArrive/" + stationOrig + "/" + stationDest + "/10");
			httpUrlConnection = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StringBuilder contents = new StringBuilder();
		
		try {
			Scanner in = new Scanner(httpUrlConnection.getInputStream());
			
			while(in.hasNextLine()){
				String line = in.nextLine();
				contents.append(line);
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		String routes = contents.toString();
		
		JSONArray routesArray = new JSONArray(routes);
		
		JSONObject routesObject = null;
		for (int i = 0; i < routesArray.length(); i++) {
			if (Septa.isAfter(routesArray.getJSONObject(i).getString("orig_departure_time"), time)) {
				routesObject = routesArray.getJSONObject(i);
				break;
			} else {
				i++;
			}
		}
		
		boolean isDirect = routesObject.getBoolean("isdirect");
		String origDepartureTime = routesObject.getString("orig_departure_time").trim();
		String origLine = routesObject.getString("orig_line").trim();
		String origTrain = routesObject.getString("orig_train").trim();
		String origArrivalTime = routesObject.getString("orig_arrival_time").trim();
				
		if (isDirect) {
			return new Route(isDirect, origDepartureTime, origLine, origTrain, origArrivalTime, stationOrigin, stationDestination);
		}
		else  {
			String connection = routesObject.getString("Connection").trim();
			String termDepartTime = routesObject.getString("term_depart_time").trim();
			String termLine = routesObject.getString("term_line").trim();
			String termTrain = routesObject.getString("term_train").trim();
			String termArrivalTime = routesObject.getString("term_arrival_time").trim();
						
			return new Route(isDirect, origDepartureTime, origLine, origTrain, origArrivalTime, connection, 
							termDepartTime, termLine, termTrain, termArrivalTime, stationOrigin, stationDestination);	
		}
	}
	
	public static boolean isAfter(String trainTime, String time) {
		String startTime = time.trim();
		String endTime = trainTime.trim();
		
		int startHour = Integer.valueOf(startTime.split(":")[0]);
		int startMinute = Integer.valueOf((startTime.split(":")[1].substring(0, 2)));
		String startHalf = startTime.split(":")[1].substring(2);
		
		int endHour = Integer.valueOf(endTime.split(":")[0]);
		int endMinute = Integer.valueOf((endTime.split(":")[1].substring(0, 2)));
		String endHalf = endTime.split(":")[1].substring(2);
				
		if (startHalf.equals("PM") && startHour < 12) {
			startHour += 12;
		} else if (startHalf.equals("AM") && startHour == 12) {
			startHour += 12;
		}
		
		if (endHalf.equals("PM") && endHour < 12) {
			endHour += 12;
		} else if (endHalf.equals("AM") && endHour == 12) {
			endHour += 12;
		}
				
		if (startHour < endHour) {
			return true;
		} else if (startHour > endHour) {
			return false;
		} else {
			if (startMinute < endMinute) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static double getCost(String stationOrigin, String stationDestination, boolean isWeekday, Route route) {
		SeptaStation origin = septaStations.get(stationOrigin);
		SeptaStation destination = septaStations.get(stationDestination);
		
		Zone originZone = origin.getZone();
		Zone destinationZone = destination.getZone();
		
		if (route.getIsDirect()) {
			if (originZone.equals(Zone.CENTER_CITY) || destinationZone.equals(Zone.CENTER_CITY)) {
				if (originZone.equals(Zone.CENTER_CITY)) {
					if (isWeekday) {
						return weekdayCosts.get(destinationZone);
					} else {
						return weekendCosts.get(destinationZone);
					}
				} else {
					if (isWeekday) {
						return weekdayCosts.get(originZone);
					} else {
						return weekendCosts.get(originZone);
					}
				}
			} else {
				return OUTLYING_COST;
			}
		} else {
			return VIA_CCP_COST;
		}
	}
}
