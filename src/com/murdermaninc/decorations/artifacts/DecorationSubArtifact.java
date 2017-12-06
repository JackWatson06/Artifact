package com.murdermaninc.decorations.artifacts;

import com.mudermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;

public class DecorationSubArtifact extends DecorationArtifact{

	//TODO SubArtifacts in the center of their tile icon using widthPixels and heightPixels
	
	public int artifactBarPosition;
	
	public DecorationSubArtifact(int id, int x, int y, int xTile, int yTile,  int artifactBarPostition, int widthPixels, int heightPixels, int spriteWidth, int spriteHeight, int render, String name, LevelSequencing levelS) {
		super(id, x, y, xTile, yTile, widthPixels, heightPixels, spriteWidth, spriteHeight, render, name, levelS);
		
		this.artifactBarPosition = artifactBarPostition;

	}

	public void tick(Player player){
		
		if(!pickUp){
			
			if(tickcounter % 2 == 0){
				if(tickcounter <= 60){
					y -= 1;
					direction = true;
				}else{
					y += 1;
					direction = false;
				}
			}
			if(tickcounter > 120){
				tickcounter = 0;
			}
		
		
			tickcounter++;
		}
	}
	
	public void render(Screen screen, float interpolation){
		
		if(pickUp && !clear){
			int animationFrames = 10;
			float seconds = 1;
			float animationTime = (float)(60 * seconds) / (animationFrames);
		
		
			for(int i = 0; i < animationFrames; i++){
				if(animationCounter >= i * animationTime && animationCounter <= (i + 1) * animationTime){
					screen.render(x, y, 1 + i, 7, 1, 1, 4, "Icons");
				}
			}
			
			animationCounter+= (int) interpolation;
			
			if(animationCounter > animationTime * animationFrames){
				clear = true;
				levelSequence.artifactFound(name, xTile, yTile, widthPixels, heightPixels, artifactBarPosition);
			}	
		}else if(!clear){
			screen.render(x + ((64 - (widthPixels * 4)) / 2), y + ((64 - (heightPixels * 4)) / 2), xTile, yTile, 1, 1, 4, "Icons");
		}
	}

}
