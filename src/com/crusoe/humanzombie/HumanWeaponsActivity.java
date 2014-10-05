package com.crusoe.humanzombie;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.crusoe.humanzombie.library.EntityManager;
import com.crusoe.humanzombie.library.OverallStateController;
import com.crusoe.humanzombie.library.OverallStateController.OverallStatus;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class HumanWeaponsActivity extends CoreActivity implements AnimatorListener,
		SensorEventListener {

	ImageView flingObj;
	FrameLayout mainScreen;
	GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	ImageView flash;
	View.OnTouchListener gestureListener1;
	private SensorManager mSensorManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		flingObj = (ImageView) findViewById(R.id.gun);
		mainScreen = (FrameLayout) findViewById(R.id.mainscreen);

		// flingObj.setClickable(true);
		// mainScreen.setOnTouchListener(new SwipeDetector(this, flingObj));
		gestureDetector = new GestureDetector(this, new GestureListener());
		// gestureListener1 = new TouchListener(mainScreen);

		// flingObj.setOnTouchListener(gestureListener1);
		flash = (ImageView) findViewById(R.id.flash);
		flash.setVisibility(View.GONE);
		flingObj.setOnTouchListener(new OnTouch());
		int[] posXY = new int[2];
		flingObj.getLocationOnScreen(posXY);

		originalX = flingObj.getX() - flingObj.getMeasuredWidth();
		originalY = flingObj.getY() - flingObj.getMeasuredHeight();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

	}

	@Override
	public void onResume() {

		super.onResume();
		// for the system's orientation sensor registered listeners

	}

	/**
	 * SWIPE
	 */
	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {

			return super.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			boolean result = false;
			try {
				float distY = e2.getY() - e1.getY();
				float distX = e2.getX() - e1.getX();

				// Get the Y OFfset
				DisplayMetrics displayMetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(
						displayMetrics);
				int offsetY_1 = displayMetrics.heightPixels
						- mainScreen.getMeasuredHeight();

				int[] location = new int[2];
				flingObj.getLocationOnScreen(location);

				float orgX = location[0];
				float orgY = location[1] - offsetY_1;

				float stopX = orgX + distX;
				float stopY = orgY + distY;

				// check velocity
				float totalVelocity = (float) Math.sqrt(Math.abs(velocityX)
						* Math.abs(velocityX) + Math.abs(velocityY)
						* Math.abs(velocityY));
				if (totalVelocity > SWIPE_VELOCITY_THRESHOLD) {
					// we swiped!

					// math stuff. We want to calculate a vector

					ObjectAnimator flingAnimatorX = ObjectAnimator.ofFloat(
							flingObj, "translationX", originalX, originalX
									+ distX * 10);
					ObjectAnimator flingAnimatorY = ObjectAnimator.ofFloat(
							flingObj, "translationY", originalY, originalY
									+ distY * 10);

					// double angle = Math.atan(distY/distX);
					double theta = Math.toDegrees(Math.atan2(distY, distX));

					if (theta < 0.0) {
						theta += 360.0;
					}
					// math calculations since we dont want to wait for the
					// animaiton
					Log.i("CurrentDegree", Float.toString(currentDegree));
					Log.i("Shot Degree", Double.toString(theta));

					degreeOfShot = currentDegree - theta;

					ParseGeoPoint geo = ParseUser.getCurrentUser().getParseGeoPoint("location");
					
					ParseQuery<ParseInstallation> query = ParseQuery.getQuery("Users");
					query.whereWithinMiles("location", geo, 0.15);
					query.setLimit(1);
					query.findInBackground(new FindCallback<ParseInstallation>() {

						@Override
						public void done(List<ParseInstallation> parseIns, ParseException e) {
							if (e == null) {
								for (ParseInstallation p : parseIns) {
									
									ParseGeoPoint target = p.getParseGeoPoint("location");
									double tLat = target.getLatitude();
									double tLong = target.getLongitude();
									
									Log.i("targetLat", Double.toString(tLat));
									Log.i("targetLong", Double.toString(tLong));

									double pAngle = Math.atan2(tLong - longitude, tLat - latitude) * 180 / Math.PI;
									
									boolean hit = pAngle > degreeOfShot - 60 || pAngle < degreeOfShot + 60;
									
									JSONObject data;
									try {
										long timestamp = System.currentTimeMillis();
										data = new JSONObject(
												"{\"name\": \"Title\"," +
												"\"alert\": \"ts:" + timestamp + "\n" +
															"hit:" + hit +
														"\"}"
										);
								        ParsePush push = new ParsePush();
								        push.setChannel("user_" + p.getObjectId());
								        push.setData(data);
								        push.sendInBackground();
								        
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
									
									Log.d("near", p.getObjectId());
								}
							} else {
								System.out.println(e);
							}
						}
					});
					
					// animate it
					AnimatorSet as = new AnimatorSet();
					as.playTogether(flingAnimatorY, flingAnimatorX);
					as.setDuration(1000);
					as.addListener(HumanWeaponsActivity.this);
					as.start();

				}

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public boolean onSwipeRight() {

		return false;
	}

	public boolean onSwipeLeft() {
		return false;
	}

	public boolean onSwipeTop() {
		return false;
	}

	public boolean onSwipeBottom() {
		return false;
	}

	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animator animation) {
		// mainScreen.invalidate();
		/*
		 * Log.e("END", "END"); ObjectAnimator flingAnimatorX =
		 * ObjectAnimator.ofFloat(flingObj, "translationX", originalX,
		 * originalX); ObjectAnimator flingAnimatorY =
		 * ObjectAnimator.ofFloat(flingObj, "translationY", originalY,
		 * originalY);
		 * 
		 * AnimatorSet as = new AnimatorSet(); as.playTogether(flingAnimatorY,
		 * flingAnimatorX); as.setDuration(0); as.start();
		 */
		flash.setOnTouchListener(new OnTouch());
		flash.setVisibility(View.GONE);
		// TODO: Send off request

		// super.latitude; super.longitude;

		// get angle relative to the north

	}

	float originalX = 0;
	float originalY = 0;

	double degreeOfShot = 0.0;

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub

	}

	private float currentDegree = 0.0f;

	@Override
	public void onSensorChanged(SensorEvent event) {

		currentDegree = Math.round(event.values[0]);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private class OnTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			 MediaPlayer mp = MediaPlayer.create(HumanWeaponsActivity.this, R.raw.shotty);
			   mp.start();
			flash.setVisibility(View.VISIBLE);
			flash.setOnTouchListener(null);

			ObjectAnimator fadeOut = ObjectAnimator.ofFloat(flash, "alpha", 1f,
					.3f);
			fadeOut.setDuration(600);

			 AnimatorSet mAnimationSet = new AnimatorSet();

			mAnimationSet.play(fadeOut);

			mAnimationSet.addListener(HumanWeaponsActivity.this);
			mAnimationSet.start();
			return false;
		}

	}
	
	@Override
	public void fillData(Intent intent) {
		super.fillData(intent);
		// should we switch to weapon mode?
		if(OverallStateController.getInstance().getStatus().equals(OverallStatus.NO_ENEMY)){
			Intent i = new Intent(this, NormalHumanActivity.class);
			startActivity(i);
		}
	}
	
	@Override
	public void fillDataSwapEntity(Intent intent){
		super.fillDataSwapEntity(intent);
		String id = intent.getStringExtra("ID");
		
		if(EntityManager.getInstance().getCurrentEntity().getId().equals(id)){
			EntityManager.getInstance().replaceEntity(null);
		}
		//if we got swapped, then we go straight to the zombie activity
		Crouton.makeText(this, "You shot a zombie", Style.ALERT).show();
	}
	
	@Override
	public void fillDataKillHuman(Intent intent){
		Crouton.makeText(this, "A zombie killed you. You are now one of them!", Style.ALERT).show();
		Intent i = new Intent(this, ZombieActivity.class);
		context.startActivity(i);
		
	}
}
