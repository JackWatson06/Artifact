package com.murdermaninc.pauseMenu;

import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.main.InputManager;
import com.murdermaninc.main.Log;
import com.murdermaninc.main.Main;

public class PauseMenu {

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
	
	private int tranpsarentColor;
	
	public PauseMenu(int width, int height, InputManager input, Main main){
		
		this.input = input;
		this.width = width;
		this.main = main;
		
		
		int alphaValue = 129;
		
		//This color is stored in twos complement as all other color values are
		this.tranpsarentColor = (~(alphaValue << 24) + 1);
		
		
		//This screen is only used to preload the button data so it is not necessary to load on class start.
		Screen screenPause = new Screen(width, height, 0, 0);
		screenPause.loadSpriteSheet("/MenuButton.png", "button");
		
		buttonDataGreen = screenPause.loadData(0, 0, 7, 2, 4, "button");
		buttonDataRed = screenPause.loadData(0, 2, 7, 2, 4, "button");

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
				Log.write("Switched To Ship");
				if(!main.mainShipWorld){
					main.mainShipWorld = true;
					int currentWorld = main.getCurrentWorldAndLevel()[0];
					int currentLevel = main.getCurrentWorldAndLevel()[1];
					if(currentWorld == 1){
						main.loadShip = "lightGreenLaunch";
						main.loadLevelNumber = currentLevel;
					}else if(currentWorld == 2){
						main.loadShip = "darkGreenLaunch";
						main.loadLevelNumber = currentLevel;
					}else if(currentWorld == 3){
						main.loadShip = "snowLaunch";
						main.loadLevelNumber = currentLevel;
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
	
	public boolean pauseButtonHit = true;
	
	public void render(Screen screen){
		
		if(pauseButtonHit) {
			
			int a1 = (tranpsarentColor >> 24) & 0xFF;
			int r1 = (tranpsarentColor >> 16) & 0xFF;
			int g1 = (tranpsarentColor >> 8) & 0xFF;
			int b1 = (tranpsarentColor) & 0xFF;
			
			
			float rat1 = (float) a1 / 255;
			float rat2 = 1 - ((float) a1 / 255);

			for(int i = 0; i < screen.pixels.length; i++) {
				
				int color = screen.pixels[i];
				
				int r2 = (color >> 16) & 0xFF;
				int g2 = (color >> 8) & 0xFF;
				int b2 = (color) & 0xFF;
				
				int r = Math.round(Math.min((r1 * rat1) + (r2 * rat2), 255));
				int g = Math.round(Math.min((g1 * rat1) + (g2 * rat2), 255));
				int b = Math.round(Math.min((b1 * rat1) + (b2 * rat2), 255));
				
				screen.pixels[i] = (-1 << 24) | (r << 16) | (g << 8) | b;
				
			}
			
			font.drawText(screen, "PAUSED!", (width / 2) - (font.getTextLength("PAUSED!", pauseScale) / 2) + screen.screenX, topOffset + screen.screenY, pauseScale);
			
			
			pauseButtonHit = false;
		}
		
		
		
		//load buttons
		
		int yOffset = topOffset + (16 * pauseScale);

		if(currentButton == 1){
			screen.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}else{
			screen.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}
		
		yOffset = yOffset + spacing + buttonHeight;
		
		if(currentButton == 2){
			screen.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}else{
			screen.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}
		
		yOffset = yOffset + spacing + buttonHeight;
		
		if(currentButton == 3){
			screen.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}else{
			screen.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}
		
		yOffset = yOffset + spacing + buttonHeight;
		
		if(currentButton == 4){
			screen.renderData(buttonDataGreen, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}else{
			screen.renderData(buttonDataRed, (width / 2) - (buttonWidth / 2) + screen.screenX, yOffset + spacing + screen.screenY, 7, 2, 4);
		}
		
		int fontOffset = topOffset + (16 * 9);
		
		font.drawText(screen, "Resume", (width / 2) - (font.getTextLength("Resume", 3) / 2) + screen.screenX, fontOffset + spacing + ( (buttonHeight / 2) - (((16 * 3) + (4 * 3)) / 2)) + screen.screenY, 3);
		
		fontOffset = fontOffset + spacing + buttonHeight;
		
		
		//yOffset + (buttonHeight / 2) - ((((12 * 3 / 2) + (4 * 3)) + 10 + (((16 * 3) + (8 * 3)) / 2)) / 2)
		
		//fontOffset + (buttonHeight / 2) + (((16 * 3) + (4 * 3) + 10 + (16 * 2) + (8 * 2)) / 2)
		
		//fontOffset + (buttonHeight / 2) + (((((16 * 3) + (4 * 3)) / 2) + 10 + (((16 * 2) + (8 * 2)) / 2)) / 2)
		
		int messageSizeOffset = ((16 * 3) - (4 * 3) + 10 + (16 * 2) + (7 * 2)) / 2;
		
		font.drawText(screen, "return to the", (width / 2) - (font.getTextLength("return to the", 2) / 2) + screen.screenX,fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)) + screen.screenY,2);
		
		
		font.drawText(screen, "MotherShip", (width / 2) - (font.getTextLength("MotherShip", 3) / 2) + screen.screenX, fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)) + (16 * 2) - ((4 * 3) - 10) + screen.screenY, 3);
		
		fontOffset = fontOffset + spacing + buttonHeight;
		
		font.drawText(screen, "return to the", (width / 2) - (font.getTextLength("return to the", 2) / 2) + screen.screenX, fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)) + screen.screenY, 2);
		
		
		
		font.drawText(screen, "Main Menu", (width / 2) - (font.getTextLength("Main Menu", 3) / 2) + screen.screenX, fontOffset + spacing + ((buttonHeight / 2) - (messageSizeOffset)) + (16 * 2) - ((4 * 3) - 10) + screen.screenY, 3);
		
		fontOffset = fontOffset + spacing + buttonHeight;
		
		font.drawText(screen, "Exit Game", (width / 2) - (font.getTextLength("Exit Game", 3) / 2) + screen.screenX, fontOffset + spacing + ( (buttonHeight / 2) - (((16 * 3) + (4 * 3)) / 2)) + screen.screenY, 3);
		

		

	}
	
	
}
