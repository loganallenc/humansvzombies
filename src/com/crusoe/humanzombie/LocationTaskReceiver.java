package com.crusoe.humanzombie;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationTaskReceiver extends BroadcastReceiver implements LocationListener {
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	double latitude = 0;
	double longitude = 0;
	ParseUser userObject;
	ParseGeoPoint geoObject;
	ParseQuery<ParseObject> query;
	
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
      //  Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
    	
    	locationManager = (LocationManager) arg0.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		Log.i("LocationTaskReceiver", "Location Request");
    }
    
    @Override
	public void onLocationChanged(Location location) {
		userObject = ParseUser.getCurrentUser();
		
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
		Log.i("Latitude", Double.toString(latitude));
		Log.i("Longitude", Double.toString(longitude));
		
		geoObject = new ParseGeoPoint(latitude, longitude);

		userObject.put("location", geoObject);
		userObject.saveEventually();
		
		findUsersInRadius(0.5);
	}
	
	public void findUsersInRadius(double radius) {
		ParseQuery.clearAllCachedResults();
		
		query = ParseQuery.getQuery("Users");
		query.setLimit(10);
		query.whereWithinMiles("location", geoObject, radius);

		query.findInBackground(new FindCallback<ParseObject>() {

			ParseObject p;

			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				if (e == null) {
					for (int x = 0; x < parseObjects.size(); x++) {
						p = parseObjects.get(x);
						
						if (p.getString("playerType").equals("Human")) {
							if ("Zombies".equals(p.getString("playerType"))) {
								//sendIntent with p
								Log.d("nearby", p.getClassName());
								x = parseObjects.size();
							}
						} else {
							if ("Humans".equals(p.getString("playerType"))) {
								//sendIntent with p
								Log.d("nearby", p.getClassName());
								x = parseObjects.size();
							}
						}
					}
				} else {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}