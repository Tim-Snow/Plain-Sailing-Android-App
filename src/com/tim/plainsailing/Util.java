package com.tim.plainsailing;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;

public class Util {

	static final int upgradesSize = 5;
	
	static boolean collidesWith(Rect a, Rect b){
		return !(b.left > a.right	|| b.right  < a.left
		      || b.top  > a.bottom  || b.bottom < a.top );
	}
	
	//get set high score
	//get set currency
	
	static void saveUpgrades(Context context, int size, int[] upgrades){
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.upgrade_file_key), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		for(int i = 0; i < size; i++){
			editor.putInt(Integer.toString(i), upgrades[i]);
		}
		editor.commit();
	}
	
	static void saveSettings(Context context, int size, int[] upgrades){
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.settings_file_key), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		for(int i = 0; i < size; i++){
			editor.putInt(Integer.toString(i), upgrades[i]);
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
		
	static void loadSettingsFile(Context context, int size){
		int defaultSetting = 0;
		int[] theSettings  = new int[size];
		SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.settings_file_key), Context.MODE_PRIVATE );
		
		for(int i = 0; i < size; i++){
			theSettings[i] = sharedPref.getInt(Integer.toString(i), defaultSetting);
		}
	}
}
