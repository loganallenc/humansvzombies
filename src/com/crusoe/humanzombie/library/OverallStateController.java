package com.crusoe.humanzombie.library;

import com.crusoe.humanzombie.library.Entities.EntityType;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class OverallStateController {
	public enum OverallStatus {
		NO_ENEMY, ENEMY_PRESENT, ENEMY_CLOSE
	}

	static Object lock = new Object();
	private static volatile OverallStateController instance = null;

	public static OverallStateController getInstance() {
		OverallStateController r = instance;
		if (r == null) {
			synchronized (lock) { // while we were waiting for the lock, another
				r = instance; // thread may have instantiated the object
				if (r == null) {
					r = new OverallStateController();
					instance = r;
				}
			}
		}
		return r;
	}

	public OverallStatus evaluateOverallStatus(float distance) {
		if (distance > DistanceConverter.TOO_CLOSE) {
			return OverallStatus.ENEMY_PRESENT;
		} else {
			return OverallStatus.ENEMY_CLOSE;
		}
	}

	OverallStatus status = OverallStatus.NO_ENEMY;

	public void setStatus(OverallStatus status2) {
		status = status2;
	}

	public void updateOverallStatus(ParseObject o) {

		if(o == null){
			status = OverallStatus.NO_ENEMY;
			return;
		}
		Entities e = new Entities();

		ParseGeoPoint geo = o.getParseGeoPoint("location");

		e.setLat(geo.getLatitude());
		e.setLongit(geo.getLongitude());
		e.setId(o.getObjectId());

		// is this the other person?

		// we will update our status based on this
		OverallStatus status_from_entity = evaluateOverallStatus(e);
		if (status_from_entity.equals(OverallStatus.ENEMY_CLOSE)
				&& status.equals(OverallStatus.ENEMY_PRESENT)) {
			status = OverallStatus.ENEMY_CLOSE;
		}else{
			status = OverallStatus.ENEMY_PRESENT;
		}

	}
	public OverallStatus getStatus(){
		return status;
	}

	private OverallStatus evaluateOverallStatus(Entities e) {
		ParseGeoPoint l = ParseUser.getCurrentUser().getParseGeoPoint(
				"location");
		double absDist = Math.abs(Entities.distanceFrom(e, l.getLatitude(),
				l.getLongitude()));

		return evaluateOverallStatus((float) absDist);

	}
}