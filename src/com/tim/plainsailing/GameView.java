package com.tim.plainsailing;

import com.tim.plainsailing.Boat.BOAT_STATE;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	final static String TAG 		= "Plain_Sailing";
	GameThread 						gameThread 	= null;
	SurfaceHolder 						surfaceHolder;
	GestureDetectorCompat detector;
		
	public GameView(Context context) {
		super(context);
		init();
	}
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init(){
    	getHolder().addCallback(this);
        detector 			= new GestureDetectorCompat(super.getContext(), new MyGestureListener());
    	gameThread 	= new GameThread(this); 
    	setFocusable(true);
    }
                
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(holder.getSurface().isValid()){
			gameThread.setRunning(true);
			gameThread.start();
		} else {
			Log.d(TAG, "Surface not valid");
		}		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameThread.setRunning(false);
		gameThread = null;
	}

    @Override
    public boolean onTouchEvent(MotionEvent event){    
    	if(gameThread.world.gameUi.countdownShown){
	    	if(event.getAction() == MotionEvent.ACTION_UP && !detector.onTouchEvent(event) ){
	    		onUp(event);
	    		performClick();
	    	} else {
	    	detector.onTouchEvent(event);
	    	}
    	}
    	return true;
    }
    
    public void onUp(MotionEvent event){
    	gameThread.world.boat.state = BOAT_STATE.FALLING;
    }
	
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
	    @Override
	    public boolean onDown(MotionEvent event) { 
	    	if(gameThread.world.loseScreen.isVisible){
	    		if(event.getX() >= gameThread.world.loseScreen.replayButton.left && event.getX() <= gameThread.world.loseScreen.replayButton.right)
	    			if(event.getY() >= gameThread.world.loseScreen.replayButton.top && event.getY() <= gameThread.world.loseScreen.replayButton.bottom)
	    				((GameActivity) getContext()).restart();
	    		
	    		if(event.getX() >= gameThread.world.loseScreen.menuButton.left && event.getX() <= gameThread.world.loseScreen.menuButton.right)
	    			if(event.getY() >= gameThread.world.loseScreen.menuButton.top && event.getY() <= gameThread.world.loseScreen.menuButton.bottom){
	    				gameThread.setRunning(false);
	    				((GameActivity) getContext()).onBackPressed();
	    			}
	    				
	    		
	    	} else if(Boat.fuel > 0){
    			Util.playSound(Util.jumpSound);
    			gameThread.world.boat.state = BOAT_STATE.RISING;
	    	}
	    	
    		return true;
	    }
	
	    @Override
	    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
	    	if(velocityX > velocityY){
	    		if(velocityX > 0){	//swipe left to right
	    			Util.playSound(Util.boostSound);
	    			gameThread.world.boost();
	    			return true;
	    		} 
	    	}
	    	return false;
	    }
	}
}
