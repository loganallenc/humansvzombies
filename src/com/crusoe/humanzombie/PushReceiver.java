package com.crusoe.humanzombie;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

public class PushReceiver extends ParsePushBroadcastReceiver {
	
	@Override
	protected void onPushReceive(Context context, Intent intent) {
		super.onPushReceive(context, intent);
		
		String action = intent.getStringExtra("alert");
		String alert = intent.getStringExtra("alert");
		String title = intent.getStringExtra("title");

	}
	
}
