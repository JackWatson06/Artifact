package com.murdermaninc.blocks;

public class DirtBlock extends Block{

	public DirtBlock(int id, int x, int y, int xTile, int yTile) {
		super(id, x, y, xTile, yTile);
		collisions = true;
	}

}
