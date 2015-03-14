package com.tim.plainsailing;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.os.Bundle;

public class SettingsActivity extends ActionBarActivity {
	CheckBox soundToggle, fuelToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		fuelToggle = (CheckBox) findViewById(R.id.fuelToggle);
		soundToggle = (CheckBox) findViewById(R.id.volumeToggle);
	}
	
	public void apply(View view){
		if( soundToggle.isChecked()){
			Util.soundToggle = true;
		} else {
			Util.soundToggle = false;		
		}
		
		if( !fuelToggle.isChecked()){
			Util.fuelToggle = true;
		} else {
			Util.fuelToggle = false;		
		}
	}
}
