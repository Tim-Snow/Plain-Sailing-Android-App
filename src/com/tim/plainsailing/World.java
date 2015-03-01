package com.tim.plainsailing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

public class World {
	Boat 		boat;
	GameObjects	objects;
	
	Boolean		hasCollided;
	
	Bitmap 		botDetailBitmap1, 	botDetailBitmap2, 	botDetailBitmap3;
	
	int 		screenW, 			screenH,			screenHPortion,
				bgWidth, 			bgHeight,			
				currentScore;
		
	Rect 		bg1PosRect1, 		bg1PosRect2, 		bg1PosRect3,
				bg2PosRect1, 		bg2PosRect2, 		bg2PosRect3,
				bg3PosRect1, 		bg3PosRect2, 		bg3PosRect3;

 
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
		objects = new GameObjects();
		objects.init(context);
		Bitmap boatSs 	= BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
		boat	 		= new Boat(boatSs);		
		hasCollided		= false;
		currentScore 	= 0;
		
		bg1PosRect1 = new Rect( 0, 			 screenH - bgHeight, 	bgWidth, 		screenH);
		bg1PosRect2 = new Rect( bgWidth, 	 screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bg1PosRect3 = new Rect( bgWidth * 2, screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		bg2PosRect1 = new Rect( 0, 			 screenH - bgHeight, 	bgWidth, 		screenH);
		bg2PosRect2 = new Rect( bgWidth, 	 screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bg2PosRect3 = new Rect( bgWidth * 2, screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		bg3PosRect1 = new Rect( 0, 			 screenH - bgHeight, 	bgWidth, 		screenH);
		bg3PosRect2 = new Rect( bgWidth, 	 screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bg3PosRect3 = new Rect( bgWidth * 2, screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		
	}
	
	public void update(){
		if(!hasCollided){
			moveBackgrounds();
			objects.update();
			boat.update();
			
			for(Rect r : objects.obstacles){
				if(Util.collidesWith(boat.screenPosition, r))
					hasCollided = true;
			}
			
			currentScore++;
		}
	}
		
	public void draw(Canvas c){		
		drawBackgrounds(c);
		objects.draw(c);
		boat.draw(c);
		
		if(hasCollided)	
			drawLoseScreen(c);
	}
	
	private void drawBackgrounds(Canvas c){
		c.drawBitmap(botDetailBitmap1, null, bg1PosRect1, null);
		c.drawBitmap(botDetailBitmap1, null, bg1PosRect2, null);
		c.drawBitmap(botDetailBitmap1, null, bg1PosRect3, null);
		
		c.drawBitmap(botDetailBitmap2, null, bg2PosRect1, null);
		c.drawBitmap(botDetailBitmap2, null, bg2PosRect2, null);
		c.drawBitmap(botDetailBitmap2, null, bg2PosRect3, null);
		
		c.drawBitmap(botDetailBitmap3, null, bg3PosRect1, null);
		c.drawBitmap(botDetailBitmap3, null, bg3PosRect2, null);
		c.drawBitmap(botDetailBitmap3, null, bg3PosRect3, null);
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
		scrollBg(bg1PosRect1, 2, 0);
		scrollBg(bg2PosRect1, 4, 0);
		scrollBg(bg3PosRect1, 8, 0);
		
		scrollBg(bg1PosRect2, 2, bgWidth);
		scrollBg(bg2PosRect2, 4, bgWidth);
		scrollBg(bg3PosRect2, 8, bgWidth);

		scrollBg(bg1PosRect3, 2, bgWidth * 2);
		scrollBg(bg2PosRect3, 4, bgWidth * 2);
		scrollBg(bg3PosRect3, 8, bgWidth * 2);
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