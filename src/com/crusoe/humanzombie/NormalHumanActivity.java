package com.crusoe.humanzombie;

import com.crusoe.humanzombie.library.Entities;
import com.crusoe.humanzombie.library.EntityManager;
import com.crusoe.humanzombie.library.OverallStateController;
import com.crusoe.humanzombie.library.OverallStateController.OverallStatus;
import com.parse.ParsePush;
import com.parse.ParseUser;

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
	
	

}