package com.murdermaninc.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.murdermaninc.main.Main;

public class Background {

	public int width, height;
	public int scaledWidth, scaledHeight;
	public boolean scrolling = false;
	public int[] scaledDown;
	public int[] pixels;
	
	public Background(String path, boolean scaleBackground){
		BufferedImage image;
		try {
			image = ImageIO.read(Main.class.getResource(path));
			height = image.getHeight();
			width = image.getWidth();
			if(!scaleBackground){
				pixels = image.getRGB(0, 0, width, height, null, 0, width);
				scaledWidth = width;
				scaledHeight = height;
			}else{
				scaledDown = image.getRGB(0, 0, width, height, null, 0, width);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void scaleImage(int scale, int screenWidth, int screenHeight) {
		
		//This is for scaling an image to a meet the desired constraints.
		
		this.scaledWidth = screenWidth;
		this.scaledHeight = screenHeight;
		pixels = new int[screenWidth * screenHeight];
		for(int by = 0; by < height; by++){
			for(int bx = 0; bx < width; bx++){
				int current = scaledDown[bx + (by * width)];
				for(int y = 0; y < scale; y++){
					for(int x = 0; x < scale; x++){
						pixels[(x + (bx * scale)) + ((y + (by * scale)) * screenWidth)] = current;
					}
				}
			}
		}
		
		
	}
	
	public void scaleImage(int scale) {
		
		//This just scales the image by the designated scale.
		
		this.scaledWidth = width * scale;
		this.scaledHeight = height * scale;
		pixels = new int[scaledWidth * scaledHeight];
		for(int by = 0; by < height; by++){
			for(int bx = 0; bx < width; bx++){
				int current = scaledDown[bx + (by * width)];
				for(int y = 0; y < scale; y++){
					for(int x = 0; x < scale; x++){
						pixels[(x + (bx * scale)) + ((y + (by * scale)) * scaledWidth)] = current;
					}
				}
			}
		}
		
	}
	
	
}
