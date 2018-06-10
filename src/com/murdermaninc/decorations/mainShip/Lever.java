package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class Lever extends DecorationShip{

	private Animation animation = new Animation();
	private ArrayList<int[]> animationData;

	
	//This id tells if the lever is up or down
	private Monitor monitor;
	
	public int id;
	
	public Lever(MainShipWorldManager manager, Monitor monitor, int leverId, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		animation.once = false;
		
		this.monitor = monitor;
		
		this.id = leverId;

	}
	
	@Override
	public void tick(){

		if(manager.input.e && !animation.once && manager.player.x + 8 <= x + 52 && manager.player.x + 63 - 8 >= x && manager.player.y + 4 <= y + 160 && manager.player.y + 63 - 4 >= y){
			if(id == 0) {
				monitor.currentLevelNumber++;
				if(monitor.currentLevelNumber > 16){
					monitor.currentLevelNumber = 1;
				}
			}
			if(id == 1) {
				monitor.currentLevelNumber--;
				if(monitor.currentLevelNumber <= 0){
					monitor.currentLevelNumber = 16;
				}
			}
			animation.once = true;
			manager.input.e = false;
		}


	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(animationData == null) {
			if(id == 0) {
				animationData = animation.loadAnimationData(screen, "shipIcons", 4, 5, xTile, yTile, spriteWidth, spriteHeight);
			}else {
				animationData = animation.loadAnimationData(screen, "shipIcons", 4, 5, xTile, yTile, spriteWidth, spriteHeight);
			}
		}
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		
		if(animation.once){
			animation.animateOnce(screen, animationData, true, 10, spriteWidth, spriteHeight, x, y, 8, 4, interpolation);
		}else{
			screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		}
	}
	
	

}
