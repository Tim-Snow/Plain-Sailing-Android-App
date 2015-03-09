package com.tim.plainsailing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {	
	final static String tag = "Plain_Sailing";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
	}

	public void goPlay(View view){ 
		Intent i = new Intent(getApplicationContext(), GameActivity.class);
		startActivity(i);
	}
		
	public void goShop(View view){ 
		Intent i = new Intent(getApplicationContext(), UpgradeListActivity.class);
		startActivity(i);
	}
	
	public void goAch(View view){	}
	
	public void goSettings(View view){	}
	
}
