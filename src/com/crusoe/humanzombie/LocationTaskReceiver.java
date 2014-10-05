package com.crusoe.humanzombie;

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
		// TODO Auto-generated method stub

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		// TODO Auto-generated method stub
		Log.i("Latitude", Double.toString(latitude));
		Log.i("Longitude", Double.toString(longitude));
	}
	double latitude = 0;
	double longitude = 0;
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