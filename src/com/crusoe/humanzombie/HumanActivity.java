package com.crusoe.humanzombie;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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


public class HumanActivity extends LocationActivity implements AnimatorListener, SensorEventListener {

	ImageView flingObj;
	FrameLayout mainScreen;
	GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	ImageView projectile;
	View.OnTouchListener gestureListener1;
	  private SensorManager mSensorManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		flingObj = (ImageView) findViewById(R.id.flingobject);
		mainScreen = (FrameLayout) findViewById(R.id.mainscreen);

		// flingObj.setClickable(true);
		// mainScreen.setOnTouchListener(new SwipeDetector(this, flingObj));
		gestureDetector = new GestureDetector(this, new GestureListener());
		// gestureListener1 = new TouchListener(mainScreen);

		// flingObj.setOnTouchListener(gestureListener1);
		projectile =  (ImageView) findViewById(R.id.flingobject);
		projectile.setVisibility(View.GONE);
		flingObj.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				//gestureDetector.onTouchEvent(event);
				
				ObjectAnimator flingAnimatorY = ObjectAnimator.ofFloat(
						flingObj, "translationY", originalY, originalY + 500 * 10);
				flingAnimatorY.setDuration(1000);
				flingAnimatorY.addListener(HumanActivity.this);
				flingAnimatorY.start();
				return true;
			}
			
			
		});
		  int[] posXY = new int[2];
		  flingObj.getLocationOnScreen(posXY);
		  
		    originalX = flingObj.getX() - flingObj.getMeasuredWidth();
			originalY = flingObj.getY()- flingObj.getMeasuredHeight();
			mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

	}
@Override

public void onResume() {

    super.onResume();
    // for the system's orientation sensor registered listeners

    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),

            SensorManager.SENSOR_DELAY_GAME);

}

	/**
	 * SWIPE
	 */
	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
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
					
					//math stuff. We want to calculate a vector
					
					
					ObjectAnimator flingAnimatorX = ObjectAnimator.ofFloat(
							flingObj, "translationX", originalX, originalX + distX * 10);
					ObjectAnimator flingAnimatorY = ObjectAnimator.ofFloat(
							flingObj, "translationY", originalY, originalY + distY * 10);
				
					//double angle = Math.atan(distY/distX);
					double theta = Math.toDegrees(Math.atan2(distY, distX));

					if (theta < 0.0) {
					    theta += 360.0;
					}
					//math calculations since we dont want to wait for the animaiton 
				Log.i("CurrentDegree", Float.toString(currentDegree));
				Log.i("Shot Degree", Double.toString(theta));
				
			
					degreeOfShot = currentDegree - theta;
					
					//animate it
					AnimatorSet as = new AnimatorSet();
					as.playTogether(flingAnimatorY, flingAnimatorX);
					as.setDuration(1000);
					as.addListener(HumanActivity.this);
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
		//mainScreen.invalidate();
		Log.e("END", "END");
		ObjectAnimator flingAnimatorX = ObjectAnimator.ofFloat(
				flingObj, "translationX", originalX, originalX);
		ObjectAnimator flingAnimatorY = ObjectAnimator.ofFloat(
				flingObj, "translationY", originalY, originalY);

		AnimatorSet as = new AnimatorSet();
		as.playTogether(flingAnimatorY, flingAnimatorX);
		as.setDuration(0);
		as.start();
		
		//TODO: Send off request
		
		//super.latitude; super.longitude;
		
		//get angle relative to the north
		  
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
}
