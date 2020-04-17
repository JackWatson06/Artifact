package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class LargeShipDoor extends DecorationShip{

	public static String ORANGE = "orange";
	public static String PURPLE = "purple";
	public static String YELLOW = "yellow";
	public static String GREEN = "green";
	
	private Animation animation = new Animation();
	private ArrayList<int[]> animationData;

	private boolean collisions = false;
	private String doorColor = "";
	
	private int id = 0;

	public LargeShipDoor(MainShipWorldManager manager, int doorId, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean collisions, int startingSprite, String color) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.collisions = collisions;
		this.doorColor = color;
		this.id = doorId;
		if(startingSprite > 0){
			animation.setCurrentSprite(startingSprite);
		}
	}

	@Override
	public void tick(){
		if(manager.player != null && collisions){
			if(manager.player.x + 8>= x + 76 && manager.player.x + 63 - 8 <= x + 312 && manager.player.y + 4 >= y + 68 && manager.player.y + 63 <= y + (spriteHeight * 64) && manager.input.e){
				if(manager.currentRoom.equals("dorm")){
					if(id == 1){
						manager.triggerStageSwitch("corridor", 1);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("corridor")){
					if(id == 1){
						manager.triggerStageSwitch("dorm", 2);
						manager.input.e = false;
					}else if(id == 4){
						manager.triggerStageSwitch("kingsChambers", 1);
						manager.input.e = false;
					}
				}
			}
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(collisions){
			if(doorColor.equals("green")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "green", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "green");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 68, (spriteHeight * 64), 76, 312, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("yellow")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "yellow", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "yellow");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 68, (spriteHeight * 64), 76, 312, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}
		}else{
			
			if(doorColor.equals("orange")){
				if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("purple")){
				if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}
		}
		
	}

}
