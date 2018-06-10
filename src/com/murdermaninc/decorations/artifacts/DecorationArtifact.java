package com.murdermaninc.decorations.artifacts;

import java.util.ArrayList;

import com.murdermaninc.entity.Player;
import com.murdermaninc.decorations.Decoration;
import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;

public class DecorationArtifact extends Decoration {

	
	//TODO add particle effect to the main artifcat to add a large distinguisher between the main artifact and the sub artifact.
	public String name;
	int tickCounter = 0;
	boolean direction = true;
	boolean clear = false;
	
	protected Animation animationParticle = new Animation();
	protected Animation animationCollect = new Animation();
	
	protected ArrayList<int[]> ParticleData;
	
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
			if(tickCounter < 60){
				direction = true;
				y--;
			}else if(tickCounter >= 60 && tickCounter < 120){
				direction = false;
				y++;
			}else if(tickCounter >= 120 && tickCounter < 180){
				direction = false;
				y++;
			}else if(tickCounter >= 180 && tickCounter < 240){
				direction = true;
				y--;
			}else{
				direction = true;
				tickCounter = 0;
				y--;
			}

		
			tickCounter++;
		}
		
		
		/*if(!pickUp) {
			
			if(tickCounter == 60) {
				y+=5;
			}else if(tickCounter == 120) {
				y-=5;
			}else if(tickCounter == 180) {
				y-=5;
			}else if(tickCounter == 240) {
				y+=5;
				tickCounter = 0;
			}
			
			tickCounter++;
			
			
			
		}*/
	}
	
	public void render(Screen screen, float interpolation){
		
		if(ParticleData == null) ParticleData = animationParticle.loadAnimationData(screen, "Icons", 4, 7, 0, 19, 1, 1);
		if(AnimationData == null) AnimationData = animationCollect.loadAnimationData(screen, "Icons", 4, 10, 0, 18, 1, 1);
		
		
		
		if(pickUp && !clear){
			/*int animationFrames = 10;
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
			}	*/
			
			animationCollect.animateOnce(screen, AnimationData, false, 10, 1, 1, x, y, 10, 4, interpolation);
			
			
			if(!animationCollect.once) {
				clear = true;
				levelSequence.mainArtifactFound();
			}
			
		}else if(!clear){
			screen.render(x + ((64 - (widthPixels * 4)) / 2), y  + ((64 - (heightPixels * 4)) / 2), xTile, yTile, 1, 1, 4, "Icons");
			
			//animation.animateContinuous(screen, ParticleData, false, 4.5F, 1, 1, x , y + 32, 7, 4, interpolation);
			
		}
		
	}
	
}
