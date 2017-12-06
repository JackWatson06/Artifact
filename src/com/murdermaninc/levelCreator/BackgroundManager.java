package com.murdermaninc.levelCreator;

import com.murdermaninc.graphics.Background;

public class BackgroundManager {

	private Background background;
	
	private int yOffset = 0;
	private int xOffset = 0;
	
	public BackgroundManager(String path, int width, int height, boolean scale){
		if(scale){
			background = new Background(path, true);
			background.scaleImage(4, width, height);
		}else{
			background = new Background(path, false);
		}
	}
	
	public BackgroundManager(String path, int width, int height, boolean scale, int xOffset, int yOffset){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		if(scale){
			background = new Background(path, true);
			background.scaleImage(4);
		}else{
			background = new Background(path, false);
		}
		background.scrolling = true;
	}
	
	public void render(Screen screen){
		if(!background.scrolling){
			for(int i = 0; i < background.pixels.length; i++){
				screen.pixels[i] = background.pixels[i];
			}
		}else{
			int backgroundWidth = background.scaledWidth;
			int screenWidth = screen.width;
			for(int y = 0; y < screen.height; y++){
				for(int x = 0; x < screen.width; x++){
					screen.pixels[x + y * screenWidth] = background.pixels[(x + xOffset) + (y + yOffset) * backgroundWidth];
				}
			}
		}
	}
}
