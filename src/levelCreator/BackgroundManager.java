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
			
			float ratioY = (float) background.scaledHeight / screen.height;
			float ratioX = (float) background.scaledWidth / screen.width;
			
			for(int y = 0; y < screen.height; y++) {
				for(int x = 0; x < screen.width; x++) {
					screen.pixels[x + y * screen.width] = background.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * background.scaledWidth)];
				}
			}
		}else{
			int backgroundWidth = background.scaledWidth;
			int screenWidth = screen.width;
			
			float ratioY = (float) (background.scaledHeight - (background.scaledHeight - 1080)) / screen.height;
			float ratioX = (float) (background.scaledWidth - (background.scaledWidth - 1920)) / screen.width;
			
			for(int y = 0; y < screen.height; y++){
				for(int x = 0; x < screen.width; x++){
					screen.pixels[x + y * screenWidth] = background.pixels[((int) (x * ratioX) + xOffset) + (((int)(y * ratioY) + yOffset)) * backgroundWidth];
				}
			}
		}
	}
}
