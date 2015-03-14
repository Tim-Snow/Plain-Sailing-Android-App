package com.tim.plainsailing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class GameInterface {
	String	scoreString, 	countdownText;
	
	int 	screenW, 			screenH, 			screenWPortion, 	screenHPortion,
			fuelMeterBot, 	fuelMeterTop, 	fuelRange,				startCountdown;
	
	Rect 	fuelMeter, 		fuelMeterBg;
	Paint 	fuelBgPaint, 	fuelPaint,			textPaint,				countdownPaint;

	public boolean countdownShown;
	
	public GameInterface(int w, int h, Context context){
		scoreString 				= null;
		screenH 						= h;
		screenW 					= w;
		screenWPortion 		= w / 20;
		screenHPortion 		= h / 15;
		startCountdown		= 35;
		countdownShown	= false;
		
		fuelMeterBot 	= h - (h / 4);
		fuelMeterTop 	= h / 10;
		fuelRange 		= (fuelMeterBot - fuelMeterTop) / 100;
		
		if(!Util.fuelToggle){
			fuelMeterBg 	= new Rect( w - screenWPortion, 				fuelMeterTop, 						w - screenWPortion + (w / 30), 		fuelMeterBot);
			fuelMeter 		= new Rect((w - screenWPortion + 4), 		fuelMeterTop, 						w - screenWPortion + (w / 30) - 4, 	fuelMeterBot - 4);
		} else {
			fuelMeterBg 	= new Rect( screenWPortion, 				fuelMeterTop, 						screenWPortion + (w / 30), 		fuelMeterBot);
			fuelMeter 		= new Rect((screenWPortion + 4), 		fuelMeterTop, 						screenWPortion + (w / 30) - 4, 	fuelMeterBot - 4);
		}
		
		fuelBgPaint 			= new Paint();
		fuelPaint 				= new Paint();
		textPaint 				= new Paint();
		countdownPaint 	= new Paint();
		
		fuelBgPaint.setARGB(255, 0, 0, 0);
		fuelPaint.setARGB(255, 30, 200, 30);
		textPaint.setTextAlign(Align.RIGHT);
		textPaint.setTextSize(64);
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setShadowLayer((float)0.02, 2, 4, Color.BLACK);
		
		countdownPaint.setTextAlign(Align.CENTER);
		countdownPaint.setTextSize(128);
		countdownPaint.setARGB(255, 255, 255, 55);
		countdownPaint.setShadowLayer((float)0.04, 2, 4, Color.BLACK);
	}
	
	public void draw(Canvas c){
		c.drawRect(fuelMeterBg, fuelBgPaint);
		c.drawRect(fuelMeter, fuelPaint);
		c.drawText(scoreString, screenW - 10, screenHPortion, textPaint);
		
		if(!countdownShown)
			c.drawText(countdownText, screenW/2, screenH/2, countdownPaint);
		
	}
	
	public void updateScore(int score){
		scoreString = "Score: " + score;
	}

	public void update() {		
		if(!countdownShown){
			if(startCountdown == 0){
				countdownShown = true;
			} else {
				startCountdown--;
				
				if(startCountdown >= 25)
					countdownText = "3";
				else if(startCountdown >= 15)
					countdownText = "2";
				else if(startCountdown >= 5)
					countdownText = "1";
				else
					countdownText = "GO!";
			}
		}
		
		int fuelRatio =(int)(((double)Boat.fuel / (double)Boat.maxFuel) * 100);
		fuelMeter.top = (fuelMeterBot - (fuelRatio* fuelRange));
	}
}
