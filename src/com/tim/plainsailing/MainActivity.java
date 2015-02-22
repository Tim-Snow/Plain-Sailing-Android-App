package com.tim.plainsailing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
	}

	public void goPlay(View view){
		setContentView(R.layout.game_view);	
	}
		
	public void goShop(View view){
		
		
	}
	
	public void goAch(View view){
		
		
	}
	
	public void goSettings(View view){
		
		
	}
}
