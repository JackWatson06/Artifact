package com.murdermaninc.blocks;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class AirBlock extends Block{

	public AirBlock(int id, int x, int y) {
		super(id, x, y);
		collisions = false;
	}

	public void loadData(Screen screen){
		
	}
	
	public void render(Screen screen, Level level, int x, int y){

	}
}
