package com.murdermaninc.blocks;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class AirCollisionBlock extends Block{

	public AirCollisionBlock(int id, int x, int y) {
		super(id, x, y);
		collisions = true;
	}

	public void loadData(Screen screen){
		
	}
	
	public void render(Screen screen, Level level, int x, int y){

	}
}
