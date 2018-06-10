package com.murdermaninc.levelCreator;

public class MiniMap {

	public int[] pixels;
	public int[] scaledPixels;
	
	public int levelWidth, levelHeight;
	public int scale = 5;
	
	private int xp = 10;
	private int yp = 10;
	
	private Main main;
	
	public MiniMap(Main main,int levelWidth, int levelHeight, int[] blocks, int[] decorations, int[] subArtifacts){
		this.main = main;
		this.levelWidth = levelWidth;
		this.levelHeight = levelHeight;
		pixels = new int[levelWidth * levelHeight];
		for(int i = 0; i < blocks.length; i++){
			if(blocks[i] != 0){
				pixels[i] = -4358970;
			}
		}
		
		for(int i = 0; i < decorations.length; i+=3){
			Decoration currentDecoration = main.editorDecoration(decorations[i], decorations[i + 1], decorations[i + 2]);
			if(currentDecoration != null){
				for(int y = 0; y < currentDecoration.spriteHeight; y++){
					for(int x = 0; x < currentDecoration.spriteWidth; x++){
						pixels[((currentDecoration.x / 64) + x) + ((currentDecoration.y / 64) + y) * levelWidth] = -81276365;
					}
				}
			}else{
				DecorationArtifact currentArtifact = main.editorDecorationArtifact(decorations[i], decorations[i + 1], decorations[i + 2]);
				for(int y = 0; y < currentArtifact.spriteHeight; y++){
					for(int x = 0; x < currentArtifact.spriteWidth; x++){
						pixels[((currentArtifact.x / 64) + x) + ((currentArtifact.y / 64) + y) * levelWidth] = -43214443;
					}
				}
			}
		}
		
		for(int i = 0; i < subArtifacts.length; i+=3){
			DecorationSubArtifact currentSubArtifact = main.editorDecorationSubArtifact(subArtifacts[i], subArtifacts[i + 1], subArtifacts[i + 2]);
			if(currentSubArtifact != null){
				for(int y = 0; y < currentSubArtifact.spriteHeight; y++){
					for(int x = 0; x < currentSubArtifact.spriteWidth; x++){
						pixels[((currentSubArtifact.x / 64) + x) + ((currentSubArtifact.y / 64) + y) * levelWidth] = -432321;
					}
				}
			}
		}
		
		scaledPixels = new int[(levelWidth * scale) * (levelHeight * scale)];
		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){
				for(int sy = 0; sy < scale; sy++){
					for(int sx = 0; sx < scale; sx++){
						scaledPixels[((x * scale) + sx) + ((y * scale) + sy) * (levelWidth * scale)] = pixels[x + y * levelWidth];
					}
				}
			}
		}
	}
	
	public void updateMiniMap(int[] blocks, int[] decorations, int[] subArtifacts){
		pixels = new int[levelWidth * levelHeight];
		for(int i = 0; i < blocks.length; i++){
			if(blocks[i] != 0){
				pixels[i] = -4358970;
			}
		}
		
		
		for(int i = 0; i < decorations.length; i+=3){
			Decoration currentDecoration = main.editorDecoration(decorations[i], decorations[i + 1], decorations[i + 2]);
			if(currentDecoration != null){
				for(int y = 0; y < currentDecoration.spriteHeight; y++){
					for(int x = 0; x < currentDecoration.spriteWidth; x++){
						pixels[((currentDecoration.x / 64) + x) + ((currentDecoration.y / 64) + y) * levelWidth] = -81276365;
					}
				}
			}else{
				DecorationArtifact currentArtifact = main.editorDecorationArtifact(decorations[i], decorations[i + 1], decorations[i + 2]);
				if(currentArtifact != null){
					for(int y = 0; y < currentArtifact.spriteHeight; y++){
						for(int x = 0; x < currentArtifact.spriteWidth; x++){
							pixels[((currentArtifact.x / 64) + x) + ((currentArtifact.y / 64) + y) * levelWidth] = -43214443;
						}
					}
				}
			}
		}
		
		for(int i = 0; i < subArtifacts.length; i+=3){
			DecorationSubArtifact currentSubArtifact = main.editorDecorationSubArtifact(subArtifacts[i], subArtifacts[i + 1], subArtifacts[i + 2]);
			if(currentSubArtifact != null){
				for(int y = 0; y < currentSubArtifact.spriteHeight; y++){
					for(int x = 0; x < currentSubArtifact.spriteWidth; x++){
						pixels[((currentSubArtifact.x / 64) + x) + ((currentSubArtifact.y / 64) + y) * levelWidth] = -432321;
					}
				}
			}
		}
		
		scaledPixels = new int[(levelWidth * scale) * (levelHeight * scale)];
		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){
				for(int sy = 0; sy < scale; sy++){
					for(int sx = 0; sx < scale; sx++){
						scaledPixels[((x * scale) + sx) + ((y * scale) + sy) * (levelWidth * scale)] = pixels[x + y * levelWidth];
					}
				}
			}
		}
	}
	
	public void render(Screen screen, InputManager input, int width, int height){
		if(input.m){
			
			int windowLX = 0;
			int windowRX = width;
			int windowTY = 0;
			int windowBY = height;
			
			for(int y = 0; y < levelHeight * scale; y++){
				for(int x = 0; x < levelWidth * scale; x++){
					if(xp + x >= windowLX && xp + x < windowRX && yp + y >= windowTY && yp + y < windowBY){
						screen.pixels[(xp + x) + ((yp + y) * width)] = scaledPixels[x + y * levelWidth * scale];
					}
				}
			}
			
		}
	}
}
