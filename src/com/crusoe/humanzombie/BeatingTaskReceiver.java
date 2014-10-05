package com.crusoe.humanzombie;

import com.crusoe.humanzombie.library.HeartRateValueController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

public class BeatingTaskReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// For our recurring task, we'll just display a message
		// Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
		Log.i("Beating task receiver", "beating task receiver");
		if (CoreActivity.isBeating()) {
			Vibrator v = (Vibrator) arg0
					.getSystemService(Context.VIBRATOR_SERVICE);
		
				v.vibrate(HeartRateValueController.standardBeatLast());
			
		}

	}

}