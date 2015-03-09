package com.tim.plainsailing;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
	private boolean running 	= false;
	private double 	timeStep, 	elapsed, 
					previous, 	current;
	
	private GameView 			view;
	private SurfaceHolder 		holder;
	
	World				world;
		
	public GameThread(GameView gameView) {
		view 	= gameView;
		holder 	= view.getHolder();
		timeStep = 1.0 / 60.0;
		world 	= new World(view.getContext());
		
    	int curUpgrades[] = Util.loadUpgradesFile(view.getContext(), Util.upgradesSize);
	}
	
	public void setRunning(boolean r){ running = r; }
			
	@Override
	public void run(){
		update();
		previous = System.currentTimeMillis();
		while(running){	
			current = System.currentTimeMillis();
			elapsed = current - previous;
			
			if(elapsed > timeStep){
				update();
				previous = System.currentTimeMillis();
			}
			
			draw();
		}
	}

	private void update() {
		world.update();
	}
	
	private void draw() {
		Canvas canvas = holder.lockCanvas();
		
		if(canvas != null){
			canvas.drawRGB(123, 214, 224);
			world.draw(canvas);
			
			holder.unlockCanvasAndPost(canvas);
		}
	}
}
