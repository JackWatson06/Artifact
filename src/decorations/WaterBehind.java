package com.murdermaninc.decorations;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;

public class WaterBehind extends Decoration{

	private Animation animation = new Animation();
	
	public WaterBehind(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(AnimationData == null) AnimationData = animation.loadAnimationDataRectangle(screen, "Icons", 4, 5, xTile, yTile, spriteWidth, spriteHeight, 6, 1);
			
		
		animation.animateContinuous(screen, AnimationData, false, 7.0F, spriteWidth, spriteHeight, x, y, 5, 4, interpolation);
			
	}

}
