package com.mudermaninc.entity;


import com.murdermaninc.blocks.Block;
import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class FriendlyLeaf extends Entity{
	
	private int startX = 0;
	private int startY = 0;
	
	private float notRoundedX = 0F;
	
	private boolean layFlat = false;
	private int layFlatY = 0;
	private int layFlatX = 0;
	
	private Animation animation = new Animation();
	
	private int[] Data;
	
	public FriendlyLeaf(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight){
		this.id = id;
		this.x = x;
		startX = x;
		notRoundedX = (float) x;
		this.y = y;
		startY = y;
		this.xTile = xTile;
		this.yTile = yTile;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
	}
	
	private float slope = -2F;
	
	private float bValue = 0F;
	
	

	@Override
	public void tick(Level level){
		if(!layFlat){
			if(bValue == 0F){
				bValue = (slope * (0 - startX)) + startY;
			}

			y = Math.round(slope * notRoundedX + bValue);
			notRoundedX -= 0.5;
			x = Math.round(notRoundedX);
			
			checkBottomCollisions(level);
			checkScreenCollisions(level);
		}
				
	}
	
	public void checkBottomCollisions(Level level){
		int yB = y + 32;
		int xL = x;
		int xR = x + 40;
		
		Block bL = level.getBlock((int)Math.floor(xL / 64), (int) Math.floor(yB / 64));
		Block bR = level.getBlock((int)Math.floor(xR / 64), (int) Math.floor(yB / 64));
		
		
		if((bL.collisions || bR.collisions) && !bL.notFull && !bR.notFull){
			layFlatY = bL.y * 64 - 4;
			
			if(animation.getCurrentSprite() == 0){
				layFlatX = x;
			}else if(animation.getCurrentSprite() == 1){
				layFlatX = x + 4;
			}else if(animation.getCurrentSprite() == 2){
				layFlatX = x + 4;
			}else if(animation.getCurrentSprite() == 3){
				layFlatX = x + 8;
			}else if(animation.getCurrentSprite() == 4){
				layFlatX = x + 12;
			}else if(animation.getCurrentSprite() == 5){
				layFlatX = x + 12;
			}else{
				layFlatX = x + 16;
			}

			
			layFlat = true;
		}
	}
	
	public void checkScreenCollisions(Level level){
		

		if(y > level.levelHeight * 64){
			remove(this, level);
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation, float testInterpolation){
		if(AnimationData == null){
			Data = screen.loadData(9, 14, 1, 1, 4, "Icons");
			AnimationData = animation.loadAnimationData(screen, "Icons", 4, 7, 2, 14, spriteWidth, spriteHeight);
		}
		
		if(!layFlat){
			animation.animateContinuous(screen, AnimationData, true, 5.5F, spriteWidth, spriteHeight, x, y, 12, 4, interpolation);
		}else{
			screen.renderData(Data, layFlatX, layFlatY, 1, 1, 4);
		}
	}
}
