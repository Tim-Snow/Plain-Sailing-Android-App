package com.tim.plainsailing;

import com.tim.plainsailing.Boat.BOAT_STATE;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	final static String TAG = "Plain_Sailing";
	GameThread gameThread = null;
	SurfaceHolder surfaceHolder;
		
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
    	gameThread = new GameThread(this); 
    	setFocusable(true);
    }
        
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	int action = event.getAction();
    	switch(action){
    	case MotionEvent.ACTION_DOWN:
    		if(gameThread.world.gameUi.boost){
    			gameThread.world.boost(event.getX(), event.getY());
    			gameThread.world.boat.state 	= BOAT_STATE.BOOST;
    			gameThread.world.gameUi.boost 	= false;
    			break;
    		}
    		
    		if(event.getX() >= gameThread.world.gameUi.boostRect.left && event.getY() >= gameThread.world.gameUi.boostRect.top){
    			if(gameThread.world.boat.fuel > 99){
    				gameThread.world.gameUi.boost = true;
    				gameThread.world.boat.fuel -= 100;
    			}
    			break;
    		}
    		    		
    		if(gameThread.world.boat.fuel > 0){
    			gameThread.world.boat.state = BOAT_STATE.RISING;
    			break;
    		}
    	case MotionEvent.ACTION_UP:
    		if(gameThread.world.boat.state != BOAT_STATE.BOOST)
    			gameThread.world.boat.state = BOAT_STATE.FALLING;
    		break;	
    	}
    	return true;
    }

    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	
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
}

