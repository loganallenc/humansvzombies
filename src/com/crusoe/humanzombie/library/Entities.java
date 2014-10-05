package com.crusoe.humanzombie.library;

public class Entities{
	private double lat, longit;
	private String id;
	private EntityType TYPE;
	public enum EntityType{
		ZOMBIE, HUMAN;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLongit() {
		return longit;
	}
	public void setLongit(double longit) {
		this.longit = longit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public EntityType getTYPE() {
		return TYPE;
	}
	public void setTYPE(EntityType tYPE) {
		TYPE = tYPE;
	}
	public void setTYPE(String type) {
		if(type.equals("human")){
			setTYPE(EntityType.HUMAN);
		}else{
			setTYPE(EntityType.ZOMBIE);
		}
	}
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Entities)){
			return false;
		}
		Entities e = (Entities) o;
		if(e.id.equals(this.id)){
			return true;
		}
		return false;
	}
	
	public static float distanceFrom(Entities e1, Entities e2){

		float distance = DistanceConverter.distFrom( e1.getLat(),
				 e1.getLongit(),  e2.getLat(),  e2.getLongit());
		float absDistance = Math.abs(distance);
		return absDistance;
	}
	public static float distanceFrom(Entities e1, double lat, double longit){

		float distance = DistanceConverter.distFrom( e1.getLat(),
				 e1.getLongit(),  lat,  longit);
		float absDistance = Math.abs(distance);
		return absDistance;
	}
}