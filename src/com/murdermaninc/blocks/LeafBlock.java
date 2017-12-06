package com.murdermaninc.blocks;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class LeafBlock extends Block{

	public LeafBlock(int id, int x, int y, int xTile, int yTile) {
		super(id, x, y, xTile, yTile);
		collisions = true;
		notFull = true;
		collisionPixelsHeight = 7 * 4;
	}
	
	
	public void render(Screen screen, Level level, int x, int y){
		screen.renderData(Data, x * 64, y * 64, 1, 1, 4);
	}

}
