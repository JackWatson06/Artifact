package com.murdermaninc.levelCreator;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	public int width, height;
	public int[] pixels;
	
	
	public SpriteSheet(BufferedImage image){
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}
}
