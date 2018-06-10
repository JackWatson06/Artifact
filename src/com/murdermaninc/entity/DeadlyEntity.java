package com.murdermaninc.entity;

import com.murdermaninc.blocks.Block;
import com.murdermaninc.level.Level;

public class DeadlyEntity extends Entity{

	public static int TOUCH = 1;

	public int damageType;
	
	
	public boolean direction; // the direction in which the entity is traveling which is used for collision detection
	
	public int xR, xL, yT, yB;
	
	public DeadlyEntity(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int damageType, boolean direction) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight);
		
		this.damageType = damageType;
		this.direction = direction;
		
	}
	
	public void setBounds(int xR, int xL, int yT, int yB) {
		this.xR = xR;
		this.xL = xL;
		this.yT = yT;
		this.yB = yB;
	}
	
	
	
	public void checkCollisions(Level level) {
		if(direction){
			
			
			Block top = level.getBlock((int)Math.floor((x + xR) / 64), (int) Math.floor((y + yB) / 64));
			Block bottom = level.getBlock((int)Math.floor((x + xR) / 64), (int) Math.floor((y + yT) / 64));
			
			if(top.collisions || bottom.collisions){
				remove(this, level);
			}
			
			if(x + xL > level.levelWidth * 64){
				remove(this, level);
			}
		}else{
			
			
			Block top = level.getBlock((int)Math.floor((x + xL) / 64), (int) Math.floor((y + yB) / 64));
			Block bottom = level.getBlock((int)Math.floor((x + xL) / 64), (int) Math.floor((y + yT) / 64));
			
			if(top.collisions || bottom.collisions){
				System.out.println("Remove left");
				remove(this, level);
			}
			
			if(x + xR < 0){
				remove(this, level);
			}
		}
	}
	
	public void checkDeathCollision(Player player){
		if(player.x + 63 - 8 >= x + xL && player.y + 4 <= y + yB && player.x + 8 <= x + xR && player.y + 63 >= y + yT){
			player.kill("deflat", x, y);
		}
	}

}
