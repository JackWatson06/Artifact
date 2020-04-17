package com.murdermaninc.decorations;

import com.murdermaninc.entity.Player;

public class ClimableVine extends Decoration{

	public ClimableVine(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);
	}
	
	public void tick(Player player){
		if(player.x + 63 - 8 >= x && player.x + 8 <= x + 63 && player.y + 63 >= y + (4 * 4) && player.y + 4 <= y + (4 * 4) + 63 && player.input.e && !player.isClimbingHorizontal){
			player.input.e = false;
			player.isClimbingHorizontal = true;
			player.y = y + 48; 
			player.resetAnimation();
			
			//Scan right for climb offset
			
			boolean findOffset = true;
			int testOffset = 1;
			boolean foundNothing = false;
			while(findOffset){
				int testX = x + testOffset * 64;
				for(int i = 0; i < decorations.size(); i++){
					if(decorations.get(i).y == y && decorations.get(i).x == testX && (decorations.get(i).id == 2052 || decorations.get(i).id == 2053 || decorations.get(i).id == 2054)){
						testOffset++;
						break;
					}
					if(i == decorations.size() - 1){
						foundNothing = true;
					}
				}
				
				if(foundNothing){
					player.rightClimbBound = testX + 28;
					findOffset = false;
				}
			}
			
			//Scan left for climb offset
			
			foundNothing = false;
			testOffset = 1;
			findOffset = true;
			while(findOffset){
				int testX = x - testOffset * 64;
				for(int i = 0; i < decorations.size(); i++){
					if(decorations.get(i).y == y && decorations.get(i).x == testX && (decorations.get(i).id == 2052 || decorations.get(i).id == 2053 || decorations.get(i).id == 2054)){
						testOffset++;
						break;
					}
					if(i == decorations.size() - 1){
						foundNothing = true;
					}
				}
				
				if(foundNothing){
					player.leftClimbBound = testX + 63 - 28;
					findOffset = false;
				}
			}
			

			
			
			
		}
	}

}
