package com.crusoe.humanzombie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public abstract class CoreActivity extends Activity {

	protected Context context;

	private PendingIntent pendingIntent;
	private AlarmManager manager;

	public static final String LOCATION_ENTITY_UPDATE_FILTER = "NEW_ENTITY_INTENT_FILTER";

	public static final String ENTITY_SWAP_UPDATE_FILTER = "NEW_ENTITY_INTENT_FILTER";

	

	public static final String DISABLE_ZOMBIE_FILTER = "DISABLE_ZOMBIE_FILTER";
	public static final String HUMAN_KILLED_FILTER = "HUMAN_KILLED_FILTER";
	protected double latitude = 0, longitude = 0;

	private BroadcastReceiver newDataSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			fillData(intent);
		}
	};

	private BroadcastReceiver entitySwapSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			fillDataSwapEntity(intent);
		}
	};
	
	private BroadcastReceiver humanKilledSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			fillDataKillHuman(intent);
		}
	};
	private BroadcastReceiver disableZombieSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			fillDataDisableZombie(intent);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent alarmIntent = new Intent(this, LocationTaskReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

		Intent beatingIntent = new Intent(this, BeatingTaskReceiver.class);
		pendingBeatingIntent = PendingIntent.getBroadcast(this, 0,
				beatingIntent, 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		// stopService(new Intent(LocationActivity.this,
		// BackgroundLocationService.class));
		cancelLocationFetch();
		startLocationFetch(1000 * 10);

		cancelBeating();
		startBeating(1000);

		LocalBroadcastManager.getInstance(this).registerReceiver(
				newDataSyncReceiver,
				new IntentFilter(LOCATION_ENTITY_UPDATE_FILTER));
		LocalBroadcastManager.getInstance(this).registerReceiver(
				entitySwapSyncReceiver,
				new IntentFilter(ENTITY_SWAP_UPDATE_FILTER));
		LocalBroadcastManager.getInstance(this).registerReceiver(
				humanKilledSyncReceiver,
				new IntentFilter(HUMAN_KILLED_FILTER));
		LocalBroadcastManager.getInstance(this).registerReceiver(
				disableZombieSyncReceiver,
				new IntentFilter(DISABLE_ZOMBIE_FILTER));
		

	}

	@Override
	public void onPause() {
		super.onPause();
		cancelLocationFetch();
		startLocationFetch(1000 * 3);

		cancelBeating();
		startBeating(1000);

		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				newDataSyncReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				entitySwapSyncReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				humanKilledSyncReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				disableZombieSyncReceiver);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cancelLocationFetch();
		cancelBeating();
	}

	//
	public void startLocationFetch(int interval) {
		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		manager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), interval, pendingIntent);

	}

	public void cancelLocationFetch() {
		if (manager != null) {
			manager.cancel(pendingIntent);

		}
	}

	// vibrate
	private static boolean beating = false;

	public static void setBeating(boolean b) {
		beating = b;
	}

	public static boolean isBeating() {
		return beating;
	}

	private PendingIntent pendingBeatingIntent;

	public void startBeating(int interval) {
		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		manager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), interval, pendingBeatingIntent);

	}

	public void cancelBeating() {
		if (manager != null) {
			manager.cancel(pendingBeatingIntent);

		}
	}

	public void fillData(Intent intent) {

	}

	public void fillDataSwapEntity(Intent intent) {

	}
	public void fillDataKillHuman(Intent intent) {

	}
	public void fillDataDisableZombie(Intent intent) {

	}

}