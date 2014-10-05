package com.crusoe.humanzombie.library;

import android.location.Location;
import android.util.Log;

public class DistanceConverter{
	
	public static final int TOO_CLOSE = 12;
	public static final int PROXIMITY = 75;
	
	public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
	   /* double earthRadius = 6371; //kilometers
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);

	    
	    return dist*100;*/
		Log.i("Lat1", Double.toString(lat1));
		Log.i("Long1", Double.toString(lng1));
		
		Log.i("Lat2", Double.toString(lat2));
		Log.i("Long2", Double.toString(lng2));
		float[] results = new float[3];
		Location.distanceBetween(lat1, lng1, lat2, lng2, results);
		for(int i = 0; i < results.length; i++){
			Log.i("DistanceFrom", Float.toString(results[i]));
		}
		return results[0];
	}
}