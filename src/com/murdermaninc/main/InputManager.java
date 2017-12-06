package com.murdermaninc.main;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener{

	public boolean w = false;
	public boolean a = false;
	public boolean s = false;
	public boolean d = false;
	public boolean e = false;
	public boolean f = false;
	public boolean r = false;

	public boolean shift = false;
	public boolean test = false;
	public boolean enter = false;
	public boolean escape = false;
	
	public boolean spaceBar = false;

	
	
	public InputManager(Frame mainFrame){
		mainFrame.addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent key) {
		
		if(key.getKeyCode() == KeyEvent.VK_W) w = true;
		if(key.getKeyCode() == KeyEvent.VK_UP) w = true;
		if(key.getKeyCode() == KeyEvent.VK_A) a = true;
		if(key.getKeyCode() == KeyEvent.VK_LEFT) a = true;
		if(key.getKeyCode() == KeyEvent.VK_S) s = true;
		if(key.getKeyCode() == KeyEvent.VK_DOWN) s = true;
		if(key.getKeyCode() == KeyEvent.VK_D) d = true;
		if(key.getKeyCode() == KeyEvent.VK_RIGHT) d = true;
		if(key.getKeyCode() == KeyEvent.VK_E) e = true;
		if(key.getKeyCode() == KeyEvent.VK_F) f = true;
		if(key.getKeyCode() == KeyEvent.VK_T) test = true;
		if(key.getKeyCode() == KeyEvent.VK_R) r = true;
		if(key.getKeyCode() == KeyEvent.VK_SHIFT) shift = true;
		if(key.getKeyCode() == KeyEvent.VK_ENTER) enter = true;
		if(key.getKeyCode() == KeyEvent.VK_ESCAPE)escape = true;
		
		
		if(key.getKeyChar() == KeyEvent.VK_SPACE) spaceBar = true;

	}


	public void keyReleased(KeyEvent key) {
		
		if(key.getKeyCode() == KeyEvent.VK_W) w = false;
		if(key.getKeyCode() == KeyEvent.VK_UP) w = false;
		if(key.getKeyCode() == KeyEvent.VK_A) a = false;
		if(key.getKeyCode() == KeyEvent.VK_LEFT) a = false;
		if(key.getKeyCode() == KeyEvent.VK_S) s = false;
		if(key.getKeyCode() == KeyEvent.VK_DOWN) s = false;
		if(key.getKeyCode() == KeyEvent.VK_D) d = false;
		if(key.getKeyCode() == KeyEvent.VK_RIGHT) d = false;
		if(key.getKeyCode() == KeyEvent.VK_E) e = false;
		if(key.getKeyCode() == KeyEvent.VK_F) f = false;
		if(key.getKeyCode() == KeyEvent.VK_T) test = false;
		if(key.getKeyCode() == KeyEvent.VK_R) r = false;
		if(key.getKeyCode() == KeyEvent.VK_ESCAPE) escape = false;
		if(key.getKeyCode() == KeyEvent.VK_SHIFT) shift = false;
		if(key.getKeyCode() == KeyEvent.VK_ENTER) enter = false;
		if(key.getKeyChar() == KeyEvent.VK_SPACE) spaceBar = false;

		
	}


	public void keyTyped(KeyEvent arg0) {
	
		
	}
	

	
	/*public void extendJump(int ticks){
		totalJumpTick = ticks;
		jumpTick++;
		jumping = true;
		if(Main.getTicks() >= lastTick + ticks){
			jump = false;
			jumping = false;
			jumpTick = 0;
		}	
	}
	
	public void resetJump(){
		jump = false;
		jumping = false;
		jumpTick = 0;
	}*/
	

}
