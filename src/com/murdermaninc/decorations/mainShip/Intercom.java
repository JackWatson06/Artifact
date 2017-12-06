package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.text.TextBox;

public class Intercom extends DecorationShip{

	private ArrayList<int[]> AnimationData;
	private Animation animation = new Animation();
	
	private int[] Data;
	
	public TextBox textBox;
	
	private boolean displayText = false;
	private boolean animate = false;
	private int state = 0;
	
	private int tickCounter = 0;
	
	
	
	
	public Intercom(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int state) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.state = state;
	}
	
	int timeLimit = 360;
	int switchCounter = 360;
	int textBoxNumber = 0;
	
	@Override
	public void tick() {
		
		if(state == 0) {
			if(tickCounter >= 300 && tickCounter < 1800) {
				animate = true;
				displayText = true;
			}else if(tickCounter == 1800) {
				animate = false;
				displayText = false;
			}else if(tickCounter == 1920) {
				manager.updateBed();
			}
			
		}
		
		if(displayText) {
				
			//textBox = new TextBox("Attention! We have just arrived at planet B-41CA", x + 34, y, 1);
			
			boolean switchText = false;
			
			if(switchCounter >= timeLimit) {
				
				switchCounter = 0;
				textBoxNumber++;
				
				switchText = true;
			}
			
			
			if(switchText) {
				switch (textBoxNumber) {
				
				case 1: textBox = new TextBox("Attention!", x + 34, y, 1, 50);
						timeLimit = 3 * 60;
				break;
				
				case 2: textBox = new TextBox("We have just arrived at planet B-41CA.", x + 34, y, 1, 50);
						timeLimit = 6 * 60;
				break;

				case 3: textBox = new TextBox("Members of the Planetary Explorers are requested to go to the Kings Chambers immediatly.", x + 34, y, 1, 50);
						timeLimit = 6 * 60;
				break;

				case 4: textBox = new TextBox("The King will be speaking shortly and all Planetary Explorers must attend!", x + 34, y, 1, 50);
						timeLimit = 6 * 60;
				break;

				default: textBox = null;
						 manager.shipData.save("playerRoom: ", 1);
				break;

				}
			}
				
			
			
			switchCounter++;
				
		}
		
		if(state == 1) {
			if(tickCounter == 120) {
				manager.updateBed();
			}
		}
		
		
		tickCounter++;
	}
	
	@Override
	public void render(Screen screen, float interpolation) {
		if(AnimationData == null) AnimationData = animation.loadAnimationData(screen, "shipIcons", 4, 3, xTile + 1, yTile, spriteWidth, spriteHeight); //plus one is because the animation is offset one from the original image
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
	
		if(animate) {
			animation.animateContinuous(screen, AnimationData, false, 3.0F, spriteWidth, spriteHeight, x, y, 3, 4, interpolation);
		}else {
			screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		}
		
		if(textBox != null && displayText) {
			textBox.render(screen);
		}
	}

}
