package com.murdermaninc.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	public int width, height;
	public int[] pixels;
	
	//TODO maybe adjust coloring of sprite sheet to show the boundaries of the orange tile, you know what I mean!
	
	public SpriteSheet(BufferedImage image){
		height = image.getHeight();
		width = image.getWidth();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);	
	}
}
