package com.crusoe.humanzombie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class LocationActivity extends Activity  {
	
	protected Context context;
	
	private PendingIntent pendingIntent;
	private AlarmManager manager;

	protected double latitude = 0, longitude = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		Intent alarmIntent = new Intent(this, LocationTaskReceiver.class);
	    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
	}

	@Override
	public void onResume(){
		super.onResume();
	//	stopService(new Intent(LocationActivity.this, BackgroundLocationService.class));
		cancelLocationFetch();
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		startLocationFetch();
		   
	}
	
	//
	public void startLocationFetch() {
	    manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    int interval = 1000*3;

	    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	
	}
	public void cancelLocationFetch() {
	    if (manager != null) {
	        manager.cancel(pendingIntent);
	       
	    }
	}
}