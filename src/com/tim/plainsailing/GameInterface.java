package com.tim.plainsailing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class GameInterface {
	Bitmap	boostButton, 	boostButtonPressed;
	
	boolean	boost;
	
	String	scoreString;
	
	int 	screenW, 		screenH, 		screenWPortion, screenHPortion,
			fuelMeterBot, 	fuelMeterTop, 	fuelRange,		startCountdown;
	
	Rect 	fuelMeter, 		fuelMeterBg,	boostRect;
	Paint 	fuelBgPaint, 	fuelPaint,		textPaint;

	public boolean countdownShown;
	
	public GameInterface(int w, int h, Context context){
		boost			= false;
		scoreString 	= null;
		screenH 		= h;
		screenW 		= w;
		screenWPortion 	= w / 20;
		screenHPortion 	= h / 15;
		startCountdown	= 10;
		countdownShown	= false;
		
		boostButton			= BitmapFactory.decodeResource(context.getResources(), R.drawable.boost);
		boostButtonPressed	= BitmapFactory.decodeResource(context.getResources(), R.drawable.boostp);
		
		fuelMeterBot 	= h - (h / 4);
		fuelMeterTop 	= h/10;
		fuelRange 		= (fuelMeterBot - fuelMeterTop) / 100;
		
		fuelMeterBg 	= new Rect( w - screenWPortion, 			fuelMeterTop, 						w - screenWPortion + (w/30), 		fuelMeterBot);
		fuelMeter 		= new Rect((w - screenWPortion + 4), 		fuelMeterTop, 						w - screenWPortion + (w/30) - 4, 	fuelMeterBot - 4);
		boostRect		= new Rect( w - boostButton.getWidth(), 	h - boostButton.getHeight(), 		w, 									h);
		
		fuelBgPaint 	= new Paint();
		fuelPaint 		= new Paint();
		textPaint 		= new Paint();
		
		fuelBgPaint.setARGB(255, 0, 0, 0);
		fuelPaint.setARGB(255, 30, 200, 30);
		textPaint.setTextAlign(Align.RIGHT);
		textPaint.setTextSize(64);
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setShadowLayer((float)0.02, 2, 4, Color.BLACK);
	}
	
	public void draw(Canvas c){
		c.drawRect(fuelMeterBg, fuelBgPaint);
		c.drawRect(fuelMeter, fuelPaint);
		
		if(!boost)
			c.drawBitmap(boostButton, 			null, boostRect, null);
		else
			c.drawBitmap(boostButtonPressed, 	null, boostRect, null);
		
		c.drawText(scoreString, screenW - 10, screenHPortion, textPaint);
		
		if(startCountdown != 0){
			String countdownText;
			countdownText = Integer.toString(startCountdown);
			c.drawText(countdownText, screenW/2, screenH/2, textPaint);
			startCountdown--;
		}
		if(startCountdown == 0){
			countdownShown = true;
		}
	}
	
	public void updateScore(int score){
		scoreString = "Score: " + score;
	}

	public void update(int fuel) {		
		fuelMeter.top = fuelMeterBot - ((fuel / 10) * fuelRange);
	}
}
