package com.murdermaninc.decorations;

import com.mudermaninc.entity.Player;

public class DeadlyDecoration extends Decoration{

	private int leftOffset, rightOffset, topOffset, bottomOffset;
	private String deathTag;
	
	
	public DeadlyDecoration(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render, String deathTag, int leftOffset, int rightOffset, int topOffset, int bottomOffset) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);
		this.leftOffset = leftOffset * 4;
		this.rightOffset = rightOffset * 4;
		this.topOffset = topOffset * 4;
		this.bottomOffset = bottomOffset * 4;
		this.deathTag = deathTag;
	}
	
	@Override
	public void tick(Player player){
		
		checkCollisions(player);
		
	}
	
	public void checkCollisions(Player player){
		if(player.x + 63 - 8 >= x  + leftOffset && player.y + 4 <= y + ((spriteHeight * 64) - 1)  - bottomOffset && player.x + 8 <= x + ((spriteWidth * 64) - 1) - rightOffset && player.y + 63 >= y + topOffset){
			player.kill(deathTag, x, y);
		}
	}

}
