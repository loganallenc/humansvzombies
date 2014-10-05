package com.crusoe.humanzombie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ZombieActivity extends CoreActivity {

	private GoogleMap map;

	public static final String LAT_INTENT = "LAT";
	public static final String LONG_INTENT = "LONG";
	public static final String ID_INTENT = "ID";
	public static final String MAP_UPDATE_INTENT_FILTER = "HUMAN_ACTIVITY_LOCATION_FILTER";
	HashMap<String, Marker> listOfPoints = new HashMap<String, Marker>();

	private BroadcastReceiver newDataSyncreceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			fillData(intent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps_layout);

		// set up action bar
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(true);

		getActionBar().setTitle("Location");
		getActionBar().setDisplayUseLogoEnabled(false);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		fillData(this.getIntent());
		map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
		    @Override
		    public void onMapLoaded() {
		    	fixZoom();
		    }
		});
		
	}
	

	private void fixZoom() {
		// List<LatLng> points = route.getPoints(); // route is instance of
		// PolylineOptions
		int counter = 0;
		LatLngBounds.Builder bc = new LatLngBounds.Builder();
		Marker m = null;
		for (Map.Entry<String, Marker> entry : listOfPoints.entrySet()) {
			 m = (Marker) entry.getValue();
			bc.include(m.getPosition());
			counter++;

		}
		if (counter > 0) {
			map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));

		} else {
			map.moveCamera(CameraUpdateFactory
					.newLatLngZoom(m.getPosition(), 8));

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this)
				.registerReceiver(newDataSyncreceiver,
						new IntentFilter(MAP_UPDATE_INTENT_FILTER));
	}

	@Override
	public void onPause() {
		super.onPause();

		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				newDataSyncreceiver);

	}

	public void fillData(Intent intent) {
		double lat = this.getIntent().getDoubleExtra(LAT_INTENT, 0);
		double longit = this.getIntent().getDoubleExtra(LONG_INTENT, 0);
		String id = this.getIntent().getStringExtra(ID_INTENT);

		LatLng newLocation = new LatLng(lat, longit);
		if (id == null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			id = dateFormat.format(date);
		}

		if (listOfPoints.containsKey(id)) {
			Marker markerToBeReplaced = listOfPoints.get(id);
			markerToBeReplaced.remove();
			listOfPoints.remove(id);
		}
		Marker newMarker = map.addMarker(new MarkerOptions()
				.position(newLocation));
		listOfPoints.put(id, newMarker);
	}

	public void reloadMap() {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

}