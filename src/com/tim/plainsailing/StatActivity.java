package com.tim.plainsailing;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stat);
		
		this.setTitle("Statistics");
		int playerInfo[] = new int[2];
		playerInfo = Util.loadPlayerInfo(getApplicationContext());
		((TextView) this.findViewById(R.id.theScore)).setText(Integer.toString(playerInfo[1]));	
	}
}
