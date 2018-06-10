package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class Door extends DecorationShip{

	public static String BLUE = "blue";
	public static String BLACK = "black";
	public static String LIGHTGREEN = "lightgreen";
	public static String DARKGREEN = "darkgreen";
	public static String WHITE = "white";
	public static String YELLOW = "yellow";
	public static String GREY = "grey";
	
	private Animation animation = new Animation();
	private ArrayList<int[]> animationData;
	
	private boolean collisions = false;
	
	private int id = 0;
	
	private String doorColor = "";
	
	public boolean lockDoor = false;
	
	public Door(MainShipWorldManager manager, int doorId, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean collisions, int startingSprite, String color) {
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
		
		//This detects if the player enter the door and chooses the correct stage switch accordingly.
		
		if(manager.player != null && collisions && !lockDoor){
			if(manager.player.x + 8 >= x + 52 && manager.player.x + 63 - 8 <= x + 208 && manager.player.y + 4 >= y + 96 && manager.player.y + 63 <= y + (spriteHeight * 64) && manager.input.e){
				//System.out.println(manager.currentRoom);
				if(manager.currentRoom.equals("room")){
					if(id == 1){
						manager.triggerStageSwitch("dorm", 1);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("dorm")){
					if(id == 1){
						manager.triggerStageSwitch("room", 0);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("kingsChambers")){
					if(id == 1){
						manager.triggerStageSwitch("podRoom", 1);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("podRoom")){
					if(id == 2){
						manager.triggerStageSwitch("lightGreenLaunch", 1);
						manager.input.e = false;
					}else if(id == 3){
						manager.triggerStageSwitch("darkGreenLaunch", 1);
						manager.input.e = false;
					}else if(id == 4){
						manager.triggerStageSwitch("whiteLaunch", 1);
						manager.input.e = false;
					}else if(id == 5){
						manager.triggerStageSwitch("yellowLaunch", 1);
						manager.input.e = false;
					}else if(id == 6){
						manager.triggerStageSwitch("greyLaunch", 1);
						manager.input.e = false;
					}else if(id == 1) {
						manager.triggerStageSwitch("kingsChambers", 2);
						manager.input.e = false;
					}

				}else if(manager.currentRoom.equals("lightGreenLaunch")){
					if(id == 1){
						manager.triggerStageSwitch("podRoom", 2);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("darkGreenLaunch")){
					if(id == 1){
						manager.triggerStageSwitch("podRoom", 3);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("whiteLaunch")){
					if(id == 1){
						manager.triggerStageSwitch("podRoom", 4);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("yellowLaunch")){
					if(id == 1){
						manager.triggerStageSwitch("podRoom", 5);
						manager.input.e = false;
					}
				}else if(manager.currentRoom.equals("greyLaunch")){
					if(id == 1){
						manager.triggerStageSwitch("podRoom", 6);
						manager.input.e = false;
					}
				}
			}
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation){
		
		
		
		if(collisions && !lockDoor){
			if(doorColor.equals("blue")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "blueDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "blueDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("black")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "blackDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "blackDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("lightgreen")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "lightgreenDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "lightgreenDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("darkgreen")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "darkgreenDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "darkgreenDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("white")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "whiteDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "whiteDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("yellow")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "yellowDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "yellowDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}else if(doorColor.equals("grey")){
				if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "greyDoor", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "greyDoor");

				if(manager.player != null){
					animation.animateCollision(screen, manager.player, animationData, true, 10.0F, spriteWidth, spriteHeight, x, y, 96, (spriteHeight * 64), 52, 208, 3, interpolation);
				}else{
					screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
				}
			}
			
			
			
		}else{

			if(doorColor.equals("blue")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "blueDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("black")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "blackDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("lightgreen")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "lightgreenDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("darkgreen")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "darkgreenDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("white")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "whiteDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("yellow")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "yellowDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}else if(doorColor.equals("grey")){
				if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "greyDoor");
				screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
			}
			
			
			
		}
	}

}
