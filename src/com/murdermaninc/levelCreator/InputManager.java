package com.murdermaninc.levelCreator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputManager implements MouseListener, KeyListener, MouseWheelListener{
	
	public boolean leftClick = false;
	public boolean middleClick = false;
	public boolean rightClick = false;
	public boolean controlZ = false;
	public boolean shift = false;
	public boolean s = false;
	public boolean c = false;
	public boolean m = false;
	public boolean plus = false;
	public boolean minus = false;
	public boolean n = false;
	
	public int menuDepiction = 0;
	
	private Main main;
	
	private char lastKey;
	
	public InputManager(Main main){
		main.addKeyListener(this);
		main.addMouseListener(this);
		main.addMouseWheelListener(this);
		
		this.main = main;

	}
	
	int escapeCounter = 0;
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S) s = true;
		if(e.getKeyCode() == KeyEvent.VK_C) c = true;
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) controlZ = true;
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) shift = true;
		if(e.getKeyCode() == KeyEvent.VK_L) plus = true;
		if(e.getKeyCode() == KeyEvent.VK_K) minus = true;
		if(e.getKeyCode() == KeyEvent.VK_N) n = true;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			escapeCounter++;
			if(escapeCounter == 2){
				System.exit(0);
			}
		}

	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S) s = false;
		if(e.getKeyCode() == KeyEvent.VK_C) c = false;
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) shift = false;
		if(e.getKeyCode() == KeyEvent.VK_L) plus = false;
		if(e.getKeyCode() == KeyEvent.VK_K) minus = false;
		if(e.getKeyCode() == KeyEvent.VK_N) n = false;
	}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == 'm' || e.getKeyChar() == 'M') m = !m;
		if(e.getKeyChar() == 'b' || e.getKeyChar() == 'B'){
			menuDepiction = 0;
			if(e.getKeyChar() == lastKey){
				main.menu.showing = !main.menu.showing;
			}else{
				main.menu.showing = true;
			}
			lastKey = e.getKeyChar();
		}
		if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D'){
			menuDepiction = 1;
			if(e.getKeyChar() == lastKey){
				main.menu.showing = !main.menu.showing;
			}else{
				main.menu.showing = true;
			}
			lastKey = e.getKeyChar();
		}
		if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A'){
			menuDepiction = 2;
			if(e.getKeyChar() == lastKey){
				main.menu.showing = !main.menu.showing;
			}else{
				main.menu.showing = true;
			}
			lastKey = e.getKeyChar();
		}
		if(e.getKeyChar() == 'u' || e.getKeyChar() == 'U'){
			menuDepiction = 3;
			if(e.getKeyChar() == lastKey){
				main.menu.showing = !main.menu.showing;
			}else{
				main.menu.showing = true;
			}
			lastKey = e.getKeyChar();
		}
		
	}

	public void mouseClicked(MouseEvent m) {
		
		
	}

	public void mouseEntered(MouseEvent m) {
		
	}

	public void mouseExited(MouseEvent m) {
		
	}

	public void mousePressed(MouseEvent m) {
		if(m.getButton() == MouseEvent.BUTTON1){
			if(main.menu.showing && main.menu.checkMouseCollision()){
				main.menu.mouseClickCollision();
			}else{
				leftClick = true;
			}
		}
		if(m.getButton() == MouseEvent.BUTTON2) middleClick = true;
		if(m.getButton() == MouseEvent.BUTTON3) rightClick = true;
	}

	public void mouseReleased(MouseEvent m) {
		if(m.getButton() == MouseEvent.BUTTON1 && shift){
			leftClick = false;
			main.fillShiftClick();
			main.storeData();
		}else if(m.getButton() == MouseEvent.BUTTON1){
			leftClick = false;
			main.storeData();
		}
		if(m.getButton() == MouseEvent.BUTTON2)    middleClick = false;
		if(m.getButton() == MouseEvent.BUTTON3){
			rightClick = false;
			main.storeData();
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		
		if(!main.menu.showing){
			main.editorId += -e.getWheelRotation();
		}else{
			if(menuDepiction != 1){
				main.menu.scrollAmount += e.getWheelRotation() * 5;
			}else{
				main.menu.scrollAmountDecorations += -e.getWheelRotation();
			}

		}
		
	}

}
