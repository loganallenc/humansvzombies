package com.crusoe.humanzombie;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

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
	}
	
}
