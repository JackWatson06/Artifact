package com.murdermaninc.levelCreator;

import java.awt.MouseInfo;


public class Cursor {

	public int currentMouseXS;
	public int currentMouseYS;
	
	public int currentMouseXNS;
	public int currentMouseYNS;
	
	public Cursor(){
		
	}
	
	public void tick(Screen screen, float scale){

		
		currentMouseXS = (int) ((float)((MouseInfo.getPointerInfo().getLocation().x  / scale + screen.screenX) / 64)) * 64;
		currentMouseYS = (int) ((float) ((MouseInfo.getPointerInfo().getLocation().y  / scale + screen.screenY) / 64)) * 64;
		

		
		currentMouseXNS = (int) ((float)((MouseInfo.getPointerInfo().getLocation().x  / scale + screen.screenX) / 64));
		currentMouseYNS = (int) ((float) ((MouseInfo.getPointerInfo().getLocation().y / scale + screen.screenY) / 64));
		

	}
	
	
	public void render(Screen screen){

		//It is necessary to scale the mouse because when everything gets scaled up to the larger resolution the mouse pointer at 1920 gets scaled to 2560 thus making the mouse appear to 
		//be at 2560 when in actuallity the mouse is at 1920
		
		screen.render(currentMouseXS ,currentMouseYS, 11, 15, 1, 1, 4, "icons");
	}
}
