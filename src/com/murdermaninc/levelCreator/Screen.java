package com.murdermaninc.levelCreator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.murdermaninc.main.Main;

public class Screen {

	public int width, height;
	public int screenX, screenY;
	
	private ArrayList<SpriteSheet> spriteSheets = new ArrayList<SpriteSheet>();
	private ArrayList<String> spriteSheetName = new ArrayList<String>();
	
	public BufferedImage image;
	public int[] pixels;
	
	Color transparent = new Color(0,0,0,0);
	
	//TODO - remove adding sprite sheet on the creation of the screen class
	//TODO - remove level sprite sheet once level is quite or cleared.
	
	public Screen (int width, int height, int screenX, int screenY, int type){
		this.width = width;
		this.height = height;
		this.screenX = screenX;
		this.screenY = screenY;
		
		if(type == 0){
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		}else{
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		}
	}
	
	public void loadSpriteSheet(String spriteSheetPath, String spriteSheetName){
		try {
			this.spriteSheets.add(new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream(spriteSheetPath))));
			this.spriteSheetName.add(spriteSheetName);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	//This is usually used for animations and small other items never large items
	
	//SCROLLING OUT REQUIRMENTS
	/*
	 * Window size must be changed because that is what actually loads the specific amount of blocks based off the size of the window.
	 * 
	 * Point to a new pixel loading method that scales down the window size pixels to the final int[] which is 1920 x 1080
	 * 
	 * Write to a windowPixel[] which is an array of pixels that are currently visible, then scale down this [] to the final int[]
	 * 
	 */
	
	
	
	
	
	public void render(int xp, int yp, int startXTile, int startYTile, int spriteWidth, int spriteHeight, int scale, String currentSpriteSheetName){
		
		int currentSpriteSheet = 0;
		
		int windowLX = screenX;
		int windowRX = screenX + width;
		int windowTY = screenY;
		int windowBY = screenY + height;
		
		for(int i = 0; i < spriteSheetName.size(); i++){
			if(spriteSheetName.get(i).equals(currentSpriteSheetName)){
				currentSpriteSheet = i;
			}
		}
		
		if(xp + (spriteWidth * 64 - 1) >= windowLX && xp < windowRX && yp + (spriteHeight * 64 - 1) >= windowTY && yp < windowBY){
			
			
			xp -= screenX;
			yp -= screenY;
		
			int[] Data = new int[(spriteWidth * 16) * (spriteHeight * 16)];
			int[] ScaledData = new int[((spriteWidth * 16) * (spriteHeight * 16)) * (scale * scale)];
		
			int pixelsWidth = spriteWidth * 16;
			int pixelsHeight = spriteHeight * 16;
		
			for(int y = 0; y < pixelsHeight; y++){
				for(int x = 0; x < pixelsWidth; x++){
					Data[x + (y * pixelsWidth)] = spriteSheets.get(currentSpriteSheet).pixels[((startYTile * 16 + y) * spriteSheets.get(currentSpriteSheet).width) + ((startXTile * 16) + x)];
				}
			}
			
			
			for(int dy = 0; dy < pixelsHeight; dy++){
				for(int dx = 0; dx < pixelsWidth; dx++){
					int currentColor = Data[dx + (dy * pixelsWidth)];
					for(int y = 0; y < scale; y++){
						for(int x = 0; x < scale; x++){
							ScaledData[(x + (dx * scale)) + ((y + (dy * scale)) * (pixelsWidth * scale))] = currentColor;
						}
					}
				}
				
			}
			
			for(int y = 0; y < pixelsHeight * scale; y++){
				for(int x = 0; x < pixelsWidth * scale; x++){
					if(xp + x >= windowLX - screenX && xp + x < windowRX - screenX && yp + y >= windowTY - screenY && yp + y < windowBY - screenY){
						if(ScaledData[x + (y * (pixelsWidth * scale))] != -1086453){
								pixels[(((yp + y) * width)) + (xp + x)] = ScaledData[x + (y * (pixelsWidth * scale))];
						}
					}
				}
			}
		}
		
	}
	
}
