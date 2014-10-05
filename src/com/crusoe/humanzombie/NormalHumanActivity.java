package com.crusoe.humanzombie;

import com.crusoe.humanzombie.library.Entities;
import com.crusoe.humanzombie.library.EntityManager;
import com.crusoe.humanzombie.library.OverallStateController;
import com.crusoe.humanzombie.library.OverallStateController.OverallStatus;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.content.Intent;
import android.os.Bundle;

public class NormalHumanActivity extends CoreActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.normal_human);
		// set up out views
	}

	@Override
	public void fillData(Intent intent) {
		super.fillData(intent);
		// should we switch to weapon mode?
		if(!OverallStateController.getInstance().getStatus().equals(OverallStatus.NO_ENEMY)){
			Intent i = new Intent(this, HumanWeaponsActivity.class);
			startActivity(i);
		}
	}
	
	@Override
	public void fillDataSwapEntity(Intent intent){
		super.fillDataSwapEntity(intent);
		String id = intent.getStringExtra("ID");
		
		if(EntityManager.getInstance().getCurrentEntity() != null &&EntityManager.getInstance().getCurrentEntity().getId().equals(id)){
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