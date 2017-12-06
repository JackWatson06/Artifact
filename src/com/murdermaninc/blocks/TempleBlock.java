package com.murdermaninc.blocks;

public class TempleBlock extends Block{

	public TempleBlock(int id, int x, int y, int xTile, int yTile) {
		super(id, x, y, xTile, yTile);
		collisions = true;
	}

}
