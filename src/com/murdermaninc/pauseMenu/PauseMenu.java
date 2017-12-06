package com.murdermaninc.pauseMenu;

import com.murdermaninc.graphics.Background;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.main.InputManager;
import com.murdermaninc.main.Main;

public class PauseMenu {

	private Background semiTransparentBackground;
	
	private Screen screenPause;
	
	private InputManager input;
	private Main main;
	private int width;
	
	private Font font = new Font();
	
	public int currentButton = 1;
	
	int buttonHeight = 96;
	int buttonWidth = 416;
	int spacing = 80;
	
	int pauseScale = 9;
	int topOffset = (1080 / 2) - ((((16 * pauseScale) + (4 * pauseScale)) + (4 * (spacing + buttonHeight))) / 2);
	//There is no class for button as it is easier to just store everything in one class
	private int[] buttonDataGreen;
	private int[] buttonDataRed;
	
	public PauseMenu(int width, int height, InputManager input, Main main){
		
		this.input = input;
		this.width = width;
		this.main = main;
		
		semiTransparentBackground = new Background("/CollectionBackground.png", true);
		semiTransparentBackground.scaleImage(4, width, height);
		
		screenPause = new Screen(width, height, 0, 0, 1);
		screenPause.loadSpriteSheet("/font.png", "font");
		screenPause.loadSpriteSheet("/MenuButton.png", "button");
		
		buttonDataGreen = screenPause.loadData(0, 0, 7, 2, 4, "button");
		buttonDataRed = screenPause.loadData(0, 2, 7, 2, 4, "button");
		
		for(int i = 0; i < semiTransparentBackground.pixels.length; i++){
			screenPause.pixels[i] = semiTransparentBackground.pixels[i];
		}
		
		font.drawText(screenPause, "PAUSED!", (width / 2) - (font.getTextLength("PAUSED!", pauseScale) / 2), topOffset, pauseScale);
	}
	
	public void tick(){
		
		if(input.s){
			currentButton++;
			if(currentButton > 4){
				currentButton = 1;
			}
			input.s = false;
		}
		
		if(input.w){
			currentButton--;
			if(currentButton < 1){
				currentButton = 4;
			}
			input.w = false;
		}
		
		if(input.enter || input.e){
			
			if(currentButton == 1){
				main.pauseMenu = false;
			}else if(currentButton == 2){
				if(!main.mainShipWorld){
					main.mainShipWorld = true;
					int currentWorld = main.getCurrentWorldAndLevel()[0];
					if(currentWorld == 1){
						main.loadShip = "lightGreenLaunch";
					}else if(currentWorld == 2){
						main.loadShip = "darkGreenLaunch";
					}else if(currentWorld == 3){
						main.loadShip = "snowLaunch";
					}
				}else{
					main.mainShipManager.triggerStageSwitch("room", 1);
				}
				main.pauseMenu = false;
			}else if(currentButton == 3){
				
			}else if(currentButton == 4){
				if(!main.mainShipWorld) {
					int currentWorld = main.getCurrentWorldAndLevel()[0];
					if(currentWorld == 1){
						main.startData.save("lightGreenLaunch");
					}else if(currentWorld == 2){
						main.startData.save("darkGreenLaunch");
					}else if(currentWorld == 3){
						main.startData.save("snowLaunch");
					}
				}else {
					String currentShipWorld = main.mainShipManager.currentRoom;
					
					if(currentShipWorld.equals("lightGreenLaunch") || currentShipWorld.equals("darkGreenLaunch")) {
						if(currentShipWorld.equals("lightGreenLaunch")) main.startData.save("lightGreenLaunch");
						if(currentShipWorld.equals("darkGreenLaunch")) main.startData.save("darkGreenLaunch");
					}else {
						main.startData.save("room");
					}
				}
				
				System.exit(0);
			}
			
			input.enter = false;
			input.e = false;
		}
		
	}
	
	public void render(){
		//load buttons
		
		int yOffset = topOffset + (16 * pauseScale);

		

		
		if(currentButton == 1){
			screenPause.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}else{
			screenPause.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}
		
		yOffset = yOffset + spacing + buttonHeight;
		
		if(currentButton == 2){
			screenPause.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}else{
			screenPause.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}
		
		yOffset = yOffset + spacing + buttonHeight;
		
		if(currentButton == 3){
			screenPause.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}else{
			screenPause.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}
		
		yOffset = yOffset + spacing + buttonHeight;
		
		if(currentButton == 4){
			screenPause.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}else{
			screenPause.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2), yOffset + spacing, 7, 2, 4);
		}
		
		int fontOffset = topOffset + (16 * 9);
		
		font.drawText(screenPause, "Resume", (width / 2) - (font.getTextLength("Resume", 3) / 2), fontOffset + spacing + ( (buttonHeight / 2) - (((16 * 3) + (4 * 3)) / 2)), 3);
		
		fontOffset = fontOffset + spacing + buttonHeight;
		
		
		//yOffset + (buttonHeight / 2) - ((((12 * 3 / 2) + (4 * 3)) + 10 + (((16 * 3) + (8 * 3)) / 2)) / 2)
		
		//fontOffset + (buttonHeight / 2) + (((16 * 3) + (4 * 3) + 10 + (16 * 2) + (8 * 2)) / 2)
		
		//fontOffset + (buttonHeight / 2) + (((((16 * 3) + (4 * 3)) / 2) + 10 + (((16 * 2) + (8 * 2)) / 2)) / 2)
		
		int messageSizeOffset = ((16 * 3) - (4 * 3) + 10 + (16 * 2) + (7 * 2)) / 2;
		
		font.drawText(screenPause, "return to the", (width / 2) - (font.getTextLength("return to the", 2) / 2),fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)),2);
		
		
		font.drawText(screenPause, "MotherShip", (width / 2) - (font.getTextLength("MotherShip", 3) / 2), fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)) + (16 * 2) - ((4 * 3) - 10), 3);
		
		fontOffset = fontOffset + spacing + buttonHeight;
		
		font.drawText(screenPause, "return to the", (width / 2) - (font.getTextLength("return to the", 2) / 2), fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)), 2);
		
		
		
		font.drawText(screenPause, "Main Menu", (width / 2) - (font.getTextLength("Main Menu", 3) / 2), fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)) + (16 * 2) - ((4 * 3) - 10), 3);
		
		fontOffset = fontOffset + spacing + buttonHeight;
		
		font.drawText(screenPause, "Exit Game", (width / 2) - (font.getTextLength("Exit Game", 3) / 2), fontOffset + spacing + ( (buttonHeight / 2) - (((16 * 3) + (4 * 3)) / 2)), 3);
		

		

	}
	
	
	public Screen getScreen(){
		return screenPause;
	}
	
}
