package com.tim.plainsailing;

import com.tim.plainsailing.Boat.BOAT_STATE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class World {
	public 			Boat 						boat;
	public 			GameInterface 	gameUi;
	public 			LoseScreen			loseScreen;
	public 			Boolean 				hasCollided,				newHighscore;
	
	private			Context					context;
	private			GameObjects		objects;
	private			Boolean					hasUpdated;	
	private			Bitmap 					botDetailBitmap1, 	botDetailBitmap2, 	botDetailBitmap3;
	private			int							screenW, 					screenH,						currentScore,				
															bgWidth, 					bgHeight;

	private			int[] 						playerInfo;
	private			Rect[] 					bgPosRects;
 
	public World(Context context){
		init(context);
	}
	
	public void init(Context context){
		this.context 	=context;
		//Screen info
		screenW = context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		screenH = context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		
		//Scrolling background
		botDetailBitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg1);
		botDetailBitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
		botDetailBitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
		
		bgWidth 			 = botDetailBitmap1.getWidth();
		bgHeight 			 = botDetailBitmap1.getHeight();
		
		//Init Objects
		hasUpdated 		= false;
		hasCollided		= false;
		newHighscore 	= false;
		playerInfo	 		= new int[2];
		playerInfo 			= Util.loadPlayerInfo(context);
		currentScore 		= 0;

		gameUi  				= new GameInterface(screenW, screenH, context);
		loseScreen 			= new LoseScreen(screenW, screenH);
		objects 				= new GameObjects();
		objects.init(context);
		
		boat	 				= new Boat(context);		
		
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
		if(gameUi.countdownShown){
			if(!hasCollided){								
				currentScore++;
				gameUi.updateScore(currentScore);
				gameUi.update();
				
				moveBackgrounds();
				objects.update();
				boat.update();
				
				if(boat.state == BOAT_STATE.BOOST && !objects.boost)
					boat.state = BOAT_STATE.FALLING;
				
				if(boat.state != BOAT_STATE.BOOST){
					for(Rect r : objects.obstacles){
						if(Util.collidesWith(boat.collisionRect, r) || boat.collisionRect.bottom > screenH){
							hasCollided = true;
							loseScreen.isVisible = true;
			    			Util.playSound(Util.dieSound);
						}
					}
				}
				
				boolean flag = false;
				for(Rect r : objects.fuelList){
					if(!flag && Util.collidesWith(boat.collisionRect, r)){
						objects.fuelOOB.add(r);
						flag = true;
						currentScore += 50;
						boat.pickUp();
					}
				}
				
				objects.fuelList.removeAll(objects.fuelOOB);
				
				if(!hasUpdated && hasCollided){
					int moneyEarned = calcPlayerCurrency();
					loseScreen.setFinalScore(currentScore, moneyEarned, newHighscore);
				}
			}
		} else {
			gameUi.updateScore(currentScore);
			gameUi.update();
		}
	}
	
	public int calcPlayerCurrency(){
		hasUpdated 				= true;
		int currencyEarned = (currentScore / 2) * Util.getUpgradeLevel(3);
		currentScore 				= currentScore * Util.getUpgradeLevel(3);
		playerInfo[0] 			 += currencyEarned;
		
		if(currentScore > playerInfo[1]){
			playerInfo[1] 			= currentScore;
			newHighscore 		= true;
		}
		Util.savePlayerInfo(context, playerInfo);
		return currencyEarned;
	}
		
	public void draw(Canvas c){	
		drawBackgrounds(c);
		objects.draw(c);
		boat.draw(c);
		
		if(!hasCollided){
			gameUi.draw(c);
		} else {
			loseScreen.draw(c);
		}
	}
	
	public void boost(){
		boat.state = BOAT_STATE.BOOST;
		Boat.fuel -= 200;
		objects.boost();
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