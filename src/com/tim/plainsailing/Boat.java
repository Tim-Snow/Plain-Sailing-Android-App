package com.tim.plainsailing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Boat {
	static enum BOAT_STATE { FALLING, RISING, BOOST };
	
	public 	BOAT_STATE 		state;
	public  	Bitmap 				boat, wingSs, boatBoost;
	static 		int						fuel, 						height, 		maxFuel;
	public  	Rect 					collisionRect;
	
		
	private	 int 						boatX, 			boatY,			collisionH, 
												width, 			dy, 				wingAnimStep;
		
	private 	Rect 					screenPosition,					wingScreenPosition,
												wing1TopSsPosition,	 	wing1BotSsPosition,
												wing2TopSsPosition,	 	wing2BotSsPosition,
												wing3TopSsPosition,	 	wing3BotSsPosition,
												boostTopSsPosition,	 	boostBotSsPosition;
								
	public Boat(Context context){
		boat  					=  BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
		wingSs  				=  BitmapFactory.decodeResource(context.getResources(), R.drawable.wings);
		state 					= BOAT_STATE.FALLING;
		fuel 						= 750 * Util.getUpgradeLevel(0);
		maxFuel				= 1000 * Util.getUpgradeLevel(0);
		dy 						= 0;
		boatX 					= 250; 		boatY 				= 5;
		width 					= boat.getWidth();		
		height 				= boat.getHeight();
		wingAnimStep 	= 0;
		collisionH 			= ((height)/5)*3; 
		
		int wingW = wingSs.getWidth()/4;
		int wingH = wingSs.getHeight()/2;

		wing1TopSsPosition 	= new Rect(0, 					0, 				wingW, 		wingH);
		wing1BotSsPosition 		= new Rect(0, 					wingH, 		wingW, 		wingH*2);
		wing2TopSsPosition 	= new Rect(wingW, 		0, 				wingW*2, 	wingH);
		wing2BotSsPosition 		= new Rect(wingW, 		wingH, 		wingW*2, 	wingH*2);
		wing3TopSsPosition 	= new Rect(wingW*2, 		0, 				wingW*3, 	wingH);
		wing3BotSsPosition 		= new Rect(wingW*2, 		wingH, 		wingW*3, 	wingH*2);
		boostTopSsPosition 	= new Rect(wingW*3, 		0, 				wingW*4, 	wingH);
		boostBotSsPosition 		= new Rect(wingW*3, 		wingH, 		wingW*4, 	wingH*2);
			
		screenPosition 				= new Rect(boatX, 	boatY, boatX+width, boatY+height);
		wingScreenPosition		= new Rect(screenPosition.left, screenPosition.top +(height/5), screenPosition.right, screenPosition.top +(height/5) + height);
		collisionRect 					= new Rect(boatX, 	boatY, boatX+width, boatY+collisionH);
	}
	
	public void update(){
		if(state == BOAT_STATE.FALLING){
			addFuel(1 * (Util.getUpgradeLevel(2) - 1));
					
			if(dy < 2) dy = 2;  
			else 	 	dy++; 	
		} 
		if(state == BOAT_STATE.RISING){
			fuel -= 4;
			dy 	  = -10;
		}
		
		if(state == BOAT_STATE.BOOST){
			dy = 0;
		}
		
		if(fuel < 0){
			fuel = 0;
			state = BOAT_STATE.FALLING;
		}

		screenPosition.top 					= screenPosition.top + dy;
		screenPosition.bottom 			= screenPosition.top + height;
		wingScreenPosition.top 			= wingScreenPosition.top + dy;
		wingScreenPosition.bottom 	= wingScreenPosition.top+height;
	//	wingScreenPosition.offset(dx, dy);
	//	wingScreenPosition.contains(r)
		collisionRect.top 						= screenPosition.top + dy;
		collisionRect.bottom 				= collisionRect.top + collisionH;
	}
		
	private void addFuel(int fuelAmount) {
		if(fuel + fuelAmount >= maxFuel){
			fuel = maxFuel;
		} else {
			fuel += fuelAmount;
		}
	}

	public void draw(Canvas c){
		Paint black = new Paint();
		black.setARGB(255, 0, 0, 0);
		c.drawRect(collisionRect, black);
		
		switch(state){
		case FALLING:
			c.drawBitmap(wingSs, wing1TopSsPosition, 	wingScreenPosition, null);		
			c.drawBitmap(boat, 		null, 								screenPosition, null);
			c.drawBitmap(wingSs, wing1BotSsPosition, 	wingScreenPosition, null);
			break;
		case BOOST:
			c.drawBitmap(wingSs, boostTopSsPosition, 	wingScreenPosition, null);		
			c.drawBitmap(boat, 		null, 								screenPosition, null);
			c.drawBitmap(wingSs, boostBotSsPosition, 	wingScreenPosition, null);
			break;
		case RISING:
			switch (wingAnimStep) {
			case 0:
				c.drawBitmap(wingSs, wing1TopSsPosition, 	wingScreenPosition, null);		
				c.drawBitmap(boat, 		null, 								screenPosition, null);
				c.drawBitmap(wingSs, wing1BotSsPosition, 	wingScreenPosition, null);
				wingAnimStep++;
				break;
			case 1:
				c.drawBitmap(wingSs, wing2TopSsPosition, 	wingScreenPosition, null);		
				c.drawBitmap(boat, 		null, 								screenPosition, null);
				c.drawBitmap(wingSs, wing2BotSsPosition, 	wingScreenPosition, null);
				wingAnimStep++;
				break;
			case 2:
				c.drawBitmap(wingSs, wing3TopSsPosition, 	wingScreenPosition, null);		
				c.drawBitmap(boat, 		null, 								screenPosition, null);
				c.drawBitmap(wingSs, wing3BotSsPosition, 	wingScreenPosition, null);
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

	public void pickUp() {
		addFuel(200 * Util.getUpgradeLevel(0));
		
		Util.playSound(Util.pickUpSound);
	}
}
