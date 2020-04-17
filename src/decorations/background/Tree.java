package com.murdermaninc.decorations.background;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Tree extends DecorationsBackground{

	private float slowDownScaleX = 1.0F;
	private float slowDownScaleY = 1.45F;
	private int xOffset = 0;
	private int yOffset = 0;
	
	private int[] Leaves;
	private int[] UpperLeaves;
	private int[] Trunk;
	private int[] Stump;
	
	public Tree(int decorationId, int objectNumber, float x, float y, int spriteWidth, int spriteHeight, int xTile, int yTile, float slowDownScaleX) {
		super(decorationId, objectNumber, x, y, spriteWidth, spriteHeight, xTile, yTile);
		this.slowDownScaleX = slowDownScaleX;
		this.xOffset = (int) x;
		this.yOffset = (int) y;

	}
	
	@Override
	public void tick(Screen screen, Level level){

		
		x = (screen.screenX / slowDownScaleX) + xOffset;
		y = (((level.levelHeight * 64 - screen.height) - (screen.screenY)) / -slowDownScaleY) + yOffset;

	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(UpperLeaves == null) UpperLeaves = screen.loadData(13, 11, 11, 1, 4, "background");
		if(Leaves == null) Leaves = screen.loadData(0, 0, 11, 1, 4, "background");
		if(Trunk == null){
			if(id == 0){
				Trunk = screen.loadData(13, 0, 4, 11, 4, "background");
			}else if(id == 1){
				Trunk = screen.loadData(17, 0, 4, 11, 4, "background");
			}else{
				Trunk = screen.loadData(21, 0, 3, 11, 4, "background");
			}	
		}
		if(Stump == null){
			if(id == 0){
				Stump = screen.loadData(0, 1, 7, 6, 4, "background");
			}else if(id == 1){
				Stump = screen.loadData(7, 1, 6, 6, 4, "background");
			}else{
				Stump = screen.loadData(0, 7, 7, 6, 4, "background");
			}	
		}
		
		x = Math.round(x);
		
		if(id == 0){
			screen.renderData(Leaves, (int) x, (int) y, 11, 1, 4);
			screen.renderData(UpperLeaves, (int) x, (int) y - 60, 11, 1, 4);
			screen.renderData(Trunk, (int) x + 236, (int) y + 56, 4, 11, 4);
			screen.renderData(Stump, (int) x + 140, (int) y + 704, 7, 6, 4);
		}else if(id == 1){
			screen.renderData(Leaves, (int) x, (int) y, 11, 1, 4);
			screen.renderData(UpperLeaves, (int) x, (int) y - 60, 11, 1, 4);
			screen.renderData(Trunk, (int) x + 220, (int) y + 56, 4, 11, 4);
			screen.renderData(Stump, (int) x + 128, (int) y + 740, 6, 6, 4);
		}else{
			screen.renderData(Leaves, (int) x, (int) y, 11, 1, 4);
			screen.renderData(UpperLeaves, (int) x, (int) y - 60, 11, 1, 4);
			screen.renderData(Trunk, (int) x + 244, (int) y + 56, 3, 11, 4);
			screen.renderData(Stump, (int) x + 164, (int) y + 740, 7, 6, 4);
		}
	}

	
}
