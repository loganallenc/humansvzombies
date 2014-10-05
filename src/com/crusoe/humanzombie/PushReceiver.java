package com.crusoe.humanzombie;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class PushReceiver extends ParsePushBroadcastReceiver {
	
	@Override
	protected void onPushReceive(Context context, Intent intent) {
		super.onPushReceive(context, intent);
		
		String action = intent.getStringExtra("action");
		String alert = intent.getStringExtra("alert");
		String title = intent.getStringExtra("title");

		Log.i("pushReceiver", action);
		Log.i("pushReceiver", alert);
		Log.i("pushReceiver", title);
		
		if (title.equals("HtoZ")) {
			
		} else if (title.equals("Gunshot")) {
			String[] lines = alert.split("\n");
			String[] keyVal = lines[0].split(":");
			long timestamp = Long.parseLong(keyVal[1]);
			keyVal = lines[1].split(":");
			boolean hit = Boolean.parseBoolean(keyVal[1]);
			
			if (hit) {
				//zombie disabled
				//Add some state that means you can't attack.
				
				JSONObject data;
				try {
					data = new JSONObject(
							"{\"name\": \"ZDisabled\"," +
							"\"alert\": \"ts:" + timestamp + "\n" +
										"id:" + ParseUser.getCurrentUser().getObjectId() +
									"\"}"
					);
					ParsePush push = new ParsePush();
		        	push.setChannel("Humans");
		        	push.setData(data);
		        	push.sendInBackground();
		        	
		        	//this should only send out to zombies to disable them
		        	LocalBroadcastManager.getInstance(context).sendBroadcast(
							new Intent(CoreActivity.DISABLE_ZOMBIE_FILTER));
					
		        	
				} catch (JSONException e) {}
			}
		} else if (title.equals("ZDisabled")) {
			String[] lines = alert.split("\n");
			String[] keyVal = lines[0].split(":");
			long timestamp = Long.parseLong(keyVal[1]);
			keyVal = lines[1].split(":");
			String id = keyVal[1];
			
			Intent i = new Intent(CoreActivity.LOCATION_ENTITY_UPDATE_FILTER);
			i.putExtra("ID", id);
			LocalBroadcastManager.getInstance(context).sendBroadcast(
					i);
			
			//Remove zombie marker from map using ID.
		}  else if (title.equals("HTurned")) {
			//Human turned into zombie.
			
			String[] lines = alert.split("\n");
			String[] keyVal = lines[0].split(":");
			long timestamp = Long.parseLong(keyVal[1]);
			keyVal = lines[1].split(":");
			String id = keyVal[1];
			
			Intent i = new Intent(CoreActivity.LOCATION_ENTITY_UPDATE_FILTER);
			i.putExtra("ID", id);
			LocalBroadcastManager.getInstance(context).sendBroadcast(i);
			
			//Remove human marker from map using ID.
		} else if (title.equals("Death")) {
			//human killed :)
			LocalBroadcastManager.getInstance(context).sendBroadcast(
					new Intent(CoreActivity.HUMAN_KILLED_FILTER));
			
			//convert human to zombie.
			
		}
	}
	
}
