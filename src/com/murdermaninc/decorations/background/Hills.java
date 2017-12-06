package com.murdermaninc.decorations.background;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Hills extends DecorationsBackground{

	//This offset is for the scrolling of the hill so more hills maintain their original x
	int xOffset = 0;
	int yOffset;
	//int lastScreenY = -1;
	
	public Hills(int decorationId, int objectNumber, int x, int y, int spriteWidth, int spriteHeight, int xTile, int yTile) {
		super(decorationId, objectNumber, x, y, spriteWidth, spriteHeight, xTile, yTile);
		xOffset = x;
		yOffset = y;

	}
	
	
	//This number should typically be between 2F and 1F.
	//2.0 = 50% scroll speed / 1.5 = 66.66% scroll speed / 1.2 = 83.3% / 10.0 = 10%
	// higher percentage the terrain more closely follows the player giving it a slower scrolling appearance and vice versa
	float slowDownScaleY = 1.2F;
	float slowDownScaleX = 1.5F;

	//2184 + 1080 * 1.5 / 1.5 - 256 = 3008
	
	
	@Override
	public void tick(Screen screen, Level level){

		
		x =  (int) (screen.screenX / slowDownScaleX) + xOffset;
		y = (((level.levelHeight * 64 - screen.height) - (screen.screenY)) / -slowDownScaleY) + yOffset;
		

	}

}
