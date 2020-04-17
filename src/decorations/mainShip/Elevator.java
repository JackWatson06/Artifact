package com.murdermaninc.decorations.mainShip;

import com.murdermaninc.graphics.MainShipWorldManager;

public class Elevator extends DecorationShip{

	public int colR, colL, colT, colB;
	
	
	public Elevator(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		colR = x + 420;
		colL = x + 32;
		colT = y + 28;
		colB = y + 64;
	}
	
	
	public boolean switchDirections = false;
	@Override
	public void tick(){
		
		if(y < 988){
			if(switchDirections){
				y++;
			}
		}else{
			switchDirections = false;
		}
		
		if(y > 496){
			if(!switchDirections){
				y--;
			}
		}else{
			switchDirections = true;
		}
		
		colR = x + 420;
		colL = x + 32;
		colT = y + 28;
		colB = y + 64;
	}

}
