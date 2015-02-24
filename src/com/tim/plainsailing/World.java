package com.tim.plainsailing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class World {
	enum OBSTACLE_STYLE { NORMAL, NARROW, ALTERNATE, DOUBLE, MOVING_MIDDLE, FLOATING_BLOCKS }
	
	Boat 			boat;
	
	Boolean			alternate, 			transition,			hasCollided;
	
	Bitmap 			botDetailBitmap1, 	botDetailBitmap2, 	botDetailBitmap3;
	
	int 			screenW, 			screenH,			screenHPortion,
					speed, 				minObstacleHeight, 	obstacleWidth, 
					newBlockTimer, 		obsStyleTimer,		bgWidth, 			
					bgHeight;
	
	List<Rect> 		obstacles,			obstaclesOOB;
	
	Rect 			bg1PosRect1, 		bg1PosRect2, 		bg1PosRect3,
					bg2PosRect1, 		bg2PosRect2, 		bg2PosRect3,
					bg3PosRect1, 		bg3PosRect2, 		bg3PosRect3;
	
	byte[]			randNumberPool;
	byte 			nextRandIndex;

	OBSTACLE_STYLE 	obstaStyle;
	
	Random 	rand = null;
	Paint 	p 	 = null;
 
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
		Bitmap boatSs 	= BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
		boat	 		= new Boat(boatSs);
		obstacles 		= new ArrayList<Rect>();
		obstaclesOOB 	= new ArrayList<Rect>();
		randNumberPool 	= new byte[50];
		rand 			= new Random();	
		p 				= new Paint();
		p.setARGB(255, 130, 130, 50);
		
		//50 Random numbers between 1 and 15, prevents any rand calculations during gameplay
		nextRandIndex 	= 0;

		for(int i = 0; i < 50; i++){
			randNumberPool[i] = (byte)(rand.nextInt(14)+1);
		}

		//Block & pattern properties
		alternate 			= false;
		transition			= false;
		hasCollided			= false;
		obstacleWidth 		= 100;
		obstaStyle 			= OBSTACLE_STYLE.NORMAL;
		minObstacleHeight 	= screenH / 25;
		newBlockTimer 		= obstacleWidth;
		obsStyleTimer		= 500;
		speed 				= 15;
		
		bg1PosRect1 = new Rect( 0, 			 screenH - bgHeight, 	bgWidth, 		screenH);
		bg1PosRect2 = new Rect( bgWidth, 	 screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bg1PosRect3 = new Rect( bgWidth * 2, screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		bg2PosRect1 = new Rect( 0, 			 screenH - bgHeight, 	bgWidth, 		screenH);
		bg2PosRect2 = new Rect( bgWidth, 	 screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bg2PosRect3 = new Rect( bgWidth * 2, screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		bg3PosRect1 = new Rect( 0, 			 screenH - bgHeight, 	bgWidth, 		screenH);
		bg3PosRect2 = new Rect( bgWidth, 	 screenH - bgHeight, 	bgWidth * 2, 	screenH);
		bg3PosRect3 = new Rect( bgWidth * 2, screenH - bgHeight, 	bgWidth * 3, 	screenH);
		
		createTopBlocks(obstaStyle);
		createBottomBlocks(obstaStyle);
	}
	
	public void update(){
		if(!hasCollided){
			timerTick();
			moveBackgrounds();
			boat.update();
			updateBlocks(obstaStyle, obstacles, obstaclesOOB, speed);	
			
			for(int i = 0; i < obstacles.size(); i++){
				if(Util.collidesWith(boat.screenPosition, obstacles.get(i))){
					//do pixel perfect collision
					hasCollided = true;
				}
			}
		}
	}
		
	public void draw(Canvas c){
		c.drawBitmap(botDetailBitmap1, null, bg1PosRect1, null);
		c.drawBitmap(botDetailBitmap1, null, bg1PosRect2, null);
		c.drawBitmap(botDetailBitmap1, null, bg1PosRect3, null);
		
		c.drawBitmap(botDetailBitmap2, null, bg2PosRect1, null);
		c.drawBitmap(botDetailBitmap2, null, bg2PosRect2, null);
		c.drawBitmap(botDetailBitmap2, null, bg2PosRect3, null);
		
		c.drawBitmap(botDetailBitmap3, null, bg3PosRect1, null);
		c.drawBitmap(botDetailBitmap3, null, bg3PosRect2, null);
		c.drawBitmap(botDetailBitmap3, null, bg3PosRect3, null);
		
		for(Rect r : obstacles){
			c.drawRect(r, p);
		}
		
		boat.draw(c);
		
		if(hasCollided){
			Rect r = new Rect( 20, 20, screenW - 20, screenH - 20);
			Paint p  = new Paint();
			p.setARGB(75, 230, 230, 230);
			c.drawRect(r, p);
			p.setARGB(255, 0, 0, 0);
			//c.drawText("Game over!", screenW/2, screenH/2, p);
		}
	}
	
	private void timerTick(){
		newBlockTimer -= speed;
		obsStyleTimer--;
		
		if(obsStyleTimer < 1){
			if(transition){
				newBlockTimer	= 600;
				obstaStyle 		= OBSTACLE_STYLE.ALTERNATE;
				obsStyleTimer 	= 800;
				transition 		= false;
			} else {
				obstaStyle 		= OBSTACLE_STYLE.NARROW;
				obsStyleTimer 	= 200;
				transition		= true;
			}
		}
		
		if(newBlockTimer < 1){
			createTopBlocks(obstaStyle);
		}
	}
	
	private void createTopBlocks(OBSTACLE_STYLE os){
		Rect r = null;
		switch(os){
		case NORMAL:
			int a = (getNextRand(randNumberPool) + 2) * minObstacleHeight;
			r = new Rect(screenW, a, screenW + obstacleWidth, a + (1 + getNextRand(randNumberPool)) * minObstacleHeight );
			obstacles.add(r);
			newBlockTimer = 650 + (getNextRand(randNumberPool) * 10);
			break;
		case NARROW:
			r = new Rect(screenW, 0, screenW + obstacleWidth, 6 * minObstacleHeight );
			obstacles.add(r);
			newBlockTimer = obstacleWidth;
			createBottomBlocks(os);
			break;
		case ALTERNATE:
			if(alternate){
				r = new Rect(screenW, 0, screenW + obstacleWidth, (1 + getNextRand(randNumberPool)) * minObstacleHeight );
				obstacles.add(r);
				newBlockTimer = 650 + (getNextRand(randNumberPool) * 10);
				alternate = false;
			} else {
				createBottomBlocks(os);
			}
			break;
		case DOUBLE:
			break;
		case MOVING_MIDDLE:
			break;
		case FLOATING_BLOCKS:
			break;
		default:
			break;
		}
	}
	
	private void createBottomBlocks(OBSTACLE_STYLE os){
		Rect r = null;
		switch(os){
		case NARROW:
			r = new Rect(screenW, screenH - (6 * minObstacleHeight), screenW + obstacleWidth, screenH);
			obstacles.add(r);
			break;
		case ALTERNATE:
			r = new Rect(screenW, screenH - (getNextRand(randNumberPool) * minObstacleHeight), screenW + obstacleWidth, screenH);
			obstacles.add(r);
			newBlockTimer = 650 + (getNextRand(randNumberPool) * 10);
			alternate = true;
			break;
		case DOUBLE:
			break;
		case MOVING_MIDDLE:
			break;
		case FLOATING_BLOCKS:
			break;
		default:
			break;
		}
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
	
	private byte getNextRand(byte[] array){
		byte r = 0;
		if(nextRandIndex < 49){
			r = array[nextRandIndex];
			nextRandIndex++;
		} else {
			r = array[nextRandIndex];
			nextRandIndex = 0;
		}
		return r; 
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
	
	private static void updateBlocks(OBSTACLE_STYLE os, List<Rect> blocks, List<Rect> remBlocks, int speed){
		//moves all obstacles as well as cleanup
		for(Rect r : blocks){
			r.left  -= speed;
			r.right -= speed;
			if(r.right < 0)	remBlocks.add(r);
		}
		blocks.removeAll(remBlocks);
	}
}