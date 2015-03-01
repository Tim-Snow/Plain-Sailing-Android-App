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

public class GameObjects {
	enum OBSTACLE_STYLE { NORMAL, NARROW, ALTERNATE, DOUBLE, MOVING_MIDDLE, FLOATING_BLOCKS }
	
	OBSTACLE_STYLE 	obstaStyle;
	
	Boolean			alternate, 			transition;
	
	Bitmap 			fuel;
	
	int 			minObstacleHeight, 	obstacleWidth, 		speed,	
					screenW, 			screenH,			newBlockTimer, 		
					obsStyleTimer;
	
	List<Rect> 		obstacles,			obstaclesOOB;
	
	byte[]			randNumberPool;
	byte 			nextRandIndex;
	
	Random 	rand = null;
	Paint 	p 	 = null;
	
	public void init(Context context){
		screenW = context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		screenH = context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		
		fuel 			= BitmapFactory.decodeResource(context.getResources(), R.drawable.fuel);
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
		obstacleWidth 		= 100;
		obstaStyle 			= OBSTACLE_STYLE.NORMAL;
		minObstacleHeight 	= screenH / 25;
		newBlockTimer 		= obstacleWidth;
		obsStyleTimer		= 500;
		speed 				= 15;
		
		createTopBlocks(obstaStyle);
		createBottomBlocks(obstaStyle);
	}

	public void update(){
		updateBlocks(obstaStyle, obstacles, obstaclesOOB, speed);	
		timerTick();
	}
	
	public void draw(Canvas c){
		for(Rect r : obstacles){ c.drawRect(r, p); }
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
	
	private static void updateBlocks(OBSTACLE_STYLE os, List<Rect> blocks, List<Rect> remBlocks, int speed){
		//moves all obstacles as well as cleanup
		for(Rect r : blocks){
			r.left  -= speed;
			r.right -= speed;
			if(r.right < 0)	remBlocks.add(r);
		}
		blocks.removeAll(remBlocks);
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
}
