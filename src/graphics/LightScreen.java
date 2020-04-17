package com.murdermaninc.graphics;

import com.murdermaninc.blocks.Block;

public class LightScreen {

	private double slope;
	private int levelWidth, levelHeight;
	private int levelWidthPixels, levelHeightPixels;
	private int width;
	public boolean[] shadowData;
	
	
	//TODO Make the light screen more productive (maybe just a tad more but I am extremely happy with what I have as I met the mark of 100 fps on almost every single level, except for when the level is completely dark which I don't plan on that happening to often)
	//LATER Fix shadows on level such as level2(and level2 in general)
	
	
	//LATER Brainstorm solution to the corners of blocks and the shadows they cast
	
	
	public LightScreen(int levelWidth, int levelHeight, int width, int height){
		this.levelWidth = levelWidth;
		this.levelHeight = levelHeight;
		levelWidthPixels = levelWidth * 64;
		levelHeightPixels = levelHeight * 64 - 8;
		this.width = width;
	}
	
	double angle = 45.0D;
	
	//LATER Fix issue with blank spots on -45.0 and other degrees and also 90
	
	public void loadLight(boolean addOne){
		//LATER Make this angle able to work in reverse
		
		shadowData = new boolean[(levelWidth * 64) * (levelHeight * 64)];
		
		if(addOne){
			angle = angle - 1.0F;
		}
		
		System.out.println(angle);
		
		slope = obtainEquation(angle) / levelHeight;
		
		System.out.println(slope);
		
		//System.out.println(slope);
		
		//This will scan through he left hand side of the screen and draw light rays from there and numbers will be added to the equation
		int yValue = 0;
		int xValue = 0;
		
		for(int b = 0; b < levelHeightPixels; b++){
			boolean shadowRay = false;
			
			if(angle < 0.0D){
				xValue = levelWidthPixels - 1;
				b = levelHeightPixels - b;
			}
			
			while(yValue < levelHeightPixels && yValue >= 0 && xValue >= 0 &&  xValue < levelWidthPixels){

				
				Block currentBlock = Block.blocks[(int) Math.floor((xValue) / 64) + (int) Math.floor((yValue) / 64) * levelWidth];
				
				int DataX = xValue - ((int) Math.floor(xValue / 64) * 64);
				int DataY = yValue - ((int) Math.floor(yValue / 64) * 64);
				
				//System.out.println(DataY);
				
				if((currentBlock.id == 0 || currentBlock.Data[DataX + DataY * 64] == -1086453) && shadowRay){
					shadowData[xValue + yValue * width] = true;

				}
				
				if(currentBlock.id != 0 && currentBlock.Data[DataX + DataY * 64] != -1086453 && !shadowRay){
					shadowRay = true;
				}
				
				if(angle < 0.0D){
					xValue--;
				}else{
					xValue++;
				}
			
				
				yValue = (int)(slope * xValue) + b;
				
				//System.out.println(xValue);
				//System.out.println(yValue);
				

			}

			yValue = 0;
			xValue = 0;
		}
		
		//This will scan through the top of the screen and draw light rays from the top of the screen and numbers will be subtracted from the equation
		
		//B is the amount of lines drawn through the level based on the pixel width of the level
			for(int b = 0; b < levelWidthPixels; b++){
				boolean shadowRay = false;
		
				//Sets the xValue to the value of b which would be the start of the diagonal line
				xValue = b;
				
				//This keeps the shadow checking contained in the level
				while(xValue < levelWidthPixels && xValue >= 0 && yValue >= 0 &&  yValue < levelHeightPixels){

					
					Block currentBlock = Block.blocks[(int) Math.floor((xValue) / 64) + (int) Math.floor((yValue) / 64) * levelWidth];
					
					//This is used for the calculation of free space based on the actual tile data
					int DataX = xValue - ((int) Math.floor(xValue / 64) * 64);
					int DataY = yValue - ((int) Math.floor(yValue / 64) * 64);
				
					//This sets a shadow
					if((currentBlock.id == 0 || currentBlock.Data[DataX + DataY * 64] == -1086453) && shadowRay){
						shadowData[xValue + yValue * width] = true;

					}
				
					
					//This sets the shadow to continue on the line
					if(currentBlock.id != 0 && currentBlock.Data[DataX + DataY * 64] != -1086453 && !shadowRay){
						shadowRay = true;
					}
				
				
					//This increases the yValue
					yValue++;
				
					//This finds the xValue based on the yValue of the current slope, acts as a inverse linear equation
					xValue = (int) ((yValue + (b * slope)) / slope);
				
				
				}	
				
				xValue = 0;
				yValue = 0;
			}
		
		
		
		
	}
	
	public double obtainEquation(double angle){
		double slopeEquation =  Math.tan(Math.toRadians(angle)) * levelHeight;
		return slopeEquation;
	}
	

	
	public void renderLight(Screen screen){
		int lightOffset = screen.screenX + (screen.screenY * levelWidthPixels);
		
		
		
		for(int y = 0; y < screen.height; y++){
			for(int x = 0; x < screen.width; x++){
				if(shadowData[lightOffset + (x + (y * levelWidthPixels))]){
					//screen.pixels[x + (y * screen.width)] = darken(screen.pixels[x + (y * screen.width)]);
					//int color = screen.pixels[x + (y * screen.width)];
					//screen.pixels[x + (y * screen.width)] = ((((color & 0xFF0000) * 7) / 8) & 0xFF0000) | ((((color & 0xFF00) * 4) / 5) & 0xFF00) | (((((color) & 0xFF) * 4) / 5));
				}
			}
		}
	}
	// ((int) (((color & 0xFF0000) * 3) / 4) & 0xFF0000) | ((int) (((color & 0xFF00) * 3) / 4) & 0xFF00) | ((int) ((((color) & 0xFF) * 3) / 4));
	
	//LATER Maybe use subtraction for determining the darkness instead of multiplication and division as it may be faster.
	
	//((int) ((color & 0x00FF0000) * percentage) & 0x00FF0000) | ((int) ((color & 0x0000FF00) * percentage) & 0x0000FF00) | ((int) (((color) & 0xFF) * percentage));
	//((int) ((color << 8) * percentage) >> 8) | ((int) ((color << 16) * percentage) >> 16) | ((int) ((color << 24) * percentage) >> 24);
	//((int) (((color >> 16) & 0xFF) * percentage) << 16) | ((int) (((color >> 8) & 0xFF) * percentage) << 8) | ((int) (((color) & 0xFF) * percentage));
	
	// ex.) 0.75 is 75 percent of the original color brightness so 25 percent darker.
	//LATER May make this 90% later or something else depending on what I like (9 / 10) and 75% is (3 / 4)
	
	//private float percentage = 0.75F;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*if(angle <= 45.0D){
	for(int y = 1; y < levelWidthPixels; y++){
		boolean shadowRay = false;
	
		xValue = y;
		while(yValue < levelHeightPixels && yValue >= 0 && xValue >= 0 &&  xValue < levelWidthPixels){

		
			Block currentBlock = Block.blocks[(int) Math.floor((xValue) / 64) + (int) Math.floor((yValue) / 64) * levelWidth];
		
			int DataX = xValue - ((int) Math.floor(xValue / 64) * 64);
			int DataY = yValue - ((int) Math.floor(yValue / 64) * 64);
		
			if((currentBlock.id == 0 || currentBlock.Data[DataX + DataY * 64] == -1086453) && shadowRay){
				shadowData[xValue + yValue * width] = true;

			}
		
			if(currentBlock.id != 0 && currentBlock.Data[DataX + DataY * 64] != -1086453 && !shadowRay){
				shadowRay = true;
			}
		
			//System.out.println("YBefore: " + y);
			//System.out.println("XBefore: " + xValue);
			//System.out.println("YValueBefore: " + yValue);
		
			xValue++;
		
			if(angle < 45.0D){
				yValue = (int) ((slope * xValue) - (y * slope));
			}else{
				yValue = (int) ((slope * xValue) - y);
			}
		
			//System.out.println("XValue: " + xValue);
			//System.out.println("Y: " + y);
			//System.out.println("YValue: " + yValue);
		
		//	if(xValue == 4) {
				//break;
			//}
		}
		
		xValue = 0;
		yValue = 0;
	}
	
	
	/*if(y == 3) {
		//break;
	}*/

//}else{*/
	
}
