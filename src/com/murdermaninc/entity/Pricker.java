package com.mudermaninc.entity;

import com.murdermaninc.blocks.Block;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Pricker extends Entity{

	private boolean direction = false;
	private Player player;
	
	public Pricker(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean direction, Player player){
		this.id = id;
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
		this.direction = direction;
		this.spriteHeight = spriteHeight;
		this.spriteWidth = spriteWidth;
		this.speed = 3;
		this.player = player;
	}
	
	@Override
	public void tick(Level level){
		if(direction){
			x+=speed;
		}else{
			x-=speed;
		}
		
		
		checkCollisions(level);
		checkDeathCollision();
	}
	
	public void checkCollisions(Level level){
		if(direction){
			int xR = x + 12;
			int yB = y + 8;
			int yT = y + 4;
			
			Block top = level.getBlock((int)Math.floor(xR / 64), (int) Math.floor(yB / 64));
			Block bottom = level.getBlock((int)Math.floor(xR / 64), (int) Math.floor(yT / 64));
			
			if(top.collisions || bottom.collisions){
				remove(this, level);
			}
			
			if(x > level.levelWidth * 64){
				remove(this, level);
			}
		}else{
			int xL = x;
			int yB = y + 8;
			int yT = y + 4;
			
			Block top = level.getBlock((int)Math.floor(xL / 64), (int) Math.floor(yB / 64));
			Block bottom = level.getBlock((int)Math.floor(xL / 64), (int) Math.floor(yT / 64));
			
			if(top.collisions || bottom.collisions){
				remove(this, level);
			}
			
			if(x + 12 < 0){
				remove(this, level);
			}
		}
		
	}
	
	public void checkDeathCollision(){
		if(player.x + 63 - 8 >= x  && player.y + 4 <= y + 12 && player.x + 8 <= x + 12 && player.y + 63 >= y){
			player.kill("deflat", x, y);
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation, float testInterpolation){
		screen.render(x, y, xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");
	}
}
