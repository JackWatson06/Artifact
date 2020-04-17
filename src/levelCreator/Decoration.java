package com.murdermaninc.levelCreator;

import java.util.ArrayList;

public class Decoration {

	public static ArrayList<Decoration> decorations = new ArrayList<Decoration>();
	
	public int id;
	public int x, y;
	public int xTile, yTile;
	public int spriteWidth, spriteHeight;
	public int render;
	
	
	
	public Decoration(int id, int x, int y, int startXTile, int startYTile, int spriteWidth, int spriteHeight, int render){
		this.id = id;
		this.x = x;
		this.y = y;
		this.xTile = startXTile;
		this.yTile = startYTile;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.render = render;
	}
	
	public void render(Screen screen){
		if((id >= 2000 && id <= 2038) || (id >= 2750 && id <= 2765) || (id >= 3000 && id <= 3047)){
			screen.render(x, y, xTile, yTile, spriteWidth, spriteHeight, 4, "iconsPlains");
		}else{
			screen.render(x, y, xTile, yTile, spriteWidth, spriteHeight, 4, "iconsForest");
		}
	}
}
