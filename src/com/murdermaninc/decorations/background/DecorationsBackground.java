package com.murdermaninc.decorations.background;

import java.util.ArrayList;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class DecorationsBackground {

	public float x, y;
	public int spriteWidth, spriteHeight;
	protected int xTile;
	protected int yTile;
	private int[] Data;
	protected ArrayList<int[]> AnimationData;
	public int id;
	public int objectNumber;
	
	
	public DecorationsBackground(int decorationId, int objectNumber, float x, float y, int spriteWidth, int spriteHeight, int xTile, int yTile){
		//This xTile and yTile represent that starting top leftmost corner of the sprite
		this.id = decorationId;
		this.objectNumber = objectNumber;
		this.x = x;
		this.y = y;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.xTile = xTile;
		this.yTile = yTile;
		
	}
	
	public void tick(Screen screen, Level level){
		
	}
	
	public void render(Screen screen, float interpolation){
		if(Data == null){
			this.Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "background");
		}
		screen.renderData(Data, (int) Math.round(x), (int) Math.round(y), spriteWidth, spriteHeight, 4);
	}
}
