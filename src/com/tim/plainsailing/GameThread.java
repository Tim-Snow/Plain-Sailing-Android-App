package com.tim.plainsailing;

import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
	GestureDetectorCompat 	mDetector;
	private boolean 		running = false;

	GameView 		view;
	SurfaceHolder 	holder;
	World			world;
	
	public GameThread(GameView gameView) {
		view 		= gameView;
		holder 		= view.getHolder();
		mDetector 	= new GestureDetectorCompat(view.getContext(), new GameGestureListener());
		world 		= new World(view.getContext());	
	}
	
	public void setRunning(boolean r){ running = r; }
			
	@Override
	public void run(){
		while(running){
			update();
			draw();
		}
	}

	private void update() {
		world.update();
	}
	
	private void draw() {
		Canvas canvas = holder.lockCanvas();
		
		canvas.drawRGB(12, 50, 150);
		world.draw(canvas);
		
		holder.unlockCanvasAndPost(canvas);
	}
		
	class GameGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) { 
        	world.boat.setRising();
            return true;
        }
    }
}
