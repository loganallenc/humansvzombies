package com.crusoe.humanzombie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
		startLocationFetch(1000 * 10);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		cancelLocationFetch();
		startLocationFetch(1000 * 30);
		   
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		cancelLocationFetch();
	}
	
	//
	public void startLocationFetch(int interval) {
	    manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

	    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	
	}
	public void cancelLocationFetch() {
	    if (manager != null) {
	        manager.cancel(pendingIntent);
	       
	    }
	}
}