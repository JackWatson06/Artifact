package com.murdermaninc.levelCreator;

public class DecorationArtifact extends Decoration{

	public String name;
	
	public DecorationArtifact(int id, int x, int y, int startXTile, int startYTile, int widthPixels, int heightPixels, int spriteWidth, int spriteHeight, int render, String name) {
		super(id, x, y, startXTile, startYTile, spriteWidth, spriteHeight, render);
		
		this.name = name;
	}

}
