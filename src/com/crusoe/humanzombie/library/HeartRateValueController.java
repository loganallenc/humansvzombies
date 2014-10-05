package com.crusoe.humanzombie.library;

public class HeartRateValueController{
	public static int normalRateFrequency(){
		//this is how many times
		return 700;
	}
	public static int standardBeatLast(){
		return 100;
	}
	
	public static int fastRateFrequency(){
		return 300;
	}
	
	public enum HeartStatus{
		NO_ENEMY, ENEMY_PRESENT, ENEMY_CLOSE
	}
	private static HeartStatus currentHeartStatus = HeartStatus.NO_ENEMY;
	public static HeartStatus getHeartStatus(){
		return currentHeartStatus;
	}
	public static void setHeartStatus(HeartStatus h){
		currentHeartStatus = h;
	}
}