package com.murdermaninc.levelCreator;

public class Tree extends Decoration{

	public Tree(int id, int x, int y, int startXTile, int startYTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, startXTile, startYTile, spriteWidth, spriteHeight, render);

	}

	@Override
	public void render(Screen screen){
		
		if(id == 2039){
			screen.render(x, y, 0, 0, 11, 1, 4, "iconsTree");
			screen.render(x, y - 60, 13, 12, 11, 1, 4, "iconsTree");
			screen.render(x + 236, y + 56, 13, 0, 4, 12, 4, "iconsTree");
			screen.render(x + 140, y + 748 + 28, 0, 1, 7, 6, 4, "iconsTree");
		}else if(id == 2040){
			screen.render(x, y, 0, 0, 11, 1, 4, "iconsTree");
			screen.render(x, y - 60, 13, 12, 11, 1, 4, "iconsTree");
			screen.render(x + 220, y + 56, 17, 0, 4, 12, 4, "iconsTree");
			screen.render(x + 128, y + 784 + 28, 7, 1, 6, 6, 4, "iconsTree");
		}else{
			screen.render(x, y, 0, 0, 11, 1, 4, "iconsTree");
			screen.render(x, y - 60, 13, 12, 11, 1, 4, "iconsTree");
			screen.render(x + 244, y + 56, 21, 0, 3, 12, 4, "iconsTree");
			screen.render(x + 164, y + 784 + 28, 0, 7, 7, 6, 4, "iconsTree");
		}
	}
}
