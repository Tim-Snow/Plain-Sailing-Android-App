package com.tim.plainsailing;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class Util {
	static private Context aContext;
	static MediaPlayer boostSound, dieSound, jumpSound, pickUpSound;
	public static boolean soundToggle = false,  fuelToggle = false;
	static final int upgradesSize = 5;
	
	static void loadSounds(Context context){
		aContext = context;
        boostSound 		= MediaPlayer.create(context, R.raw.boost);
        dieSound			= MediaPlayer.create(context, R.raw.explosion);
        jumpSound		= MediaPlayer.create(context, R.raw.jump);
        pickUpSound		= MediaPlayer.create(context, R.raw.pickup);
	}
	
	static void playSound(MediaPlayer sound){
		if(soundToggle){
			sound.start();
		}
	}
	
	static int getUpgradeLevel(int id){
		SharedPreferences sharedPref = aContext.getSharedPreferences( aContext.getString(R.string.upgrade_file_key), Context.MODE_PRIVATE );

		return (sharedPref.getInt(Integer.toString(id), 0) + 1);
	}
	
	static boolean collidesWith(Rect a, Rect b){
		return !(b.left > a.right	|| b.right  < a.left
		      || b.top  > a.bottom  || b.bottom < a.top );
	}
		
	static void saveUpgrades(Context context, int size, int[] upgrades){
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.upgrade_file_key), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		for(int i = 0; i < size; i++){
			editor.putInt(Integer.toString(i), upgrades[i]);
		}
		editor.commit();
	}
	
	static void savePlayerInfo(Context context, int[] info){
		int size = 2;
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.player_file_key), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		for(int i = 0; i < size; i++){
			editor.putInt(Integer.toString(i), info[i]);
		}
		editor.commit();
	}
	
	static void saveSettings(Context context, int size, int[] settings){
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.settings_file_key), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		for(int i = 0; i < size; i++){
			editor.putInt(Integer.toString(i), settings[i]);
		}
		editor.commit();
	}
	
	static int[] loadUpgradesFile(Context context, int size){
		int defaultUpgrade = 0;
		int[] currentUpgrades = new int[size];
		
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.upgrade_file_key), Context.MODE_PRIVATE );
				
		for(int i = 0; i < size; i++){
			currentUpgrades[i] = sharedPref.getInt(Integer.toString(i), defaultUpgrade);
		}
		
		return currentUpgrades;
	}
	
	static int[] loadPlayerInfo(Context context){
		int defaultInfo = 0;
		int size = 2; //[0] = currency [1] = high score
		int[] currentUpgrades = new int[size];
		
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.player_file_key), Context.MODE_PRIVATE );
				
		for(int i = 0; i < size; i++){
			currentUpgrades[i] = sharedPref.getInt(Integer.toString(i), defaultInfo);
		}
		
		return currentUpgrades;
	}
		
	static void loadSettingsFile(Context context, int size){
		int defaultSetting = 0;
		int[] theSettings  = new int[size];
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.settings_file_key), Context.MODE_PRIVATE );
		
		for(int i = 0; i < size; i++){
			theSettings[i] = sharedPref.getInt(Integer.toString(i), defaultSetting);
		}
	}
}
