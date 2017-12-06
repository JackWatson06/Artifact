package com.murdermaninc.collectionBackground;

import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;

public class Title {

	private int[] Data;
	
	private Font font = new Font();
	
	private String title;
	private int screenWidth;
	
	public Title(String title, int screenWidth){
		this.title = title;
		this.screenWidth = screenWidth;
	}
	
	public void render(Screen screen){
		if(Data == null){
			Data = screen.loadData(0, 0, 5, 2, 4, "TitleSprite");
		}
		
		screen.renderData(Data, screenWidth / 2 - ((80 * 4) / 2), 20, 5, 2, 4);
		
		
		font.drawText(screen, "Level " + title.substring(6), (screenWidth / 2) - (font.getTextLength("Level " + title.substring(5), 2) / 2), 20 + (((20 / 2) * 4) - (6 * 2) - (4 * 2)), 2);
		
	}
	
}
