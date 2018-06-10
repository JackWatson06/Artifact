package com.murdermaninc.decorations;

import com.murdermaninc.entity.Bug;
import com.murdermaninc.entity.Entity;
import com.murdermaninc.entity.Player;

public class BugTree extends Decoration{

	private int spawnCounter = 0;
	
	public BugTree(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}
	
	@Override
	public void tick(Player player) {
		
		if(spawnCounter >= 360) {
			
			//Entity.entities.add(new Bug());
			//Entity.entities.add(new Bug());
			
			spawnCounter = 0;
			
		}else {
			spawnCounter++;
		}
		
		
	}

}
