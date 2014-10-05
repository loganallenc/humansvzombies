package com.crusoe.humanzombie.library;

public class DistanceConverter{
	
	public static final int TOO_CLOSE = 15;
	public static final int PROXIMITY = 75;
	
	public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371; //kilometers
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);

	    return dist;
	}
}