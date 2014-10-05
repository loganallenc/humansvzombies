package com.crusoe.humanzombie;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.crusoe.humanzombie.library.DistanceConverter;
import com.crusoe.humanzombie.library.Entities;
import com.crusoe.humanzombie.library.EntityManager;
import com.crusoe.humanzombie.library.OverallStateController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class LocationTaskReceiver extends BroadcastReceiver implements
		LocationListener {
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	double latitude = 0;
	double longitude = 0;
	ParseUser userObject;
	ParseGeoPoint geoObject;
	ParseQuery<ParseUser> query;
	ParseGeoPoint geo;
	GPSTracker gps;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// For our recurring task, we'll just display a message
		// Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

		locationManager = (LocationManager) arg0
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		// Log.i("LocationTaskReceiver", "Location Request");

		userObject = ParseUser.getCurrentUser();

		gps = new GPSTracker(arg0);
		latitude = gps.getLatitude();
		longitude = gps.getLongitude();

		geo = new ParseGeoPoint(latitude, longitude);

		Log.d("geo", Double.toString(geo.getLatitude()));
		Log.d("geo", Double.toString(geo.getLongitude()));

		userObject.put("location", geo);
		userObject.saveInBackground();

		Entities e = EntityManager.getInstance().getCurrentEntity();
		
		if(e != null){
			findUsersInRadius(0.5, arg0);
			
			float distance = DistanceConverter.distFrom(e.getLat(), e.getLongit(), latitude, longitude);
			
			if (distance < DistanceConverter.TOO_CLOSE) {
				String userType = userObject.getString("playerType");
				System.out.println(userType);
				
				if (userType != null && userType.equals("Zombie")) {
					//push notification, user dead.
					
					e = EntityManager.getInstance().getCurrentEntity();
					
					JSONObject data;
					try {
						long timestamp = System.currentTimeMillis();
						data = new JSONObject(
								"{\"name\": \"Death\"," +
								"\"alert\": \"ts:" + timestamp + "\n" +
											"death: true " +
										"\"}"
						);
				        ParsePush push = new ParsePush();
				        push.setChannel("user_" + e.getId());
				        push.setData(data);
				        push.sendInBackground();
				        
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else {
			findUsersInRadius(0.5, arg0);
		}
	}

	LocationManager mLocationManager;

	@Override
	public void onLocationChanged(Location location) {}

	public void findUsersInRadius(double radius, final Context arg0) {

		query = ParseUser.getQuery();
		query.setLimit(10);
		query.whereWithinMiles("location", geo, radius);

		query.findInBackground(new FindCallback<ParseUser>() {

			ParseObject p;
			boolean didIt = false;
			
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				Log.i("query", "finding things!");

				if (e == null) {
					for (int x = 0; x < users.size(); x++) {
						p = users.get(x);
						Log.i("query", "Query: " + p.getString("playerType"));
						Log.i("query", "User: " + userObject.getString("playerType"));

						String userType = userObject.getString("playerType");
						System.out.println(userType);
						
						if (userType != null && userType.equals("Human")) {
							if ("Zombie".equals(p.getString("playerType"))) {
								// sendIntent with p
								Log.d("nearby", p.getClassName());
								didIt = true;
								OverallStateController.getInstance().updateOverallStatus(p);
								break;
							}
						} else {
							if ("Human".equals(p.getString("playerType"))) {
								// sendIntent with p
								Log.d("nearby", p.getClassName());
								didIt = true;
								OverallStateController.getInstance().updateOverallStatus(p);
								// x = parseObjects.size();
								break;
							}
						}
					}

					if (!didIt) {
						OverallStateController.getInstance().updateOverallStatus(null);
						EntityManager.getInstance().replaceEntity(null);
					}else{
						EntityManager.getInstance().replaceEntity(p);
						//launch broadcast to all activities to update now that we have updated the overall status of player
						LocalBroadcastManager.getInstance(arg0).sendBroadcast(
								new Intent(CoreActivity.ENTITY_SWAP_UPDATE_FILTER));
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