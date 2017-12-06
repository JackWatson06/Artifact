package com.murdermaninc.decorations.mainShip;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;

public class Lever extends DecorationShip{

	private Animation animation = new Animation();
	private ArrayList<int[]> animationData;
	
	
	public int leverNumber = 1;
	
	public Lever(MainShipWorldManager manager, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight) {
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		animation.once = false;

	}
	
	@Override
	public void tick(){

		if(manager.input.e && !animation.once && manager.player.x + 8 <= x + 52 && manager.player.x + 63 - 8 >= x && manager.player.y + 4 <= y + 160 && manager.player.y + 63 - 4 >= y){
			leverNumber++;
			if(leverNumber > 16){
				leverNumber = 1;
			}
			animation.once = true;
			manager.input.e = false;
		}


	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(animationData == null) animationData = animation.loadAnimationDataRectangle(screen, "lever", 4, 4, xTile, yTile, spriteWidth, spriteHeight, 2, 2);
		
		if(animation.once){
			animation.animateOnce(screen, animationData, true, 10, spriteWidth, spriteHeight, x, y, 6, 4, interpolation);
		}else{
			screen.render(x, y, xTile, yTile, spriteWidth, spriteHeight, 4, "lever");
		}
	}
	
	

}
