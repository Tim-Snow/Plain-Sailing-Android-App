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
	
	int 			minObstacleHeight, 	obstacleWidth,
					screenW, 			screenH,		newBlockTimer, 	fuelTimer,	
					obsStyleTimer, 		fuelH,			fuelW,			screenPortion;

	public float 	boostDistX, 		speed, 			boostSpeed;
	
	List<Rect> 		obstacles,			obstaclesOOB,
					fuelList,			fuelOOB;

	byte[]			randNumberPool;
	byte 			nextRandIndex;
	
	Random 	rand = null;
	Paint 	p 	 = null;
	
	public void init(Context context){
		screenW = context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		screenH = context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		
		fuel 			= BitmapFactory.decodeResource(context.getResources(), R.drawable.fuel);
		fuelH			= fuel.getHeight();
		fuelW			= fuel.getWidth();
		obstacles 		= new ArrayList<Rect>();
		obstaclesOOB 	= new ArrayList<Rect>();
		fuelList 		= new ArrayList<Rect>();
		fuelOOB	 		= new ArrayList<Rect>();
		randNumberPool 	= new byte[50];
		rand 			= new Random();	
		p 				= new Paint();
		boostDistX 		= 0.0f;
		p.setARGB(255, 130, 130, 50);
		
		//50 Random numbers between 1 and 15, prevents any rand calculations during gameplay
		nextRandIndex 	= 0;
		screenPortion	= screenH / 15;

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
		fuelTimer			= 100;
		speed 				= 10;
		boostSpeed 			= 20;
		
		createTopBlocks(obstaStyle);
		createBottomBlocks(obstaStyle);
	}

	public void update(){
		if(boostDistX > 0.0){
			updateBlocks(obstacles, obstaclesOOB, boostSpeed);	
			updateBlocks(fuelList,  fuelOOB, 	  boostSpeed);	
			boostDistX -= boostSpeed;
		} else {
			updateBlocks(obstacles, obstaclesOOB, speed);	
			updateBlocks(fuelList,  fuelOOB, 	  speed);	
			if(boostDistX < 0.0){
				boostDistX = 0.0f;
			}
		}
		timerTick();
	}
	
	public void draw(Canvas c){
		for(Rect r : fuelList){ c.drawBitmap(fuel, null, r, null); }
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
		fuelTimer--;
		
		if(obsStyleTimer < 1){
			if(transition){
				newBlockTimer	= 600;
				obstaStyle 		= OBSTACLE_STYLE.ALTERNATE;
				obsStyleTimer 	= 800;
				transition 		= false;
			} else {
				newBlockTimer 	= 0;
				obstaStyle 		= OBSTACLE_STYLE.NARROW;
				obsStyleTimer 	= 100;
				transition		= true;
			}
		}
		
		if(fuelTimer < 1){
			createFuelObject();
			fuelTimer = 150;
		}
		
		if(newBlockTimer < 1){
			createTopBlocks(obstaStyle);
		}
	}
	
	private void createFuelObject() {
		int a = screenH - (getNextRand(randNumberPool) * screenPortion);
		Rect r = new Rect(screenW, a, screenW + fuelW, a + fuelH);
		fuelList.add(r);
	}

	private static void updateBlocks(List<Rect> blocks, List<Rect> remBlocks, double speed){
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

	public void boost(Boat boat, float x, float y) {
		boostDistX 		= x -  boat.collisionRect.left;
		boat.boostDistY = y - (boat.collisionRect.bottom - (boat.height / 2));
	}
}
