package com.murdermaninc.blocks;


public class RockBlock extends Block{

	public RockBlock(int id, int x, int y, int xTile, int yTile) {
		super(id, x, y, xTile, yTile);
		collisions = true;
	}
}
