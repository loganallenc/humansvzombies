package com.crusoe.humanzombie;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MainActivity extends Activity {

	ParseUser userObject;
	ParseGeoPoint geo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParseAnalytics.trackAppOpened(getIntent());

		userObject = ParseUser.getCurrentUser();

		//nearMe();
	}
/*
	public void nearMe() {
		GPSTracker gps = new GPSTracker(this);
		double latitude = gps.getLatitude();
		double longitude = gps.getLongitude();

		geo = new ParseGeoPoint(latitude, longitude);

		// geo = user.getParseGeoPoint("location");
		userObject.put("location", geo);
		userObject.saveEventually();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("location");
		query.whereWithinMiles("location", geo, 0.5);
		query.setLimit(10);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parseObjects, ParseException e) {
				if (e == null) {
					for (ParseObject p : parseObjects) {
						Log.d("near", p.getObjectId());
					}
				} else {
					System.out.println(e);
				}
			}
		});
	}

*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	public void launchHuman(View v) {
		ParsePush.subscribeInBackground("user_" + ParseUser.getCurrentUser().getObjectId());
		ParsePush.subscribeInBackground("Humans");
		ParsePush.unsubscribeInBackground("Zombies");
		Intent intent = new Intent(this, HumanActivity.class);
		startActivity(intent);
	}

	public void launchZombie(View v) {
		ParsePush.subscribeInBackground("user_" + ParseUser.getCurrentUser().getObjectId());
		ParsePush.subscribeInBackground("Zombies");
		ParsePush.unsubscribeInBackground("Humans");
		Intent intent = new Intent(this, ZombieActivity.class);
		startActivity(intent);
	}
}
