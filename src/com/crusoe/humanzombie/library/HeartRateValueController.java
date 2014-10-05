package com.crusoe.humanzombie.library;

public class HeartRateValueController{
	public static int normalRateFrequency(){
		//this is how many milliseconds between beats
		return 700;
	}
	public static int standardBeatLast(){
		return 100;
	}
	
	public static int fastRateFrequency(){
		//this is how many milliseconds between beats
		return 300;
	}
	static Object lock = new Object();
	private static volatile HeartRateValueController instance = null;

	public static  HeartRateValueController getInstance() {
		HeartRateValueController r = instance;
		if (r == null) {
			synchronized (lock) { // while we were waiting for the lock, another
				r = instance; // thread may have instantiated the object
				if (r == null) {
					r = new HeartRateValueController();
					instance = r;
				}
			}
		}
		return r;
	}
}