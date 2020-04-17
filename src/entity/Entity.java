package com.murdermaninc.entity;

import java.util.ArrayList;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;

public class Entity {

	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public int x,y;
	public int xTile, yTile;
	public int spriteWidth, spriteHeight;
	public int lastX, lastY;
	public int speed;
	public int health;
	public int id;
	public String name;
	
	public ArrayList<int[]> AnimationData;
	public int[] Data;
	
	public Entity(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight){
		this.id = id;
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
	}
	
	
	public void tick(Level level, Player player){
		
	}
	
	public void render(Screen screen, float interpolation){
		
	}
	
	public void remove(Entity entity, Level level){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).equals(entity)){
				level.deletedEntity = true;
				entities.remove(i);
				i--;
			}
		}
	}
	
	

	
}
