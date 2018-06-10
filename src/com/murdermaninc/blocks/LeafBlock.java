package com.murdermaninc.blocks;


public class LeafBlock extends Block{

	public LeafBlock(int id, int x, int y, int xTile, int yTile) {
		super(id, x, y, xTile, yTile);
		collisions = true;
		notFull = true;
		collisionPixelsHeight = 7 * 4;
	}
	

}
