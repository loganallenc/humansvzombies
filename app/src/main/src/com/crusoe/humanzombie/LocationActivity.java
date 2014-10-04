package com.crusoe.humanzombie;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public abstract class LocationActivity extends Activity implements
		LocationListener {
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;


	protected double latitude = 0, longitude = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// update every time your location changes
		Log.i("Latitude", Double.toString(latitude));
		Log.i("Longitude", Double.toString(longitude));
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("Latitude", "disable");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "enable");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "status");
	}
}