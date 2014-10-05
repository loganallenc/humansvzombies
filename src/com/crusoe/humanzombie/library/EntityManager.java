package com.crusoe.humanzombie.library;

import com.crusoe.humanzombie.library.OverallStateController.OverallStatus;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class EntityManager {
	static Object lock = new Object();
	private static volatile EntityManager instance = null;

	Entities currentEntity = new Entities();

	public static  EntityManager getInstance() {
		EntityManager r = instance;
		if (r == null) {
			synchronized (lock) { // while we were waiting for the lock, another
				r = instance; // thread may have instantiated the object
				if (r == null) {
					r = new EntityManager();
					instance = r;
				}
			}
		}
		return r;
	}

	public void replaceEntity(ParseObject o) {

		if(o == null){
			currentEntity = null;
			return;
		}
		Entities e = new Entities();

		ParseGeoPoint geo = o.getParseGeoPoint("location");

		e.setLat(geo.getLatitude());
		e.setLongit(geo.getLongitude());
		e.setId(o.getObjectId());

		///listOfEntities.add(e);
		currentEntity = e;
	}

	public void deleteEntity() {
	currentEntity = null;
	}

	public Entities getCurrentEntity(){
		return currentEntity;
	}

}