package com.tim.plainsailing;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
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
		gameThread.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
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
			Log.d("GameSurface", "not valid");
		}		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		gameThread.setRunning(false);
		gameThread = null;
	}
}

