package com.tim.plainsailing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Boat {
	enum BOAT_STATE { FALLING, RISING, BOOST };
	
	private BOAT_STATE state;
	private int			riseTime 	= 7;
	private int 		timeRising 	= 0;
	
	private int 	boatX, boatY, width, height, dy, wingAnimStep;
	
	private Rect 	boatSsPosition,			screenPosition,
					wing1TopSsPosition,	 	wing1BotSsPosition,
					wing2TopSsPosition,	 	wing2BotSsPosition,
					wing3TopSsPosition,	 	wing3BotSsPosition,
					wing4TopSsPosition,	 	wing4BotSsPosition;
	
	private Bitmap 	boatSs;
	
	public void setRising()	  { state = BOAT_STATE.RISING; }
	
	public Boat(Bitmap b){
		state 	= BOAT_STATE.FALLING;
		boatSs  = b;
		dy 		= 0;
		boatX 	= 50; 	boatY 	= 5;
		width 	= 		height 	= 256;
		wingAnimStep = 0;
		
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
	}
	
	public void update(){
		//add timestep calc and floats
		if(state == BOAT_STATE.FALLING){
			if(dy < 12) { dy = 12; } 
			else 	 	{ dy++;	   }
		} else {
			if(timeRising < riseTime){
				dy = -20;
				timeRising++;
			} else {
				timeRising = 0;
				state = BOAT_STATE.FALLING;
			}
		}
		
		screenPosition.top 		= screenPosition.top + dy;
		screenPosition.bottom 	= screenPosition.top + height;
	}
	
	public void draw(Canvas c){
		switch(state){
		case FALLING:
			c.drawBitmap(boatSs, wing1TopSsPosition, screenPosition, null);		
			c.drawBitmap(boatSs, boatSsPosition, 	screenPosition, null);
			c.drawBitmap(boatSs, wing1BotSsPosition, screenPosition, null);
			break;
		case RISING:
			switch (wingAnimStep) {
			case 0:
				c.drawBitmap(boatSs, wing2TopSsPosition, screenPosition, null);		
				c.drawBitmap(boatSs, boatSsPosition, 	screenPosition, null);
				c.drawBitmap(boatSs, wing2BotSsPosition, screenPosition, null);
				wingAnimStep++;
				break;
			case 1:
				c.drawBitmap(boatSs, wing3TopSsPosition, screenPosition, null);		
				c.drawBitmap(boatSs, boatSsPosition, 	screenPosition, null);
				c.drawBitmap(boatSs, wing3BotSsPosition, screenPosition, null);
				wingAnimStep++;
				break;
			case 2:
				c.drawBitmap(boatSs, wing4TopSsPosition, screenPosition, null);		
				c.drawBitmap(boatSs, boatSsPosition, 	screenPosition, null);
				c.drawBitmap(boatSs, wing4BotSsPosition, screenPosition, null);
				wingAnimStep = 0;
				break;
			default:
				break;
			}
			break;
		case BOOST:
			break;
		default:
			break;
		}
	}
}
