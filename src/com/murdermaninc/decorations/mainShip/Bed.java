package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.entity.*;

public class Bed extends DecorationShip{

	private ArrayList<int[]> animationData;
	private int[] EmptyBedData;
	
	private Animation animation = new Animation();
	
	public boolean startAnimation = false;
	
	private int state = 0;
	
	public Bed(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int state) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.state = state;

	}
	
	@Override
	public void tick(){
		if(!animation.once && manager.player == null){
			manager.player = new Player(0, 0, 0, 1, 1, "Player", manager.input, null, 1148 + 76, 652 + 64);
			manager.player.currentShipRoom = manager.currentRoom;
			manager.player.facing = true;
			
			if(state == 0) {

				manager.addDecoration(new KeyControls(manager, 10 ,10, 3, 1, 1, 1, "Interact"));	//X and Y Values are calculated in the KeyControls File.
				manager.addDecoration(new KeyControls(manager, 10, 10, 3, 2, 4, 1, "Jump"));
				manager.addDecoration(new KeyControls(manager, 10, 10, 3, 3, 3, 1, "Sprint"));
				manager.addDecoration(new KeyControls(manager, 10, 10, 3, 0, 1, 1, "A"));
				manager.addDecoration(new KeyControls(manager, 10, 10, 5, 0, 1, 1, "S"));
				manager.addDecoration(new KeyControls(manager, 10, 10, 4, 0, 1, 1, "D"));
				manager.addDecoration(new KeyControls(manager, 10, 10, 6, 0, 1, 1, "W"));
			}
			manager.setPlayerMain();
		}
		
		//if(jumpOut){
			//startAnimation = true;
		//}
	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "playerBed", 4, 16, xTile, yTile, spriteWidth, spriteHeight, 4, 4);
		if(Data == null) Data = screen.loadData(0, 0, spriteWidth, spriteHeight, 4, "playerBed");
		if(EmptyBedData == null) EmptyBedData = screen.loadData(0, 0, 3, 2, 4, "shipIcons");
		
		
		if(animation.once && startAnimation){
			animation.animateOnce(screen, animationData, false, 7F, spriteWidth, spriteHeight, x, y, 16, 4, interpolation);
		}else if(startAnimation){
			screen.renderData(EmptyBedData, x, y, spriteWidth, spriteHeight, 4);
		}else{
			screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		}
	
	}

	
}
