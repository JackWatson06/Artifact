package com.murdermaninc.levelCreator;


public class DecorationSubArtifact extends DecorationArtifact{

	public int artifactBarPosition;
	
	public DecorationSubArtifact(int id, int x, int y, int xTile, int yTile,  int artifactBarPostition, int widthPixels, int heightPixels, int spriteWidth, int spriteHeight, int render, String name) {
		super(id, x, y, xTile, yTile, widthPixels, heightPixels, spriteWidth, spriteHeight, render, name);
		
		this.artifactBarPosition = artifactBarPostition;

	}

	
}
