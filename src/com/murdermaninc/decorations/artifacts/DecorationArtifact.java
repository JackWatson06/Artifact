package com.murdermaninc.decorations.artifacts;

import com.mudermaninc.entity.Player;
import com.murdermaninc.decorations.Decoration;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;

public class DecorationArtifact extends Decoration {

	
	//TODO add particle effect to the main artifcat to add a large distinguisher between the main artifact and the sub artifact.
	public String name;
	int tickcounter = 0;
	boolean direction = true;
	boolean clear = false;
	
	public LevelSequencing levelSequence;
	
	public int widthPixels, heightPixels;
	
	
	public DecorationArtifact(int id, int x, int y, int xTile, int yTile, int widthPixels, int heightPixels, int spriteWidth, int spriteHeight, int render, String name, LevelSequencing levelS) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);
		this.name = name;
		this.levelSequence = levelS;
		this.widthPixels = widthPixels;
		this.heightPixels = heightPixels;

	}

	public void tick(Player player){
		
		if(!pickUp){
			//System.out.println("Testing B!!!");
			//System.out.println("XCoord: " + x);
			//System.out.println("YCoord: " + y);
			
			if(tickcounter < 60){
				direction = true;
				y--;
			}else if(tickcounter >= 60 && tickcounter < 120){
				direction = false;
				y++;
			}else if(tickcounter >= 120 && tickcounter < 180){
				direction = false;
				y++;
			}else if(tickcounter >= 180 && tickcounter < 240){
				direction = true;
				y--;
			}else{
				direction = true;
				tickcounter = 0;
				y--;
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
				levelSequence.mainArtifactFound();
			}	
		}else if(!clear){
			screen.render(x + ((64 - (widthPixels * 4)) / 2), y  + ((64 - (heightPixels * 4)) / 2), xTile, yTile, 1, 1, 4, "Icons");
		}
	}
	
}
