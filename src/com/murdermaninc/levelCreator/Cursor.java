package com.murdermaninc.levelCreator;

import java.awt.MouseInfo;


public class Cursor {

	public int currentMouseXS;
	public int currentMouseYS;
	
	public int currentMouseXNS;
	public int currentMouseYNS;
	
	public Cursor(){
		
	}
	
	public void tick(Screen screen){
		currentMouseXS = ((MouseInfo.getPointerInfo().getLocation().x + screen.screenX) / 64) * 64;
		currentMouseYS = ((MouseInfo.getPointerInfo().getLocation().y + screen.screenY) / 64) * 64;
		

		
		currentMouseXNS = ((MouseInfo.getPointerInfo().getLocation().x + screen.screenX) / 64);
		currentMouseYNS = ((MouseInfo.getPointerInfo().getLocation().y + screen.screenY) / 64);
		

	}
	
	
	public void render(Screen screen){

		screen.render(currentMouseXS, currentMouseYS, 11, 15, 1, 1, 4, "icons");
	}
}
