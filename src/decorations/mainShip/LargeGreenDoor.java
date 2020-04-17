package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class LargeGreenDoor extends DecorationShip {
	
	private Animation animation = new Animation();
	private ArrayList<int[]> animationData;
	
	private boolean collisions = false;
	
	public LargeGreenDoor(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean collisions) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.collisions = collisions;
	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(collisions){
			if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "largeDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);

			
			if(manager.player != null){
				animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 0, 384, 0, 320, 3, interpolation);
			}else{
				screen.render(x, y, 0, 0, spriteWidth, spriteHeight, 4, "largeDoor");
			}
		}else{
			screen.render(x, y, 0, 0, spriteWidth, spriteHeight, 4, "largeDoor");
		}
	}
}
