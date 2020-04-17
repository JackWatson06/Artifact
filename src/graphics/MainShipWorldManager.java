package com.murdermaninc.graphics;

import java.util.ArrayList;

import com.murdermaninc.entity.Player;
import com.murdermaninc.decorations.mainShip.Bed;
import com.murdermaninc.decorations.mainShip.DecorationShip;
import com.murdermaninc.decorations.mainShip.Door;
import com.murdermaninc.decorations.mainShip.Elevator;
import com.murdermaninc.decorations.mainShip.Intercom;
import com.murdermaninc.decorations.mainShip.King;
import com.murdermaninc.decorations.mainShip.LargeShipDoor;
import com.murdermaninc.decorations.mainShip.Lever;
import com.murdermaninc.decorations.mainShip.Monitor;
import com.murdermaninc.decorations.mainShip.Ship;
import com.murdermaninc.decorations.mainShip.Sign;
import com.murdermaninc.level.LevelSequencing;
import com.murdermaninc.main.InputManager;
import com.murdermaninc.main.Main;

public class MainShipWorldManager {

	//ON SWITHC REMOVE SPRITE SHEETS
	
	public Screen shipScreen;
	public Background currentBackground;
	
	public int width, height;
	
	public InputManager input;
	private Main main;
	
	public Player player;
	
	public String currentRoom = new String("");
	
	private ArrayList<DecorationShip> decorations = new ArrayList<DecorationShip>();
	
	private LevelSequencing levelS = new LevelSequencing();
	public ShipData shipData = new ShipData();
	
	long beginningTime = 0;
	
	boolean once = true;
	boolean lock = false;
	
	public MainShipWorldManager(Main main, String section, int loadLevel, InputManager input, int width, int height){
		
		beginningTime = System.nanoTime();
		
		this.width = width;
		this.height = height;
		this.main = main;
		
		this.input = input;
		
		levelS.loadLevelSequence();
		
		
		

		if(section.equals("room")){
			
			
			//ROOM
			
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/PlayerRoom.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/PlayerBedAnimation.png", "playerBed");
			shipScreen.loadSpriteSheet("/PlayerBedroomIcons.png", "shipIcons");
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/TextBox.png", "textBox");
			shipScreen.loadSpriteSheet("/BlueShipDoor.png", "blueDoor");
			currentRoom = section;
			
			decorations.add(new Bed(this, 1148, 652, 0, 0, 3, 2, shipData.playerRoom));
			decorations.add(new Door(this, 1, 480, 524, 0, 0, 4, 4, true, 0, Door.BLUE));
			decorations.add(new DecorationShip(this, 784, 652, 0, 2, 3, 2));
			decorations.add(new DecorationShip(this, 1404, 716, 0, 4, 1, 1));
			decorations.add(new Intercom(this, 926, 304, 1, 4, 1, 1, shipData.playerRoom));
			
			
			
			
			
		}else if(section.equals("dorm")){
			
			
			//DORM
			
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/DormsSpace.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/BlueShipDoor.png", "blueDoor");
			shipScreen.loadSpriteSheet("/LargeGreenShipDoor.png", "green");
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/DormsIcons.png", "shipIcons");
			currentRoom = section;
			
			
			decorations.add(new Elevator(this, 732, 504, 0, 0, 7, 1));
			
			decorations.add(new Door(this, 1, 24, 268, 0, 0, 4, 4, true, 3, Door.BLUE));
			decorations.add(new Door(this, 2, 252, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 3, 480, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 4, 1180, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 5, 1408, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 6, 1636, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 7, 24, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 8, 252, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 9, 480, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			
			decorations.add(new LargeShipDoor(this, 1, 1532, 696, 0, 0, 6, 5, true, 0, LargeShipDoor.GREEN));
			
			player = new Player(0, 0, 0, 1, 1, "player", input, null, 122, 460);
			player.currentShipRoom = currentRoom;
			for(int i = 0; i < decorations.size(); i++){
				if(decorations.get(i) instanceof Elevator){
					player.setElevator((Elevator) decorations.get(i));
				}
			}
			
			
			
		}else if(section.equals("corridor")){
			
			
			//CORRIDOR
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/CorridorWindow.png", true);
			currentBackground.scaleImage(4, width * 2, height);
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/LargeYellowShipDoor.png", "yellow");
			shipScreen.loadSpriteSheet("/LargeGreenShipDoor.png", "green");
			shipScreen.loadSpriteSheet("/CorridorIcons.png", "shipIcons");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/BlueShipDoor.png", "blueDoor");
			currentRoom = section;
			
			decorations.add(new Sign(this, 360, 920, 8, 0, 1, 1, false, false, "Planetary Explorers"));
			decorations.add(new Sign(this, 992, 920, 8, 1, 1, 1, false, false, "Forge"));
			decorations.add(new Sign(this, 1544, 920, 8, 2, 1, 1, true, false, "Maintenance"));
			decorations.add(new Sign(this, 2160, 920, 8, 3, 1, 1, true, true, "Fluid Control"));
			decorations.add(new Sign(this, 2716, 920, 8, 0, 1, 1, false, true, "Control Room"));
			decorations.add(new Sign(this, 3416, 920, 8, 1, 1, 1, false, true, "Kings Chambers")); // add apostrophe
			
			decorations.add(new LargeShipDoor(this, 1, 44, 696, 0, 0, 6, 5, true, 3, LargeShipDoor.GREEN));
			decorations.add(new LargeShipDoor(this, 2, 744, 760, 0, 0, 4, 4, false, 0, LargeShipDoor.ORANGE));
			decorations.add(new LargeShipDoor(this, 2, 2776, 760, 4, 0, 4, 4, false, 0, LargeShipDoor.PURPLE));
			decorations.add(new LargeShipDoor(this, 4, 3408, 696, 0, 0, 6, 5, true, 0, LargeShipDoor.YELLOW));
			
			decorations.add(new Door(this, 1, 1332, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 2, 2176, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			

			
			player = new Player(0, 0, 0, 1, 1,"player", input, null, 204, 952);
			player.currentShipRoom = currentRoom;
			
			
			
			
		}else if(section.equals("kingsChambers")){
			
			
			//KINGS CHAMBERS
			
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/KingsDorms.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/BlackShipDoor.png", "blackDoor");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/TextBox.png", "textBox");
			shipScreen.loadSpriteSheet("/KingsDormIcons.png", "shipIcons");
			currentRoom = section;
			
			decorations.add(new Sign(this, 1272, 920, 30, 12, 1, 1, true, false, "Pod Room"));
			
			decorations.add(new Door(this, 1, 1060, 760, 0, 0, 4, 4, true, 0, Door.BLACK));
			decorations.add(new King(this, 1224, 376, 0, 0, 10, 10, shipData.kingsChamber));
			decorations.add(new DecorationShip(this, 1848, 920, 30, 10, 2, 2));

			if(shipData.kingsChamber == 0 || shipData.kingsChamber == 2) {
				decorations.add(new DecorationShip(this, 0, 940, 30, 13, 5, 2));
			}else {
				decorations.add(new DecorationShip(this, 0, 940, 30, 15, 5, 2));
			}
			
			player = new Player(0, 0, 0, 1, 1, "player", input, null, 296, 952);
			//444
			player.currentShipRoom = currentRoom;
			
			
			
		}else if(section.equals("podRoom")){
			
			//POD ROOM
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/PodRoom.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/BlackShipDoor.png", "blackDoor");
			shipScreen.loadSpriteSheet("/LightgreenShipDoor.png", "lightgreenDoor");
			shipScreen.loadSpriteSheet("/DarkgreenShipDoor.png", "darkgreenDoor");
			shipScreen.loadSpriteSheet("/WhiteShipDoor.png", "whiteDoor");
			shipScreen.loadSpriteSheet("/YellowShipDoor.png", "yellowDoor");
			shipScreen.loadSpriteSheet("/GreyShipDoor.png", "greyDoor");
			shipScreen.loadSpriteSheet("/PodRoomIcons.png", "shipIcons");
			currentRoom = section;
			
			
			decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 3, Door.BLACK));
			
			if(levelS.openWorlds[0]){
				decorations.add(new Door(this, 2, 320, 760, 0, 0, 4, 4, true, 0, Door.LIGHTGREEN));
				decorations.add(new DecorationShip(this, 368, 704, 0, 0, 3, 2));
			}else{
				decorations.add(new Door(this, 2, 320, 760, 0, 0, 4, 4, false, 0, Door.LIGHTGREEN));
				decorations.add(new DecorationShip(this, 368, 704, 3, 2, 3, 2));
			}
			
			if(levelS.openWorlds[1]){
				decorations.add(new Door(this, 3, 576, 760, 0, 0, 4, 4, true, 0, Door.DARKGREEN));
				decorations.add(new DecorationShip(this, 624, 704, 0, 2, 3, 2));
			}else{
				decorations.add(new Door(this, 3, 576, 760, 0, 0, 4, 4, false, 0, Door.DARKGREEN));
				decorations.add(new DecorationShip(this, 624, 704, 3, 4, 3, 2));
			}

			if(levelS.openWorlds[2]){
				decorations.add(new Door(this, 4, 832, 760, 0, 0, 4, 4, true, 0, Door.WHITE));
				decorations.add(new DecorationShip(this, 880, 704, 0, 4, 3, 2));
			}else{
				decorations.add(new Door(this, 4, 832, 760, 0, 0, 4, 4, false, 0, Door.WHITE));
				decorations.add(new DecorationShip(this, 880, 704, 3, 6, 3, 2));
			}

			if(levelS.openWorlds[3]){
				decorations.add(new Door(this, 5, 1088, 760, 0, 0, 4, 4, true, 0, Door.YELLOW));
				decorations.add(new DecorationShip(this, 1136, 704, 0, 6, 3, 2));
			}else{
				decorations.add(new Door(this, 5, 1088, 760, 0, 0, 4, 4, false, 0, Door.YELLOW));
				decorations.add(new DecorationShip(this, 1136, 704, 6, 0, 3, 2));
			}

			if(levelS.openWorlds[4]){
				decorations.add(new Door(this, 6, 1344, 760, 0, 0, 4, 4, true, 0, Door.GREY));
				decorations.add(new DecorationShip(this, 1392, 704, 3, 0, 3, 2));
			}else{
				decorations.add(new Door(this, 6, 1344, 760, 0, 0, 4, 4, false, 0, Door.GREY));
				decorations.add(new DecorationShip(this, 1392, 704, 6, 2, 3, 2));
			}
			
			
			//decorations.add(new Door(this, 7, 1632, 760, 0, 0, 4, 4, true, 0, Door.BLACK));
			
			player = new Player(0, 0, 0, 1, 1, "player", input, null, 128, 952);
			player.currentShipRoom = currentRoom;
			
		}else if(section.equals("lightGreenLaunch")){
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/LaunchRoomWindow.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/LightgreenShipDoor.png", "lightgreenDoor");
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/LaunchRoomIcons.png", "shipIcons");
			
			currentRoom = section;
			
			decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 0, Door.LIGHTGREEN));
			decorations.add(new Monitor(this, levelS, 1, loadLevel, 436, 468, 0, 0, 5, 6));

			for(int i = 0; i < decorations.size(); i++){
				if(decorations.get(i) instanceof Monitor){
					decorations.add(new Lever(this, (Monitor) decorations.get(i), 0, 508, 856, 5, 4, 1, 2));
					decorations.add(new Lever(this, (Monitor) decorations.get(i), 1, 628, 856, 5, 6, 1, 2));
				}
			}
			for(int i = 0; i < decorations.size(); i++){
				if(decorations.get(i) instanceof Monitor){
					decorations.add(new Ship(this, (Monitor) decorations.get(i), 1348, 876, 0, 8, 3, 3));
				}
			}
			player = new Player(0, 0, 0, 1, 1, "player", input, null, 1392, 952);
			player.currentShipRoom = "launchRoom";
			
		}else if(section.equals("darkGreenLaunch")){
			
			shipScreen = new Screen(width, height, 0, 0);
			currentBackground = new Background("/LaunchRoomWindow.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/DarkgreenShipDoor.png", "darkgreenDoor");
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/LaunchRoomIcons.png", "shipIcons");
			
			currentRoom = section;
			
			decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 0, Door.DARKGREEN));
			decorations.add(new Monitor(this, levelS, 2, loadLevel, 436, 468, 0, 0, 5, 6));

			for(int i = 0; i < decorations.size(); i++){
				if(decorations.get(i) instanceof Monitor){
					decorations.add(new Lever(this, (Monitor) decorations.get(i), 0, 508, 856, 5, 4, 1, 2));
					decorations.add(new Lever(this, (Monitor) decorations.get(i), 1, 628, 856, 5, 6, 1, 2));
				}
			}
			for(int i = 0; i < decorations.size(); i++){
				if(decorations.get(i) instanceof Monitor){
					decorations.add(new Ship(this, (Monitor) decorations.get(i), 1348, 876, 0, 8, 3, 3));
				}
			}
			player = new Player(0, 0, 0, 1, 1, "player", input, null, 1392, 952);
			player.currentShipRoom = "launchRoom";
			
		}
		
		main.setPlayer(player);
		

		
	}
	
	public void triggerStageSwitch(String newStage, int playerLocation){
		

		if(newStage.equals("room")){
			
			//NEW LOAD ROOM
			
			decorations.clear();
			shipScreen.removeSpriteSheets();
			shipScreen.screenX = 0;
			
			currentBackground = new Background("/PlayerRoom.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/PlayerBedAnimation.png", "playerBed");
			shipScreen.loadSpriteSheet("/PlayerBedroomIcons.png", "shipIcons");
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/BlueShipDoor.png", "blueDoor");
			currentRoom = newStage;
			
			if(playerLocation == 0) {
				decorations.add(new DecorationShip(this, 1148, 652, 0, 0, 3, 2));
			}else {
				decorations.add(new Bed(this, 1148, 652, 0, 0, 3, 2, shipData.playerRoom));
			}
			if(playerLocation == 0) {
				decorations.add(new Door(this, 1, 480, 524, 0, 0, 4, 4, true, 3, Door.BLUE));
			}else {
				decorations.add(new Door(this, 1, 480, 524, 0, 0, 4, 4, true, 0, Door.BLUE));
			}
			decorations.add(new DecorationShip(this, 784, 652, 0, 2, 3, 2));
			decorations.add(new DecorationShip(this, 1404, 716, 0, 4, 1, 1));
			decorations.add(new Intercom(this, 926, 304, 1, 4, 1, 1, shipData.playerRoom));
			
			if(playerLocation == 0) {
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 580, 716);
				player.currentShipRoom = currentRoom;
			}else {
				player = null;
			}
			
			
			
			
			
		}else if(newStage.equals("dorm")){
			
			//NEW LOAD DORM
			
			decorations.clear();
			shipScreen.removeSpriteSheets();
			shipScreen.screenX = 0;
			
			currentBackground = new Background("/DormsSpace.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/BlueShipDoor.png", "blueDoor");
			shipScreen.loadSpriteSheet("/LargeGreenShipDoor.png", "green");
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/DormsIcons.png", "shipIcons");
			currentRoom = newStage;
			
			
			decorations.add(new Elevator(this, 732, 504, 0, 0, 7, 1));
			
			if(playerLocation == 1){
				decorations.add(new Door(this, 1, 24, 268, 0, 0, 4, 4, true, 3, Door.BLUE));
			}else if(playerLocation == 2){
				decorations.add(new Door(this, 1, 24, 268, 0, 0, 4, 4, true, 0, Door.BLUE));
			}
			decorations.add(new Door(this, 2, 252, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 3, 480, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 4, 1180, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 5, 1408, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 6, 1636, 268, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 7, 24, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 8, 252, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 9, 480, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			
			if(playerLocation == 1){
				decorations.add(new LargeShipDoor(this, 1, 1532, 696, 0, 0, 6, 5, true, 0, LargeShipDoor.GREEN));
			}else if(playerLocation == 2){
				decorations.add(new LargeShipDoor(this, 1, 1532, 696, 0, 0, 6, 5, true, 3, LargeShipDoor.GREEN));
			}
			if(playerLocation == 1){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 122, 460);
			}else if(playerLocation == 2){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 1696, 952);
				player.facing = true;
			}
			player.currentShipRoom = currentRoom;
			for(int i = 0; i < decorations.size(); i++){
				if(decorations.get(i) instanceof Elevator){
					player.setElevator((Elevator) decorations.get(i));
				}
			}
			
			
			
			
			
		}else if(newStage.equals("corridor")){
			
			//NEW LOAD CORRIDOR
			
			
			decorations.clear();
			shipScreen.removeSpriteSheets();
			
			currentBackground = new Background("/CorridorWindow.png", true);
			currentBackground.scaleImage(4, width * 2, height);
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/LargeYellowShipDoor.png", "yellow");
			shipScreen.loadSpriteSheet("/LargeGreenShipDoor.png", "green");
			shipScreen.loadSpriteSheet("/CorridorIcons.png", "shipIcons");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/BlueShipDoor.png", "blueDoor");
			currentRoom = newStage;
			
			decorations.add(new Sign(this, 360, 920, 8, 0, 1, 1, false, false, "Planetary Explorers"));
			decorations.add(new Sign(this, 992, 920, 8, 1, 1, 1, false, false, "Forge"));
			decorations.add(new Sign(this, 1544, 920, 8, 2, 1, 1, true, false, "Maintenance"));
			decorations.add(new Sign(this, 2160, 920, 8, 3, 1, 1, true, true, "Fluid Control"));
			decorations.add(new Sign(this, 2716, 920, 8, 0, 1, 1, false, true, "Control Room"));
			decorations.add(new Sign(this, 3416, 920, 8, 1, 1, 1, false, true, "Kings Chambers")); // add apostrophe
			
			
			if(playerLocation == 1){
				decorations.add(new LargeShipDoor(this, 1, 44, 696, 0, 0, 6, 5, true, 3, LargeShipDoor.GREEN));
			}else if(playerLocation == 2){
				decorations.add(new LargeShipDoor(this, 1, 44, 696, 0, 0, 6, 5, true, 0, LargeShipDoor.GREEN));
			}
			decorations.add(new LargeShipDoor(this, 2, 744, 760, 0, 0, 4, 4, false, 0, LargeShipDoor.ORANGE));
			decorations.add(new LargeShipDoor(this, 2, 2776, 760, 4, 0, 4, 4, false, 0, LargeShipDoor.PURPLE));
			if(playerLocation == 1){
				decorations.add(new LargeShipDoor(this, 4, 3408, 696, 0, 0, 6, 5, true, 0, LargeShipDoor.YELLOW));
			}else if(playerLocation == 2){
				decorations.add(new LargeShipDoor(this, 4, 3408, 696, 0, 0, 6, 5, true, 3, LargeShipDoor.YELLOW));
			}
			
			decorations.add(new Door(this, 1, 1332, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			decorations.add(new Door(this, 2, 2176, 760, 0, 0, 4, 4, false, 0, Door.BLUE));
			

			if(playerLocation == 1){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 204, 952);
			}else if(playerLocation == 2){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 3568, 952);
				player.facing = true;
			}
			player.currentShipRoom = currentRoom;

			
			
			
		}else if(newStage.equals("kingsChambers")){
			
			//NEW LOAD KINGS CHAMBERS
			
			decorations.clear();
			shipScreen.removeSpriteSheets();
			shipScreen.screenX = 0;
			
			//Change this to just setting the player rather than setting the player to null and have the other class take care of it
			currentBackground = new Background("/KingsDorms.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/BlackShipDoor.png", "blackDoor");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/TextBox.png", "textBox");
			shipScreen.loadSpriteSheet("/KingsDormIcons.png", "shipIcons");
			currentRoom = newStage;
			
			decorations.add(new Sign(this, 1272, 920, 30, 12, 1, 1, true, false, "Pod Room"));
			
			if(playerLocation == 1) {
				decorations.add(new Door(this, 1, 1060, 760, 0, 0, 4, 4, true, 0, Door.BLACK));
			}else if(playerLocation == 2){
				decorations.add(new Door(this, 1, 1060, 760, 0, 0, 4, 4, true, 3, Door.BLACK));
			}
			decorations.add(new King(this, 1224, 376, 0, 0, 10, 10, shipData.kingsChamber));
			decorations.add(new DecorationShip(this, 1848, 920, 30, 10, 2, 2));
			
			if(shipData.kingsChamber == 0 || shipData.kingsChamber == 2) {
				decorations.add(new DecorationShip(this, 0, 940, 30, 13, 5, 2));
			}else {
				decorations.add(new DecorationShip(this, 0, 940, 30, 15, 5, 2));
			}
			
			if(playerLocation == 1){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 296, 952);
				//444
			}else if(playerLocation == 2) {
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 1160, 952);
				player.facing = true;
			}
			player.currentShipRoom = currentRoom;
			
		}else if(newStage.equals("podRoom")){
			
			//NEW LOAD POD ROOM
			decorations.clear();
			shipScreen.removeSpriteSheets();
			
			currentBackground = new Background("/PodRoom.png", true);
			currentBackground.scaleImage(4, width, height);
			shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
			shipScreen.loadSpriteSheet("/font.png", "font");
			shipScreen.loadSpriteSheet("/BlackShipDoor.png", "blackDoor");
			shipScreen.loadSpriteSheet("/LightgreenShipDoor.png", "lightgreenDoor");
			shipScreen.loadSpriteSheet("/DarkgreenShipDoor.png", "darkgreenDoor");
			shipScreen.loadSpriteSheet("/WhiteShipDoor.png", "whiteDoor");
			shipScreen.loadSpriteSheet("/YellowShipDoor.png", "yellowDoor");
			shipScreen.loadSpriteSheet("/GreyShipDoor.png", "greyDoor");
			shipScreen.loadSpriteSheet("/PodRoomIcons.png", "shipIcons");
			currentRoom = newStage;
			
			if(playerLocation == 1){
				decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 3, Door.BLACK));
			}else{
				decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 0, Door.BLACK));
			}
			
			//This checks to see if the current world is unlocked and checks the player location to appropriatly add the doors
			
			if(levelS.openWorlds[0]){
				if(playerLocation == 2){
					decorations.add(new Door(this, 2, 320, 760, 0, 0, 4, 4, true, 3, Door.LIGHTGREEN));
				}else{
					decorations.add(new Door(this, 2, 320, 760, 0, 0, 4, 4, true, 0, Door.LIGHTGREEN));
				}
				decorations.add(new DecorationShip(this, 368, 704, 0, 0, 3, 2));
			}else{
				decorations.add(new Door(this, 2, 320, 760, 0, 0, 4, 4, false, 0, Door.LIGHTGREEN));
				decorations.add(new DecorationShip(this, 368, 704, 3, 2, 3, 2));
			}
			
			if(levelS.openWorlds[1]){
				if(playerLocation == 3){
					decorations.add(new Door(this, 3, 576, 760, 0, 0, 4, 4, true, 3, Door.DARKGREEN));
				}else{
					decorations.add(new Door(this, 3, 576, 760, 0, 0, 4, 4, true, 0, Door.DARKGREEN));
				}
				decorations.add(new DecorationShip(this, 624, 704, 0, 2, 3, 2));
			}else{
				decorations.add(new Door(this, 3, 576, 760, 0, 0, 4, 4, false, 0, Door.DARKGREEN));
				decorations.add(new DecorationShip(this, 624, 704, 3, 4, 3, 2));
			}

			if(levelS.openWorlds[2]){
				if(playerLocation == 4){
					decorations.add(new Door(this, 4, 832, 760, 0, 0, 4, 4, true, 3, Door.WHITE));
				}else{
					decorations.add(new Door(this, 4, 832, 760, 0, 0, 4, 4, true, 0, Door.WHITE));
				}
				decorations.add(new DecorationShip(this, 880, 704, 0, 4, 3, 2));
			}else{
				decorations.add(new Door(this, 4, 832, 760, 0, 0, 4, 4, false, 0, Door.WHITE));
				decorations.add(new DecorationShip(this, 880, 704, 3, 6, 3, 2));
			}

			if(levelS.openWorlds[3]){
				if(playerLocation == 5){
					decorations.add(new Door(this, 5, 1088, 760, 0, 0, 4, 4, true, 3, Door.YELLOW));
				}else{
					decorations.add(new Door(this, 5, 1088, 760, 0, 0, 4, 4, true, 0, Door.YELLOW));
				}
				decorations.add(new DecorationShip(this, 1136, 704, 0, 6, 3, 2));
			}else{
				decorations.add(new Door(this, 5, 1088, 760, 0, 0, 4, 4, false, 0, Door.YELLOW));
				decorations.add(new DecorationShip(this, 1136, 704, 6, 0, 3, 2));
			}

			if(levelS.openWorlds[4]){
				if(playerLocation == 6){
					decorations.add(new Door(this, 6, 1344, 760, 0, 0, 4, 4, true, 3, Door.GREY));
				}else{
					decorations.add(new Door(this, 6, 1344, 760, 0, 0, 4, 4, true, 0, Door.GREY));
				}
				decorations.add(new DecorationShip(this, 1392, 704, 3, 0, 3, 2));
			}else{
				decorations.add(new Door(this, 6, 1344, 760, 0, 0, 4, 4, false, 0, Door.GREY));
				decorations.add(new DecorationShip(this, 1392, 704, 6, 2, 3, 2));
			}

			//This determines the player location based off of the door they exit
			
			if(playerLocation == 1){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 128, 952);
				player.currentShipRoom = currentRoom;
			}else if(playerLocation == 2){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 416, 952);
				player.currentShipRoom = currentRoom;
			}else if(playerLocation == 3){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 672, 952);
				player.currentShipRoom = currentRoom;
			}else if(playerLocation == 4){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 928, 952);
				player.currentShipRoom = currentRoom;
			}else if(playerLocation == 5){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 1184, 952);
				player.currentShipRoom = currentRoom;
			}else if(playerLocation == 6){
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 1440, 952);
				player.currentShipRoom = currentRoom;
			}
			
		}else if(newStage.equals("lightGreenLaunch")){
				
				decorations.clear();
				shipScreen.removeSpriteSheets();
				
				currentBackground = new Background("/LaunchRoomWindow.png", true);
				currentBackground.scaleImage(4, width, height);
				shipScreen.loadSpriteSheet("/LightgreenShipDoor.png", "lightgreenDoor");
				shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
				shipScreen.loadSpriteSheet("/font.png", "font");
				shipScreen.loadSpriteSheet("/LaunchRoomIcons.png", "shipIcons");
				currentRoom = newStage;
				
				decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 3, Door.LIGHTGREEN));
				decorations.add(new Monitor(this, levelS, 1, 1, 436, 468, 0, 0, 5, 6));

				for(int i = 0; i < decorations.size(); i++){
					if(decorations.get(i) instanceof Monitor){
						decorations.add(new Lever(this, (Monitor) decorations.get(i), 0, 508, 856, 5, 4, 1, 2));
						decorations.add(new Lever(this, (Monitor) decorations.get(i), 1, 628, 856, 5, 6, 1, 2));
					}
				}
				for(int i = 0; i < decorations.size(); i++){
					if(decorations.get(i) instanceof Monitor){
						decorations.add(new Ship(this, (Monitor) decorations.get(i), 1348, 876, 0, 8, 3, 3));
					}
				}
				
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 128, 952);
				player.currentShipRoom = "launchRoom";
				
			}else if(newStage.equals("darkGreenLaunch")){
				
				decorations.clear();
				shipScreen.removeSpriteSheets();
				
				currentBackground = new Background("/LaunchRoomWindow.png", true);
				currentBackground.scaleImage(4, width, height);
				shipScreen.loadSpriteSheet("/DarkgreenShipDoor.png", "darkgreenDoor");
				shipScreen.loadSpriteSheet("/PlayerSprites.png", "player");
				shipScreen.loadSpriteSheet("/font.png", "font");
				shipScreen.loadSpriteSheet("/LaunchRoomIcons.png", "shipIcons");
				currentRoom = newStage;
				
				decorations.add(new Door(this, 1, 32, 760, 0, 0, 4, 4, true, 3, Door.DARKGREEN));
				decorations.add(new Monitor(this, levelS, 2, 1, 436, 468, 0, 0, 5, 6));

				for(int i = 0; i < decorations.size(); i++){
					if(decorations.get(i) instanceof Monitor){
						decorations.add(new Lever(this, (Monitor) decorations.get(i), 0, 508, 856, 5, 4, 1, 2));
						decorations.add(new Lever(this, (Monitor) decorations.get(i), 1, 628, 856, 5, 6, 1, 2));
					}
				}
				for(int i = 0; i < decorations.size(); i++){
					if(decorations.get(i) instanceof Monitor){
						decorations.add(new Ship(this, (Monitor) decorations.get(i), 1348, 876, 0, 8, 3, 3));
					}
				}
				
				player = new Player(0, 0, 0, 1, 1, "player", input, null, 128, 952);
				player.currentShipRoom = "launchRoom";
				
			}
		
		main.setPlayer(player);
		
		
			
	}
	
	public void lockDoors() {
		for(int i = 0; i < decorations.size(); i++) {
			if(decorations.get(i) instanceof Door) {
				((Door) decorations.get(i)).lockDoor = true;
			}
		}
	}
	
	public void unlockDoors() {
		for(int i = 0; i < decorations.size(); i++) {
			if(decorations.get(i) instanceof Door) {
				((Door) decorations.get(i)).lockDoor = false;
			}
		}
	}
	
	public void setPlayerMain(){
		main.setPlayer(player);
	}
	
	public void loadLevel(String loadLevel, int levelNumber, int worldNumber){
		decorations.clear();
		shipScreen.removeSpriteSheets();
		System.out.println("Level Load");
		main.loadLevel(loadLevel, levelNumber, worldNumber);
	}
	
	public boolean delete = false;
	
	public void tick(InputManager input){
		for(int i = 0; i < decorations.size(); i++){
			decorations.get(i).tick();
			if(delete) {
				i--;
				delete = false;
			}
		}
		
		if(currentRoom.equals("kingsChambers")){
			if(player.x + 63 < 0){
				for(int i = 0; i < decorations.size(); i++) {
					if(decorations.get(i) instanceof King) {
						if(((King) decorations.get(i)).state == 0 && ((King) decorations.get(i)).tickCounter < 2280) {
							shipData.save("kingRoom: ", 2);
						}
					}
				}
				triggerStageSwitch("corridor", 2);
			}
		}

	}
	
	public void render(Screen screen, float interpolation){
		
		
		if(!currentRoom.equals(new String("corridor"))){
			for(int i = 0; i < screen.pixels.length; i++){
				screen.pixels[i] = currentBackground.pixels[i];
			}
		}else{
			for(int y = 0; y < height; y++){
				for(int x = 0; x < width; x++){
					screen.pixels[x + y * width] = currentBackground.pixels[x + shipScreen.screenX + y * currentBackground.scaledWidth];
				}
			}
		}

		for(int i = 0; i < decorations.size(); i++){
			decorations.get(i).render(screen, interpolation);
		}
		
		if(once){
			once = false;
			System.out.println("First Render Time(ms): " + ((System.nanoTime() - beginningTime) / 1000000));
		}
	}
	
	public void removeDecoration(DecorationShip decoration) {
		for(int i = 0; i < decorations.size(); i++) {
			if(decorations.get(i).equals(decoration)) {
				decorations.remove(i);
			}
		}
	}
	
	public void addDecoration(DecorationShip decoration) {
		decorations.add(decoration);
	}
	
	public void updateBed() {
		for(int i = 0; i < decorations.size(); i++) {
			if(decorations.get(i) instanceof Bed) {
				((Bed) decorations.get(i)).startAnimation = true;
			}
		}
	}
	
	public Screen getScreen(){
		return shipScreen;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	
}
