package com.murdermaninc.decorations;


import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;

public class Sunflower extends Decoration{

	private Animation animation = new Animation();

	
	public Sunflower(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);
	}
	public void tick(){
		
	}	

	public void render(Screen screen, float interpolation){
		if(AnimationData == null) AnimationData = animation.loadAnimationData(screen, "icons", 4, 8, xTile, yTile, spriteWidth, spriteHeight);

		animation.animateContinuous(screen, AnimationData, false, 1.333333F, 1, 2, x, y, 8, 4, interpolation);
		
	}

}
