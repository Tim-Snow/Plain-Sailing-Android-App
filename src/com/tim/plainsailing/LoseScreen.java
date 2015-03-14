package com.tim.plainsailing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

public class LoseScreen {	
	public boolean 					isVisible;
	
	private 	int 						replayButtonX, 			replayButtonY,
												menuButtonX, 			menuButtonY,
												gameOverX,				gameOverY,
												scoreX,						scoreY,
												coinX,							coinY;
	public 	Rect					replayButton,		 		menuButton;
	private 	Rect 					background;
	private 	Paint 					backgroundPaint, 	buttonPaint,		smallTextPaint, 		bigTextPaint;
	private 	String 				score,							coins;

	public LoseScreen(int screenW, int screenH ) {
		isVisible 					= false;

		backgroundPaint 	= new Paint();
		buttonPaint 			= new Paint();
		smallTextPaint 		= new Paint();
		bigTextPaint 			= new Paint();
				
		background			= new Rect( 20, 20, screenW - 20, screenH - 20);
		replayButton 			= new Rect(screenW/2 - (screenW/3), 		screenH - (screenH/4), 	screenW/2 - (screenW/10), 		screenH - (screenH/10));
		menuButton 			= new Rect(screenW/2 + (screenW/10), 	screenH - (screenH/4), 	screenW/2 + (screenW/3), 		screenH - (screenH/10));
		
		replayButtonX 		= replayButton.left 	+ ((replayButton.right 			- replayButton.left) 	/ 	2);
		replayButtonY 		= replayButton.top 	+ ((replayButton.bottom 	- replayButton.top) 	/ 	3);
		menuButtonX		=  menuButton.left 	+ ((menuButton.right 			- menuButton.left) 	/ 	2);
		menuButtonY		= menuButton.top 	+ ((menuButton.bottom 	- menuButton.top) 	/ 	3);
		
		gameOverX			=  screenW/2;			gameOverY			= screenH/3;
		scoreX						= screenW/2;				scoreY						= screenH/2;
		coinX						= scoreX;					coinY						= scoreY + (screenH/8);
		
		backgroundPaint.setARGB(155, 30, 70, 104);
		buttonPaint.setARGB(255, 80,180, 240);
		
		smallTextPaint.setARGB(255, 255, 255, 255);
		smallTextPaint.setTextSize(40);
		smallTextPaint.setTextAlign(Align.CENTER);
		smallTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		bigTextPaint.setARGB(255, 255, 255, 255);
		bigTextPaint.setTextSize(128);
		bigTextPaint.setTextAlign(Align.CENTER);
		bigTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
	}
	
	public void setFinalScore(int score, int money, boolean isHighscore){
		if(isHighscore){
			this.score 	= "New Highscore: " + Integer.toString(score) ;
		} else {
			this.score 	= "Score: " + Integer.toString(score) ;
		}
		
		coins				= "Coins Earned: " + Integer.toString(money) ;
	}

	public void draw(Canvas c) {
			c.drawRect(background, 		backgroundPaint);
			
			c.drawRect(replayButton,		buttonPaint);
			c.drawRect(menuButton, 		buttonPaint);
						
			c.drawText( "Replay",				replayButtonX , 	replayButtonY, 		smallTextPaint	);
			c.drawText( "Menu", 				menuButtonX, 		menuButtonY, 		smallTextPaint	);
			c.drawText( "Game over!", 		gameOverX, 			gameOverY, 			bigTextPaint		);
			c.drawText( score, 					scoreX, 					scoreY, 					smallTextPaint	);
			c.drawText( coins, 					coinX, 					coinY, 					smallTextPaint	);

	}
}