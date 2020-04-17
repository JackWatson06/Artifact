package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.text.TextBox;

public class King extends DecorationShip{

	private Animation animationBlink = new Animation();
	private Animation animationMove = new Animation();
	private ArrayList<int[]> blinkAnimation;
	private ArrayList<int[]> moveAnimation;
	
	public TextBox textBox;
	
	private boolean displayText = false;
	public int state = 0;
	
	public int tickCounter = 0;
	
	public King(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int state) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.state = state;
		
		if(state == 0 || state == 2) {
			manager.lockDoors();
		}
	}


	
	int timeLimit = 0;
	int switchCounter = 0;
	int textBoxNumber = 0;
	
	@Override
	public void tick() {
		
		if(state == 0) {
			if(tickCounter >= 240 && tickCounter <= 2280) {
				displayText = true;
			}else if(tickCounter > 2280) {
				displayText = false;

			}
			
		}
		
		if(displayText && state == 0) {
			
			boolean switchText = false;
			
			if(switchCounter >= timeLimit) {
				
				switchCounter = 0;
				textBoxNumber++;
				
				switchText = true;
			}
			
			// 240 + (6 * 60) + (7 * 60) + (8 * 60) + (7 * 60) + (6 * 60)
			
			if(switchText) {
				switch (textBoxNumber) {
				
				case 1: textBox = new TextBox("Thank you all for arriving with such haste.", x + 200, y + 100, 2, 40);
						timeLimit = 6 * 60;
				break;
				
				case 2: textBox = new TextBox("As you well know we just arrived at planet B-41C in the galaxy Noen.", x + 200, y + 100, 2, 40);
						timeLimit = 7 * 60;
				break;

				case 3: textBox = new TextBox("We have detected multiple signatures of life and now need to begin the extraction of the artifacts on the surface.", x + 200, y + 100, 2, 40);
						timeLimit = 8 * 60;
				break;

				case 4: textBox = new TextBox("The pods have been prepped and are ready for takeoff.", x + 200, y + 100, 2, 40);
						timeLimit = 7 * 60;
				break;
				
				case 5: textBox = new TextBox("Good luck and long live Solea.", x + 200, y + 100, 2, 40);
						timeLimit = 6 * 60;
				break;

				default: textBox = null;
						 manager.unlockDoors();
						 manager.shipData.save("kingRoom: ", 1);
				break;

				}
			}
				
			
			
			switchCounter++;
				
		}
		
		if(state == 2) {
			if(tickCounter >= 180 && tickCounter <= 2640) {
				displayText = true;
			}else if(tickCounter > 2640) {
				displayText = false;
			}
			
		}
		
		if(displayText && state == 2) {
			
			boolean switchText = false;
			
			if(switchCounter >= timeLimit) {
				
				switchCounter = 0;
				textBoxNumber++;
				
				switchText = true;
			}
			
			
			// 180 + (6 * 60) + (7 * 60) + (8 * 60) + (7 * 60) + (6 * 60) + (6 * 60)
			if(switchText) {
				switch (textBoxNumber) {
				
				case 1: textBox = new TextBox("Squish why did you leave when I was talking?", x + 200, y + 100, 2, 40);
						timeLimit = 6 * 60;
				break;
				
				case 2: textBox = new TextBox("Anyways thank you all for arriving with such haste.", x + 200, y + 100, 2, 40);
						timeLimit = 6 * 60;
				break;
				
				case 3: textBox = new TextBox("As you well know we just arrived at planet B-41C in the galaxy Noen.", x + 200, y + 100, 2, 40);
						timeLimit = 7 * 60;
				break;

				case 4: textBox = new TextBox("We have detected multiple signatures of life and now need to begin the extraction of the artifacts on the surface.", x + 200, y + 100, 2, 40);
						timeLimit = 8 * 60;
				break;

				case 5: textBox = new TextBox("The pods have been prepped and are ready for takeoff.", x + 200, y + 100, 2, 40);
						timeLimit = 7 * 60;
				break;
				
				case 6: textBox = new TextBox("Good luck and long live Solea.", x + 200, y + 100, 2, 40);
						timeLimit = 6 * 60;
				break;

				default: textBox = null;
						 manager.unlockDoors();
						 manager.shipData.save("kingRoom: ", 1);
				break;

				}
			}
				
			
			
			switchCounter++;
				
		}
		
		tickCounter++;
	}
	
	
	@Override
	public void render(Screen screen, float interpolation){
		
		if(blinkAnimation == null)	blinkAnimation = animationBlink.loadAnimationData(screen, "shipIcons", 4, 3, 0, 10, spriteWidth, spriteHeight);
		if(moveAnimation == null)  moveAnimation = animationBlink.loadAnimationData(screen, "shipIcons", 4, 3, 10, 0, spriteWidth, spriteHeight);
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		
		if(!animationMove.random){
			animationBlink.animateRandom(screen, blinkAnimation, true, 10, spriteWidth, spriteHeight, 0.0008F, x, y, 5, interpolation);
		}
		
		if(!animationBlink.random){
			animationMove.animateRandom(screen, moveAnimation, true, 10, spriteWidth, spriteHeight, 0.0004F, x, y, 3, interpolation);
		}
		
		if(!animationBlink.random && !animationMove.random){
			screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		}
		
		if(textBox != null && displayText) {
			textBox.render(screen);
		}
		
	}
}
