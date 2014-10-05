package com.crusoe.humanzombie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;
import com.parse.ParsePush;
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

	}

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
		userObject.put("playerType", "Human");
		userObject.saveInBackground();
		
		ParsePush.subscribeInBackground("user_" + ParseUser.getCurrentUser().getObjectId());
		ParsePush.subscribeInBackground("Humans");
		ParsePush.unsubscribeInBackground("Zombies");
		Intent intent = new Intent(this, NormalHumanActivity.class);
		startActivity(intent);
	}

	public void launchZombie(View v) {
		userObject.put("playerType", "Zombie");
		userObject.saveInBackground();
		
		ParsePush.subscribeInBackground("user_" + ParseUser.getCurrentUser().getObjectId());
		ParsePush.subscribeInBackground("Zombies");
		ParsePush.unsubscribeInBackground("Humans");
		Intent intent = new Intent(this, ZombieActivity.class);
		startActivity(intent);
	}
}
