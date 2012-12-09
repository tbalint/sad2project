package test;

import java.util.ArrayList;
import java.util.List;

public class GeoLocator {
	public double latitude;		// N/S from equator
	public double longitude;	// E/W from Greenwich (London)
	public String description;
	
	public GeoLocator(double latitude, double longitude, String description) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = "close to " + description;
	}
	
	public static List<GeoLocator> locations;
	public static List<GeoLocator> invalidLocations;
	
	// class constructor
	static {
		locations = new ArrayList<GeoLocator>();
		
		locations.add(new GeoLocator(57.4667, 9.9833,  "Hjørring"));
		locations.add(new GeoLocator(57.05,   9.9333,  "Aalborg"));
		locations.add(new GeoLocator(56.35,   8.6333,  "Holstebro"));
		locations.add(new GeoLocator(56.4333, 9.4,     "Viborg"));
		locations.add(new GeoLocator(56.4667, 10.05,   "Randers"));
		
		locations.add(new GeoLocator(56.0833, 8.25,    "Ringkøbing"));
		locations.add(new GeoLocator(56.1333, 8.9833,  "Herning"));
		locations.add(new GeoLocator(56.1667, 9.5667,  "Silkeborg"));
		locations.add(new GeoLocator(56.15,   10.2167, "Aarhus"));
		locations.add(new GeoLocator(55.7333, 9.1167,  "Billund"));
		
		locations.add(new GeoLocator(55.7,    9.5333,  "Vejle"));
		locations.add(new GeoLocator(55.8719, 9.8811,  "Horsens"));
		locations.add(new GeoLocator(55.4667, 8.45  ,  "Esbjerg"));
		locations.add(new GeoLocator(55.5167, 9.4833,  "Kolding"));
		locations.add(new GeoLocator(55.35,   8.7667,  "Ribe"));
		
		locations.add(new GeoLocator(55.25,   9.5,     "Haderslev"));
		locations.add(new GeoLocator(54.9333, 8.9,     "Tønder"));
		locations.add(new GeoLocator(54.9167, 9.7833,  "Sønderborg"));
		locations.add(new GeoLocator(55.4,    10.3833, "Odense"));
		locations.add(new GeoLocator(55.2667, 9.9167,  "Assens"));
		
		locations.add(new GeoLocator(55.05,   10.6167, "Svendborg"));
		locations.add(new GeoLocator(55.9667, 12.0333, "Frederiksværk"));
		locations.add(new GeoLocator(56.0333, 12.6167, "Helsingør"));
		locations.add(new GeoLocator(55.6833, 11.1,    "Kalundborg"));
		locations.add(new GeoLocator(55.7167, 11.7167, "Holbæk"));
		
		locations.add(new GeoLocator(55.65,   12.0833, "Roskilde"));
		locations.add(new GeoLocator(55.6667, 12.5833, "København"));
		locations.add(new GeoLocator(55.4,    11.3667, "Slagelse"));
		locations.add(new GeoLocator(55.45,   11.8167, "Ringsted"));
		locations.add(new GeoLocator(55.45,   12.1833, "Køge"));
		
		locations.add(new GeoLocator(55.2333, 11.7667, "Næstved"));
		locations.add(new GeoLocator(54.8333, 11.15,   "Nakskov"));
		locations.add(new GeoLocator(55.1,    14.7,    "Rønne"));

		invalidLocations = new ArrayList<GeoLocator>();
		
		// Sweden
		invalidLocations.add(new GeoLocator(57.7167, 11.9667,  "Gothenburg"));
		invalidLocations.add(new GeoLocator(56.8989, 12.5019,  "Falkenberg"));
		invalidLocations.add(new GeoLocator(56.05,   12.7,     "Helsingborg"));
		invalidLocations.add(new GeoLocator(55.8667, 12.8333,  "Landskrona"));
		invalidLocations.add(new GeoLocator(55.6,    13,       "Malmö"));
		invalidLocations.add(new GeoLocator(55.4167, 13.8167,  "Ystad"));
		
		// 
		invalidLocations.add(new GeoLocator(54.3333, 10.1333,  "Kiel"));
		invalidLocations.add(new GeoLocator(54.0833, 12.1333,  "Rostock"));
		invalidLocations.add(new GeoLocator(54,      14.1333,  ""));
		invalidLocations.add(new GeoLocator(54,      16.1333,  ""));
		
		// Norway
		invalidLocations.add(new GeoLocator(58.1667,  8,       "Kristiansand"));
		invalidLocations.add(new GeoLocator(59.9167,  10.75,   "Oslo"));
		
		// United Kingdom
		invalidLocations.add(new GeoLocator(52.6333,  1.3,     "Norwich"));
		invalidLocations.add(new GeoLocator(51.7333,  0.4833,  "Chelmsford"));
		
	}
	
	public static String getNearestLocation(double lat, double lon) {
		String description = null;
		double shortestDistance = -1;
		double shortestInvalidDistance = -1;
		
		for(GeoLocator location : locations) {
			// a approximate calculation
			// - the small area of Denmark is almost a plane (2-dim), so you can ignore the fact, that it is lying on a globus
			// - distance between two neighboring latitudes is almost the double distance compared to longitudes (that's why we have the "*2")
			// - and the distance between two neighboring longitudes is almost 60 km
			double distance = Math.hypot( (lat-location.latitude) * 2, lon-location.longitude) * 60;
			
			// First run
			if(shortestDistance == -1) {
				shortestDistance = distance;
				description = location.description;
			}
			else {
				if(distance < shortestDistance) {
					shortestDistance = distance;
					description = location.description;		
				}
			}
		}
		
		for(GeoLocator inLocation : invalidLocations) {
			double distance = Math.hypot( (lat-inLocation.latitude) * 2, lon-inLocation.longitude) * 60;
			
			if(shortestInvalidDistance == -1) {
				shortestInvalidDistance = distance;
			}
			else {
				if(distance < shortestInvalidDistance) {
					shortestInvalidDistance = distance;
				}
			}
		}
		
		if(shortestInvalidDistance < shortestDistance)
			return null;
		
		return description + " - about " + String.format("%.2f", shortestDistance) + " km";
	}
}
