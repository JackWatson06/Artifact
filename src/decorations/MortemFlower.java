package com.murdermaninc.decorations;

import com.murdermaninc.entity.Entity;
import com.murdermaninc.entity.Player;
import com.murdermaninc.entity.Pricker;

public class MortemFlower extends DeadlyDecoration{

	private boolean direction = false;
	
	private int spawnCounter = 0;
	
	public MortemFlower(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render, String deathTag, int leftOffset, int rightOffset, int topOffset, int bottomOffset, boolean direction) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render, deathTag, leftOffset, rightOffset, topOffset, bottomOffset);
		this.direction = direction;
	}
	
	@Override
	public void tick(Player player){
		if(spawnCounter >= 110){
			if(direction){
				Entity.entities.add(new Pricker(1, x + 36 , y + 68, 1, 16, 1, 1, direction));
				Entity.entities.add(new Pricker(1, x + 28 , y + 96, 1, 16, 1, 1, direction));
			}else{
				Entity.entities.add(new Pricker(1, x + 16, y + 68, 9, 16, 1, 1, direction));
				Entity.entities.add(new Pricker(1, x + 24, y + 96, 9, 16, 1, 1, direction));
			}
			spawnCounter = 0;
		}else{
			spawnCounter++;
		}
		
		checkCollisions(player);
	}
	
	
	

}
