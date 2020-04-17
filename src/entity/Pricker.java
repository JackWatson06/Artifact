package com.murdermaninc.entity;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Pricker extends DeadlyEntity{

	
	public Pricker(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean direction){
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, DeadlyEntity.TOUCH, direction);
		
		this.speed = 3;
		
		setBounds(12, 0, 0, 12);
	}
	
	@Override
	public void tick(Level level, Player player){
		if(direction){
			x+=speed;
		}else{
			x-=speed;
		}
		
		
		checkCollisions(level);
		checkDeathCollision(player);
	}
	
	/*public void checkCollisions(Level level){
		if(direction){
			xR = x + 12;
			yB = y + 8;
			yT = y + 4;
			
			Block top = level.getBlock((int)Math.floor(xR / 64), (int) Math.floor(yB / 64));
			Block bottom = level.getBlock((int)Math.floor(xR / 64), (int) Math.floor(yT / 64));
			
			if(top.collisions || bottom.collisions){
				remove(this, level);
			}
			
			if(x > level.levelWidth * 64){
				remove(this, level);
			}
		}else{
			xL = x;
			yB = y + 8;
			yT = y + 4;
			
			Block top = level.getBlock((int)Math.floor(xL / 64), (int) Math.floor(yB / 64));
			Block bottom = level.getBlock((int)Math.floor(xL / 64), (int) Math.floor(yT / 64));
			
			if(top.collisions || bottom.collisions){
				remove(this, level);
			}
			
			if(x + 12 < 0){
				remove(this, level);
			}
		}
		
	}*/
	
	/*public void checkDeathCollision(){
		if(player.x + 63 - 8 >= x  && player.y + 4 <= y + 12 && player.x + 8 <= x + 12 && player.y + 63 >= y){
		}
	}*/
	
	@Override
	public void render(Screen screen, float interpolation){
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");
		
		screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
	}
}
