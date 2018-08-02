package com.septassistant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.septassistant.model.NearestStationResult;
import com.septassistant.model.Route;
import com.septassistant.model.SeptaRoute;
import com.septassistant.model.SeptaStation;
import com.septassistant.model.Zone;

import org.json.JSONObject;
import org.json.JSONArray;

public class Septa {
	private static Map<String, SeptaStation> septaStations = new HashMap<String, SeptaStation>();
	private static Map<Zone, Double> weekdayCosts = new HashMap<Zone, Double>();
	private static Map<Zone, Double> weekendCosts = new HashMap<Zone, Double>();

	private static final double OUTLYING_COST = 3.75;
	private static final double VIA_CCP_COST = 9.25;
	
	private static Date date = null;
	private static boolean isWeekday = false;
	
	private static void init() {
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
			String name = splitted[3];
						
			Zone zone = null;
			
			switch(zoneInput) {
                case "1":
                    zone = Zone.ONE;
                    break;
                case "2":
                    zone = Zone.TWO;
                    break;
                case "3":
                    zone = Zone.THREE;
                    break;
                case "4":
                    zone = Zone.FOUR;
                    break;
                case "NJ":
                    zone = Zone.NJ;
                    break;
                case "CC":
                    zone = Zone.CENTER_CITY;
                default:
                    break;
            } 
					
			SeptaStation septaStation = 
			        new SeptaStation()
			        .name(name)
			        .zone(zone)
			        .coordinates(new LatLng(lat, lng));
			
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
		
		date = new Date();    
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        isWeekday = !(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}
	
	private static NearestStationResult getNearestStation(String origin, String destination) {			
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
		
		NearestStationResult result = new NearestStationResult().
		        originStation(originNearestStation).
		        originDuration(originMinTime).
		        destinationStation(destinationNearestStation).
		        destinationDuration(destinationMinTime);
				
		return result;
	}
	
	private static Route getRoute(String stationOrigin, String stationDestination, String time) {
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
			if (isAfter(routesArray.getJSONObject(i).getString("orig_departure_time"), time)) {
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
	
	private static boolean isAfter(String trainTime, String time) {
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
	
	private static double getCost(String stationOrigin, String stationDestination, boolean isWeekday, Route route) {
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
	
	private static String getTime(int originDuration) {
	    Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.SECOND, originDuration);
        gc.add(Calendar.MINUTE, 1);
        Date newDate = gc.getTime();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
        String format = dateFormat.format(newDate); 
        
        String fullTime = format.split(" ")[1];
        String half = format.split(" ")[2];
        String time = fullTime.substring(0, fullTime.lastIndexOf(":")) + half;
        
        return time; 
	}
	
	public static SeptaRoute getSeptaRoute(String origin, String destination) {
	    init();
        NearestStationResult stationResults = getNearestStation(origin, destination);
        
        String originStation = stationResults.getOriginStation();
        String destinationStation = stationResults.getDestinationStation();
                
        Route route = getRoute(originStation, destinationStation, getTime((int) stationResults.getOriginDuration()));
        double cost = getCost(originStation, destinationStation, isWeekday, route);
       
	    return new SeptaRoute().cost(cost).route(route);
	}
}
