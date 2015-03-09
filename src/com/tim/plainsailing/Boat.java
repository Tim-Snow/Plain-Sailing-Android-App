package com.tim.plainsailing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Boat {
	static enum BOAT_STATE { FALLING, RISING, BOOST };
	public BOAT_STATE 	state;
	
	public  Rect 		collisionRect,			boatSsPosition;
	public  Bitmap 		boatSs;
	public	int			fuel;
		
	private int 		boatX, 		boatY,		collisionH, 
						width, 		dy, 		wingAnimStep;
	
	public int 			height;
		
	private Rect 		screenPosition,
						wing1TopSsPosition,	 	wing1BotSsPosition,
						wing2TopSsPosition,	 	wing2BotSsPosition,
						wing3TopSsPosition,	 	wing3BotSsPosition,
						wing4TopSsPosition,	 	wing4BotSsPosition;

	public float boostDistY;
		
	public Boat(Bitmap b){
		boatSs  		= b;
		state 			= BOAT_STATE.FALLING;
		fuel 			= 750;
		dy 				= 0;
		boostDistY 		= 0.0f;
		boatX 			= 50; 	boatY 	= 5;
		width 			= 256;	height 	= 256;
		wingAnimStep 	= 0;
		collisionH 		= ((height)/5)*3; 

		boatSsPosition 		= new Rect(0, 		128, 	128, 	256);
		wing1TopSsPosition 	= new Rect(128, 	0, 		256, 	128);
		wing1BotSsPosition 	= new Rect(128, 	128, 	256, 	256);
		wing2TopSsPosition 	= new Rect(256, 	0, 		384, 	128);
		wing2BotSsPosition 	= new Rect(256, 	128, 	384, 	256);
		wing3TopSsPosition 	= new Rect(384, 	0, 		512, 	128);
		wing3BotSsPosition 	= new Rect(384, 	128, 	512, 	256);
		wing4TopSsPosition 	= new Rect(512, 	0, 		640, 	128);
		wing4BotSsPosition 	= new Rect(512, 	128, 	640, 	256);
				
		screenPosition 	= new Rect(boatX, 	boatY, boatX+width, boatY+height);
		collisionRect 	= new Rect(boatX, 	boatY, boatX+width, boatY+collisionH);
	}
	
	public void update(){
		if(state == BOAT_STATE.FALLING){
			if(dy < 5) dy = 5;  
			else 	 	dy++; 	
		} 
		if(state == BOAT_STATE.RISING){
			fuel -= 4;
			dy 	  = -9;
		}
		
		if(fuel < 0){
			fuel = 0;
			state = BOAT_STATE.FALLING;
		}

		screenPosition.top 		= screenPosition.top + dy;
		screenPosition.bottom 	= screenPosition.top + height;
		
		collisionRect.top 		= screenPosition.top + dy;
		collisionRect.bottom 	= collisionRect.top + collisionH;
	}
		
	public void draw(Canvas c){
		switch(state){
		case FALLING:
			c.drawBitmap(boatSs, wing1TopSsPosition, 	screenPosition, null);		
			c.drawBitmap(boatSs, boatSsPosition, 		screenPosition, null);
			c.drawBitmap(boatSs, wing1BotSsPosition, 	screenPosition, null);
			break;
		case BOOST:
			c.drawBitmap(boatSs, wing1TopSsPosition, 	screenPosition, null);		
			c.drawBitmap(boatSs, boatSsPosition, 		screenPosition, null);
			c.drawBitmap(boatSs, wing1BotSsPosition, 	screenPosition, null);	
			break;
		case RISING:
			switch (wingAnimStep) {
			case 0:
				c.drawBitmap(boatSs, wing2TopSsPosition, 	screenPosition, null);		
				c.drawBitmap(boatSs, boatSsPosition, 		screenPosition, null);
				c.drawBitmap(boatSs, wing2BotSsPosition, 	screenPosition, null);
				wingAnimStep++;
				break;
			case 1:
				c.drawBitmap(boatSs, wing3TopSsPosition, 	screenPosition, null);		
				c.drawBitmap(boatSs, boatSsPosition, 		screenPosition, null);
				c.drawBitmap(boatSs, wing3BotSsPosition, 	screenPosition, null);
				wingAnimStep++;
				break;
			case 2:
				c.drawBitmap(boatSs, wing4TopSsPosition, 	screenPosition, null);		
				c.drawBitmap(boatSs, boatSsPosition, 		screenPosition, null);
				c.drawBitmap(boatSs, wing4BotSsPosition, 	screenPosition, null);
				wingAnimStep = 0;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	public void boostUpdate() {
		if(boostDistY > 20.0){
			boostDistY -= 20;
			dy = 20;
		} else if(boostDistY < -20.0){
			boostDistY += 20;
			dy = -20;
		} else {
			boostDistY = 0;
			dy = 0;
		}
		
		screenPosition.top 		= screenPosition.top + dy;
		screenPosition.bottom 	= screenPosition.top + height;
	}
}
