package com.mudermaninc.entity;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.murdermaninc.graphics.Screen;
import com.murdermaninc.graphics.SpriteSheet;
import com.murdermaninc.main.Main;

public class GrassBoss extends Entity{

	private SpriteSheet currentAnimationSheet;
	//private int spriteWidth, spriteHeight;
	private int animationTime = 0;
	
	public GrassBoss(String name, int x, int y, int spriteWidth, int spriteHeight){
		this.x = x;
		this.y = y;
		this.name = name;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
	}
	
	public void render(Screen screen){
		if(currentAnimationSheet == null){
			try {
				currentAnimationSheet = new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream("/GrassBoss - SpriteSheet - 4x.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		int currentXTile = 0;
		//int currentYTile = 0;
		int rows = 5;
		int sprites = 26;
		int fps = 10;
		float seconds = sprites / fps;
		float animationTicks = (float)(60 * seconds) / (sprites);
		
		for(int i = 0; i < sprites; i++){
			if(animationTime >= i * animationTicks && animationTime <= (i + 1) * animationTicks){
				//currentYTile = 0;
				currentXTile = i;
				for(int yTile = 0; yTile < rows; yTile++){
					if(currentXTile > 5){
						//currentYTile++;
						currentXTile -= 6;
					}
				}
				//screen.renderSpriteSheet(x, y, currentXTile, currentYTile, spriteWidth, spriteHeight, currentAnimationSheet);
			}
		}
		
		
		animationTime++;
		
		if(animationTime > animationTicks * sprites){
			//currentYTile = 0;
			currentXTile = 0;
			animationTime = 0;
		}	
		
	}
	
	public void despawn(){
		int[] empty = {};
		currentAnimationSheet.pixels = empty;
		
	}
}
