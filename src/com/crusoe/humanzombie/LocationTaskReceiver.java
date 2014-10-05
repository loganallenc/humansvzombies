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
	LocationManager mLocationManager;
	Location loc;
	private static final int MinTime = 5000;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		locationManager = (LocationManager) arg0
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MinTime, 0, this);

		loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
        /*float bestAccuracy = -1f;
        if (loc.getAccuracy() != 0.0f && (loc.getAccuracy() < bestAccuracy) || bestAccuracy == -1f) {
            locationManager.removeUpdates(this);
        }
        bestAccuracy = loc.getAccuracy();*/
        
		userObject = ParseUser.getCurrentUser();

		//gps = new GPSTracker(arg0);
		if (loc != null) {
			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
		}

		if (latitude == 0.0 && longitude == 0.0) {
			Location l = getLastKnownLocation(arg0);
			if (l != null) {
				latitude = l.getLatitude();
				longitude = l.getLongitude();
			} else {
				return;
			}
		}

		ZombieActivity.updateSelf(latitude, longitude);
		geo = new ParseGeoPoint(latitude, longitude);

		Log.d("geo", Double.toString(geo.getLatitude()));
		Log.d("geo", Double.toString(geo.getLongitude()));

		userObject.put("location", geo);
		userObject.saveInBackground();

		Entities e = EntityManager.getInstance().getCurrentEntity();

		if (e != null) {
			findUsersInRadius(0.5, arg0);

			float distance = DistanceConverter.distFrom(e.getLat(),
					e.getLongit(), latitude, longitude);

			if (distance < DistanceConverter.TOO_CLOSE) {
				String userType = userObject.getString("playerType");
				System.out.println(userType);

				if (userType != null && userType.equals("Zombie")) {
					// push notification, user dead.

					e = EntityManager.getInstance().getCurrentEntity();

					JSONObject data;
					try {
						long timestamp = System.currentTimeMillis();
						data = new JSONObject("{\"name\": \"Death\","
								+ "\"alert\": \"ts:" + timestamp + "\n"
								+ "death: true " + "\"}");
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

	private Location getLastKnownLocation(Context ctx) {
		mLocationManager = (LocationManager) ctx.getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = mLocationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = mLocationManager.getLastKnownLocation(provider);
			// Log.d("last known location, provider: %s, location: %s",
			// provider, l);

			if (l == null) {
				continue;
			}
			if (bestLocation == null
					|| l.getAccuracy() < bestLocation.getAccuracy()) {
				// ALog.d("found best last known location: %s", l);
				bestLocation = l;
			}
		}
		if (bestLocation == null) {
			return null;
		}
		return bestLocation;

	}

	@Override
	public void onLocationChanged(Location location) {
	}

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
						Log.i("query",
								"User: " + userObject.getString("playerType"));

						String userType = userObject.getString("playerType");
						System.out.println(userType);

						if (userType != null && userType.equals("Human")) {
							if ("Zombie".equals(p.getString("playerType"))) {
								// sendIntent with p
								Log.d("nearby", p.getClassName());
								didIt = true;
								OverallStateController.getInstance()
										.updateOverallStatus(p);
								break;
							}
						} else {
							if ("Human".equals(p.getString("playerType"))) {
								// sendIntent with p
								Log.d("nearby", p.getClassName());
								didIt = true;
								OverallStateController.getInstance()
										.updateOverallStatus(p);
								break;
							}
						}
					}

					if (!didIt) {
						OverallStateController.getInstance()
								.updateOverallStatus(null);
						EntityManager.getInstance().replaceEntity(null);
					} else {
						EntityManager.getInstance().replaceEntity(p);
						// this means that we have a new nearby person. We will
						// use this to change
						ParseGeoPoint geo = p.getParseGeoPoint("location");
						float distance = DistanceConverter.distFrom(
								geo.getLatitude(), geo.getLongitude(),
								latitude, longitude);

						Log.i("Distance_TO_ENTITY", Float.toString(distance));
					}

					LocalBroadcastManager
							.getInstance(arg0)
							.sendBroadcast(
									new Intent(
											CoreActivity.LOCATION_ENTITY_UPDATE_FILTER));

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
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}