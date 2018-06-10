package com.murdermaninc.levelCreator;

public class Block {

	public static Block[] blocks;
	
	public int id;
	public int x,y;
	public int xTile,yTile;
	
	public Block(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public Block(int id, int x, int y, int xTile, int yTile){
		this.id = id;
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
	}
	
	public void render(Screen screen){
		if(id <= 46){
			screen.render(x * 64, y * 64, xTile, yTile, 1, 1, 4, "iconsPlains");
		}else{
			screen.render(x * 64, y * 64, xTile, yTile, 1, 1, 4, "iconsForest");
		}
	}
	
	public void renderNotScaled(Screen screen){
		if(id <= 46){
			screen.render(x, y, xTile, yTile, 1, 1, 4, "iconsPlains");
		}else{
			screen.render(x, y, xTile, yTile, 1, 1, 4, "iconsForest");
		}
	}
}
