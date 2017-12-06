package com.murdermaninc.graphics;

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
	
	public void removeSpriteSheets(){
		spriteSheets.clear();
		spriteSheetName.clear();
	}
	
	public void listCurrentSpritesSheets(Screen screen){
		System.out.println((spriteSheets.size() + screen.spriteSheets.size()));
	}
	
	public void listCurrentSpritesSheets(){
		System.out.println(spriteSheets.size());
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
	
	//This is the preloaded data section
	
	public int[] loadData(int startXTile, int startYTile, int spriteWidth, int spriteHeight, int scale, String currentSpriteSheetName){
		
		int currentSpriteSheet = 0;
		
		int[] Data = new int[(spriteWidth * 16) * (spriteHeight * 16)];
		int[] ScaledData = new int[((spriteWidth * 16) * (spriteHeight * 16)) * (scale * scale)];
	
		int pixelsWidth = spriteWidth * 16;
		int pixelsHeight = spriteHeight * 16;
	
		for(int i = 0; i < spriteSheetName.size(); i++){
			if(spriteSheetName.get(i).equals(currentSpriteSheetName)){
				currentSpriteSheet = i;
			}
		}
		
		
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
		
		return ScaledData;
		
		
	}
	
	//Make this work with only the Data not scaled because theoretically there is no use of storing the scaled data as it just wastes space becuase the render method has to loop
	//through the scaled Data anyway and it would be the same length of looping if it scaled during the render process.
	
	public void renderData(int[] Data, int xp, int yp, int spriteWidth, int spriteHeight, int scale){
		int windowLX = screenX;
		int windowRX = screenX + width;
		int windowTY = screenY;
		int windowBY = screenY + height;
		
		//TODO Using the times 64 might actually be bad if the scale was something less than 4 say 2 the image would be 32 by 32 thus the edges might be strange.
		
		
		
		if(xp + (spriteWidth * 64 - 1) >= windowLX && xp < windowRX && yp + (spriteHeight * 64 - 1) >= windowTY && yp < windowBY){
			
			xp -= screenX;
			yp -= screenY;

		
			int pixelsWidth = spriteWidth * 16;
			int pixelsHeight = spriteHeight * 16;
			
			for(int y = 0; y < pixelsHeight * scale; y++){
				for(int x = 0; x < pixelsWidth * scale; x++){
					if(xp + x >= windowLX - screenX && xp + x < windowRX - screenX && yp + y >= windowTY - screenY && yp + y < windowBY - screenY){
						if(Data[x + (y * (pixelsWidth * scale))] != -1086453){
								pixels[(((yp + y) * width)) + (xp + x)] = Data[x + (y * (pixelsWidth * scale))];
						}
					}
				}
			}
		}
	}
	
}

/*
 *THIS USES PRE SCALED DATA
 * 
 * 
 * int windowLX = screenX;
		int windowRX = screenX + width;
		int windowTY = screenY;
		int windowBY = screenY + height;
		
		//TODO Using the times 64 might actually be bad if the scale was something less than 4 say 2 the image would be 32 by 32 thus the edges might be strange.
		
		if(xp + (spriteWidth * 64 - 1) >= windowLX && xp < windowRX && yp + (spriteHeight * 64 - 1) >= windowTY && yp < windowBY){
			
			
			xp -= screenX;
			yp -= screenY;
		
			int pixelsWidth = spriteWidth * 16;
			int pixelsHeight = spriteHeight * 16;
			
			for(int y = 0; y < pixelsHeight * scale; y++){
				for(int x = 0; x < pixelsWidth * scale; x++){
					if(xp + x >= windowLX - screenX && xp + x < windowRX - screenX && yp + y >= windowTY - screenY && yp + y < windowBY - screenY){
						if(Data[x + (y * (pixelsWidth * scale))] != -1086453){
								pixels[(((yp + y) * width)) + (xp + x)] = Data[x + (y * (pixelsWidth * scale))];
						}
					}
				}
			}
		}
		*/



/*
 * THIS SCALES THE DATA WHILE RENDERING
 * 
 * 
 * int windowLX = 0;
		int windowRX = width;
		int windowTY = 0;
		int windowBY = height;
		
		//TODO Using the times 64 might actually be bad if the scale was something less than 4 say 2 the image would be 32 by 32 thus the edges might be strange.

		xp -= screenX;
		yp -= screenY;
		
		if(xp + (spriteWidth * 64 - 1) >= windowLX && xp < windowRX && yp + (spriteHeight * 64 - 1) >= windowTY && yp < windowBY){
			
		
			int pixelsWidth = spriteWidth * 16;
			int pixelsHeight = spriteHeight * 16;
			
			for(int y = 0; y < pixelsHeight; y++){
				for(int x = 0; x < pixelsWidth; x++){
					for(int sy = 0; sy < scale; sy++){
						for(int sx = 0; sx < scale; sx++){
							if(xp + (x * scale) + sx >= windowLX && xp + (x * scale) + sx < windowRX && yp + (y * scale) + sy >= windowTY && yp + (y * scale) + sy < windowBY){
								if(Data[x + (y * pixelsWidth)] != -1086453){
										pixels[(((yp + (y * scale) + sy) * width)) + (xp + (x * scale) + sx)] = Data[x + (y * pixelsWidth)];
								}
							}
						}
					}
				}
			}
		}
 * 
 * 
 * 
 */
