package com.murdermaninc.text;

import com.murdermaninc.graphics.Screen;

public class TextBoxPieces {

	
	private int[] Data;
	
	private int x, y;
	private int xTile, yTile;
	
	
	public TextBoxPieces(int x, int y, int xTile, int yTile) {
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
	}
	
	public void render(Screen screen) {
		
		if(Data == null) Data = screen.loadData(xTile, yTile, 1, 1, 4, "textBox");
		
		screen.renderData(Data, x, y, 1, 1, 4);
		
	}
}
