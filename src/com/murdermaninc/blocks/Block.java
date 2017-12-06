package com.murdermaninc.blocks;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Block {

	//TODO remove the * 64 for the render portion completely unecessary becuase this can be done in the constructer and of all things it just slows down the program.
	
	public static Block[] blocks;
	public int x, y;
	
	public final int id;
	public final int xTile, yTile;
	
	public boolean collisions = true;
	public boolean notFull = false;
	public int collisionPixelsHeight = 0;
	
	public int[] Data;
	
	public Block(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
		xTile = -101;
		yTile = -101;
	}
	
	public Block(int id, int x, int y, int xTile, int yTile){
		this.id = id;
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
	}
	
	public void loadData(Screen screen){
			Data = screen.loadData(xTile, yTile, 1, 1, 4, "Icons");
	}
	
	public void render(Screen screen, Level level, int x, int y){
		screen.renderData(Data, x * 64, y * 64, 1, 1, 4);
	}
	
	
	
	
}
