package com.tim.plainsailing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class World {
	enum OBSTACLE_STYLE { NORMAL, NARROW, OPPOSITES, RANDOM, ALTERNATE, MOVING_MIDDLE, FLOATING_BLOCKS }
	
	Boat boat;
	
	int screenW, screenH,
		minObstacleHeight, 	obstacleWidth, 
		newBlockTimer, 		obsStyleTimer;

	List<Rect> 		obstacles;
	List<Rect> 		obstaclesOOB;
	int[]			randNumberPool;
	int 			nextRandIndex;
	OBSTACLE_STYLE 	obstaStyle;
	
	Random 	rand = null;
	Paint 	p 	 = null;
 
	public World(Context context){
		Bitmap boatSs = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
		
		randNumberPool 	= new int[50];
		nextRandIndex 	= 0;
		rand 			= new Random();	
		boat	 		= new Boat(boatSs);
		obstacles 		= new ArrayList<Rect>();
		obstaclesOOB 	= new ArrayList<Rect>();
		p 				= new Paint();
		
		for(int i = 0; i < 50; i++){
			randNumberPool[i] = rand.nextInt(300);
		}

		obstaStyle 			= OBSTACLE_STYLE.NORMAL;
		minObstacleHeight 	= 50;
		obstacleWidth 		= 100;
		newBlockTimer 		= obstacleWidth;
		obsStyleTimer		= 200;
		
		p.setColor(color.black);
		p.setAlpha(255);
		
		screenW = context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		screenH = context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
			
		createTopBlocks(obstaStyle);
		createBottomBlocks(obstaStyle);
	}
	
	private int getNextRand(){
		int rtn = 0;
		if(nextRandIndex < 50){
			rtn = randNumberPool[nextRandIndex];
			nextRandIndex++;
		} else {
			nextRandIndex = 0;
			rtn = randNumberPool[nextRandIndex];
		}
		return rtn; 
	}
	
	private void createTopBlocks(OBSTACLE_STYLE os){
		Rect r = null;
		switch(os){
		case NORMAL:
			int a = getNextRand() * 2;
			r = new Rect(screenW, a, screenW+obstacleWidth, a+getNextRand()+200);
			obstacles.add(r);
			newBlockTimer = 800+getNextRand();
			break;
		case NARROW:
			break;
		case OPPOSITES:
			break;
		case RANDOM:
			r = new Rect(screenW+obstacleWidth, 0, (screenW + (obstacleWidth*2)), (0 + minObstacleHeight + getNextRand()));
			obstacles.add(r);
			createBottomBlocks(obstaStyle);
			break;
		case ALTERNATE:
			break;
		case MOVING_MIDDLE:
			break;
		case FLOATING_BLOCKS:
			break;
		default:
			break;
		}
	}
	
	private void createBottomBlocks(OBSTACLE_STYLE os) {
		switch(os){
		case NORMAL:		
			break;
		case NARROW:
			break;
		case OPPOSITES:
			break;
		case RANDOM:
			Rect r = new Rect(screenW+obstacleWidth, obstacles.get(obstacles.size()-1).bottom + 500 + getNextRand() , (screenW + (obstacleWidth*2)), screenH);
			obstacles.add(r);
			break;
		case ALTERNATE:
			break;
		case MOVING_MIDDLE:
			break;
		case FLOATING_BLOCKS:
			break;
		default:
			break;
		}
	}

	public void update(){
		newBlockTimer -= 10;
		obsStyleTimer--;
		if(newBlockTimer < 1){
			newBlockTimer = obstacleWidth;
			createTopBlocks(obstaStyle);
		}
		if(obsStyleTimer < 1){
			obstaStyle = OBSTACLE_STYLE.RANDOM;
			obsStyleTimer = 200;
		}
		
		
		for(Rect r : obstacles){
			r.left -= 10;
			r.right -= 10;
			if(r.right < 0){
				obstaclesOOB.add(r);
			}
		}
		obstacles.removeAll(obstaclesOOB);
		boat.update();
	}
	
	public void draw(Canvas c){
		for(Rect r : obstacles){
			c.drawRect(r, p);
		}
		boat.draw(c);
	}
}
