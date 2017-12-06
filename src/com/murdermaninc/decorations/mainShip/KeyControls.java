package com.murdermaninc.decorations.mainShip;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class KeyControls extends DecorationShip{

	
	private Font font = new Font();
	private Animation animation = new Animation();
	
	private int tickCounter = 0;
	private String controlName;
	
	//Spacing represents the pixels between the control elements
	private int spacing = 10;
	
	private int mainSpacing = 25;
	
	
	public KeyControls(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, String controlName) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.controlName = controlName;
		if(controlName.equals("Interact") || controlName.equals("Jump")) {
			//x Calculation represents the center line of the image and text placement
			this.x = (112 * 4) / 2;
			//y Calculation repressente the horizontal center line of the image 
			this.y = manager.height / 2;
		}else {
			
			this.x = (112 * 4) / 2 + (369 * 4);
			this.y = manager.height / 2;
			
		}
			
	}
	
	
	public void tick() {
		if(tickCounter >= 1200){
			manager.delete = true;
			manager.removeDecoration(this);
		}else {
			tickCounter++;
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation) {
		
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		
		if(controlName.equals("Interact")) {
			
			//screen.renderData(Data, x + (16 / 2) - 32, y - spacing - mainSpacing - (12 * 3) - (64 - 12), spriteWidth, spriteHeight, 4);
			animation.fadeAnimation(screen, Data, x + (16 / 2) - 32, y - spacing - mainSpacing - (12 * 3) - (64 - 12), spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
			
		}else if(controlName.equals("Jump")){

			animation.fadeAnimation(screen, Data, x + (40 / 2) - 128, y + mainSpacing, spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
		}else if(controlName.equals("Sprint")) {
			
			animation.fadeAnimation(screen, Data, x + (40 / 2) - 98, y + mainSpacing, spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
		}else if(controlName.equals("A")){
			
			animation.fadeAnimation(screen, Data, x + (16 / 2) - 32 - spacing - (64 - 16), y - spacing - mainSpacing - (12 * 3) - (64 - 12), spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
		}else if(controlName.equals("S")) {
			
			animation.fadeAnimation(screen, Data, x + (16 / 2) - 32, y - spacing - mainSpacing - (12 * 3) - (64 - 12), spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
		}else if(controlName.equals("D")) {
			
			animation.fadeAnimation(screen, Data, x + (16 / 2) - 32 + spacing + (64 - 16), y - spacing - mainSpacing - (12 * 3) - (64 - 12), spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
		}else{
			
			animation.fadeAnimation(screen, Data, x + (16 / 2) - 32, y - spacing - mainSpacing - (12 * 3) - (64 - 12) - spacing - (64 - 12), spriteWidth, spriteHeight, 4, 8.0F, true, 16.0F, 4.0F, interpolation);
			
		}
		
		if(controlName.equals("Interact")) {
			
			font.renderFade(screen, controlName, x - (font.getTextLength(controlName, 3) / 2), y - mainSpacing - (16 * 3), 3, 8.0F, true, 16.0F, 4.0F, interpolation);
		}else if(controlName.equals("Jump")) {
			
			font.renderFade(screen, controlName, x - (font.getTextLength(controlName, 3) / 2), y + spacing + mainSpacing + (64 - (3 * 4)) - (4 * 3), 3, 8.0F, true, 16.0F, 4.0F, interpolation);
		}else if(controlName.equals("Sprint")) {
			
			font.renderFade(screen, controlName,  x - (font.getTextLength(controlName, 3) / 2), y + spacing + mainSpacing + (64 - (3 * 4)) - (4 * 3), 3, 8.0F, true, 16.0F, 4.0F, interpolation);
		}else if(controlName.equals("S")){
			
			font.renderFade(screen, "Move", x - (font.getTextLength("Move", 3) / 2), y - mainSpacing - (16 * 3), 3, 8.0F, true, 16.0F, 4.0F, interpolation);
		}
	}

}
