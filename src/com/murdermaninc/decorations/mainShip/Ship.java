package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class Ship extends DecorationShip{

	private Monitor monitor;
	
	private Animation animation = new Animation();
	private ArrayList<int[]> animationData;
	
	private boolean playAnimation = false;
	
	public Ship(MainShipWorldManager manager, Monitor monitor, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.monitor = monitor;

	}
	
	@Override
	public void tick(){
		
		if(manager.player != null){
			if(manager.player.x + 8 >= x && manager.player.x + 63 - 8 <= x + 152 && manager.player.y + 4 >= y && manager.player.y + 63 <= y + 140 && manager.input.e && monitor.getLevelAvailability()){

				playAnimation = true;
				manager.player = null;
				manager.setPlayerMain();
				manager.input.e = false;
			}
		}
		
		if(!animation.once){
			manager.loadLevel(monitor.currentLevel, monitor.currentLevelNumber, monitor.currentWorldNumber);
		}
		
		//This is the path the animation follows
		
		if(y >= 742 && y <= 876 && animation.animationCounter > 32 && playAnimation){
			double equation = Math.sqrt(40000 - (6.7711 * Math.pow(y - 809, 2))) + 1250;
			x = (int) equation;
			y--;
		}else if(y >= 608 && y <= 742){
			double equation = -Math.sqrt(40000 - (6.7711 * Math.pow(y - 675, 2))) + 1446;
			x = (int) equation;
			y--;
		}else if(y >= 512 && y <= 608){
			double equation = Math.sqrt(34969 - (12.7356 * Math.pow(y - 560, 2))) + 1274;
			x = (int) equation;
			y--;
		}
		
		
	}
	
	//21 sprites
	
	@Override
	public void render(Screen screen, float interpolation){
		if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "shipIcons", 4, 57, xTile, yTile, spriteWidth, spriteHeight, 13, 5);
		
		if(playAnimation){
		//animation.animateContinuous(screen, animationData, false, 7.0F, spriteWidth, spriteHeight, x, y, 13, 4);
			animation.animateOnce(screen, animationData, false, 8.0F, spriteWidth, spriteHeight, x, y, 57, 4, interpolation);
		}else{
			screen.render(x, y, 12, 0, spriteWidth, spriteHeight, 4, "shipIcons");
		}
	}

}
