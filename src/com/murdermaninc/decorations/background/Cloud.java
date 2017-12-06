package com.murdermaninc.decorations.background;


import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.BackgroundManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Cloud extends DecorationsBackground{

	
	private BackgroundManager backgroundManager;
	private Animation animation = new Animation();
	
	
	int yOffset = 0;
	
	//xOffset is the x coordinate of the object without the scroll scale applied
	float xOffset = 0;
	int lastScreenY = -1;
	
	public Cloud(int decorationId, int objectNumber, int x, int y, int spriteWidth, int spriteHeight, int xTile, int yTile, BackgroundManager backgroundManager) {
		super(decorationId, objectNumber, x, y, spriteWidth, spriteHeight, xTile, yTile);
		this.backgroundManager = backgroundManager;
		this.yOffset = y;
		this.xOffset = x;
	}
	
	float slowDownScaleY = 1.2F;
	float slowDownScaleX = 1.5F;
	
	public void tick(Screen screen, Level level){
		
		x -= 0.5F;
		xOffset -= 0.5F;
		
		
		y = (((level.levelHeight * 64 - screen.height) - (screen.screenY)) / -slowDownScaleY) + yOffset;
		x =  (int) (screen.screenX / slowDownScaleX) + xOffset;
		
		if(xOffset + (spriteWidth * 64) < 0){
			destroy();
		}
	}
	
	public void render(Screen screen, float interpolation){
		if(AnimationData == null) AnimationData = animation.loadAnimationData(screen, "background", 4, 4, xTile, yTile, spriteWidth, spriteHeight);
		
		animation.animateContinuous(screen, AnimationData, false, 0.8F, spriteWidth, spriteHeight, (int) Math.round(x), (int) Math.round(y), 4, 4, interpolation);
	}
	
	public void destroy(){
		backgroundManager.destroyObject(id);
	}
	

}
