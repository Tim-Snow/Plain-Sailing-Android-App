package com.tim.plainsailing;

import com.tim.plainsailing.Boat.BOAT_STATE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

public class World {
	Boat 			boat;
	GameObjects		objects;
	GameInterface 	gameUi;
	
	Boolean			hasCollided;
	
	Bitmap 			botDetailBitmap1, 	botDetailBitmap2, 	botDetailBitmap3;
	
	int 			screenW, 			screenH,			screenHPortion,
					bgWidth, 			bgHeight,			currentScore;
		
	Rect[] 			bgPosRects;	
 
	public World(Context context){
		//Screen info
		screenW = context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		screenH = context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		screenHPortion = screenH / 10;
		
		//Scrolling background
		botDetailBitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.wbg1);
		botDetailBitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.wbg2);
		botDetailBitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.wbg3);
		bgWidth 		 = botDetailBitmap1.getWidth();
		bgHeight 		 = botDetailBitmap1.getHeight();
		
		//Objects
		gameUi  = new GameInterface(screenW, screenH, context);
		objects = new GameObjects();
		objects.init(context);
		Bitmap boatSs 	= BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
		boat	 		= new Boat(boatSs);		
		hasCollided		= false;
		currentScore 	= 0;
		
		bgPosRects		= new Rect[9];
		bgPosRects[0] 	= new Rect( 0, 				screenH - bgHeight, 	bgWidth, 		screenH);
		bgPosRects[1] 	= new Rect( bgWidth, 	 	screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bgPosRects[2] 	= new Rect( bgWidth * 2, 	screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		bgPosRects[3] 	= new Rect( 0, 				screenH - bgHeight, 	bgWidth, 		screenH);
		bgPosRects[4] 	= new Rect( bgWidth, 	 	screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bgPosRects[5] 	= new Rect( bgWidth * 2, 	screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		bgPosRects[6] 	= new Rect( 0, 				screenH - bgHeight, 	bgWidth, 		screenH);
		bgPosRects[7] 	= new Rect( bgWidth, 	 	screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bgPosRects[8] 	= new Rect( bgWidth * 2, 	screenH - bgHeight, 	bgWidth * 3, 	screenH);
	}
	
	public void update(){
		gameUi.updateScore(currentScore);
		if(gameUi.countdownShown){
			if(!hasCollided){
				currentScore++;
				gameUi.updateScore(currentScore);
				moveBackgrounds();
				objects.update();
				
				for(Rect r : objects.fuelList){
					if(Util.collidesWith(boat.collisionRect, r)){
						boat.fuel += 200;
						objects.fuelOOB.add(r);
					}
				}
				
				objects.fuelList.removeAll(objects.fuelOOB);
				
				if(boat.state != BOAT_STATE.BOOST){
					boat.update();
					
					for(Rect r : objects.obstacles){
						if(Util.collidesWith(boat.collisionRect, r) || boat.collisionRect.bottom > screenH)
							hasCollided = true;
					}
				} else {
					if(objects.boostDistX > 0.0){
						boat.boostUpdate();
					} else {
						boat.state = BOAT_STATE.FALLING;
					}
				}
				gameUi.update(boat.fuel);
			}
		}
	}
		
	public void draw(Canvas c){		
		drawBackgrounds(c);
		objects.draw(c);
		boat.draw(c);
		gameUi.draw(c);
		
		if(hasCollided)	
			drawLoseScreen(c);
	}
	
	public void boost(float x, float y){
		objects.boost(boat, x, y);
	}
	
	private void drawBackgrounds(Canvas c){
		for(int i = 0; i < 9; i++){
			if(i < 3)
				c.drawBitmap(botDetailBitmap1, null, bgPosRects[i], null);
			else if(i < 6)
				c.drawBitmap(botDetailBitmap2, null, bgPosRects[i], null);
			else
				c.drawBitmap(botDetailBitmap3, null, bgPosRects[i], null);
		}
	}
	
	private void drawLoseScreen(Canvas c){
		Rect r = new Rect( 20, 20, screenW - 20, screenH - 20);
		Paint p  = new Paint();
		
		p.setARGB(155, 0, 0, 0);
		c.drawRect(r, p);
		
		p.setARGB(255, 255, 255, 255);
		p.setTextSize(128);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		c.drawText("Game over!", screenW/2, screenH/3, p);
		
		p.setTextSize(64);
		String scoreString = "Score: " + Integer.toString(currentScore);
		c.drawText(scoreString, screenW/2, screenH/2, p);
	}
		
	private void moveBackgrounds(){
		//Scrolling background layers at different speeds, repeating
		scrollBg(bgPosRects[0], 2, 0);
		scrollBg(bgPosRects[3], 4, 0);
		scrollBg(bgPosRects[6], 8, 0);
		
		scrollBg(bgPosRects[1], 2, bgWidth);
		scrollBg(bgPosRects[4], 4, bgWidth);
		scrollBg(bgPosRects[7], 8, bgWidth);

		scrollBg(bgPosRects[2], 2, bgWidth * 2);
		scrollBg(bgPosRects[5], 4, bgWidth * 2);
		scrollBg(bgPosRects[8], 8, bgWidth * 2);
	}
		
	private static void scrollBg(Rect r, int speed, int startPos){
		if(r.right > startPos){
			r.left 	-= speed;
			r.right -= speed;
		} else {
			r.right	= startPos + r.width();
			r.left 	= startPos;
		}		
	}
}