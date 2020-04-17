package com.murdermaninc.blocks;

public class GrassBlock extends Block {
	
	public GrassBlock(int id, int x, int y, int xTile, int yTile) {
		super(id, x, y, xTile, yTile);
		collisions = true;
		
	}
}
