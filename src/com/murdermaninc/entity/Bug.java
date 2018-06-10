package com.murdermaninc.entity;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Bug extends DeadlyEntity{

	public Bug(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean direction) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, DeadlyEntity.TOUCH, direction);
		
	}

	@Override
	public void tick(Level level, Player player) {
		
	}
	
	
	@Override
	public void render(Screen screen, float interpolation) {
		
	}
	
}
