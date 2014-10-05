package com.crusoe.humanzombie;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;

import com.crusoe.humanzombie.library.Entities;
import com.crusoe.humanzombie.library.EntityManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ZombieActivity extends CoreActivity {

	private GoogleMap map;

	public static final String LAT_INTENT = "LAT";
	public static final String LONG_INTENT = "LONG";
	public static final String ID_INTENT = "ID";
	public static final String MAP_UPDATE_INTENT_FILTER = "HUMAN_ACTIVITY_LOCATION_FILTER";
	HashMap<String, Marker> listOfPoints = new HashMap<String, Marker>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps_layout);

		// set up action bar
		

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// fillData(this.getIntent());
		map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				fixZoom();
			}
		});

		Bundle b = this.getIntent().getExtras();

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
		if(m == null){
			ParseUser user = ParseUser.getCurrentUser();
			ParseGeoPoint geo = user.getParseGeoPoint("location");
		
			LatLng l = new LatLng((float) geo.getLatitude(), (float) geo.getLongitude());
			Marker newMarker = map.addMarker(new MarkerOptions()
			.position(l));
			listOfPoints.put("SELF", newMarker);
			
			m = newMarker;
		}
		if (counter > 0) {
			map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));

		} else {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15));

		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void fillData(Intent intent) {

		Entities e = EntityManager.getInstance().getCurrentEntity();

		double lat = e.getLat();
		double longit = e.getLongit();
		String id = e.getId();

		LatLng newLocation = new LatLng(lat, longit);

		if (listOfPoints.containsKey(id)) {
			Marker markerToBeReplaced = listOfPoints.get(id);
			markerToBeReplaced.remove();
			listOfPoints.remove(id);
		}
		Marker newMarker = map.addMarker(new MarkerOptions()
				.position(newLocation));
		listOfPoints.put(id, newMarker);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}
	@Override
	public void fillDataSwapEntity(Intent intent){
		super.fillDataSwapEntity(intent);
		String id = intent.getStringExtra("ID");
		if (listOfPoints.containsKey(id)) {
			Marker markerToBeReplaced = listOfPoints.get(id);
			markerToBeReplaced.remove();
			listOfPoints.remove(id);
		}
		
		Crouton.makeText(this, "Someone got turned into a zombie", Style.ALERT).show();
	
	
	}
	@Override
	public void fillDataDisableZombie(Intent intent){
		Crouton.makeText(this, "You are disabled.", Style.ALERT).show();
		//TODO: 
	}
}