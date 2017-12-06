package com.murdermaninc.decorations.mainShip;

import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class DecorationShip {

	protected int x, y;
	protected int xTile, yTile;
	protected int spriteWidth, spriteHeight;
	
	protected MainShipWorldManager manager;
	
	protected int[] Data;
	public DecorationShip(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight){
		this.manager = manager;
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
	}
	
	public void tick(){

	}
	
	public void render(Screen screen, float interpolation){
		
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		
		screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
	}
}
