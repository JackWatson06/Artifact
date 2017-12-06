package com.murdermaninc.decorations;

import com.mudermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;

public class VerticalClimableVine extends Decoration{

	private boolean playerOn = false;
	
	int windowLX = 0;
	int windowRX = 0;
	int windowTY = 0;
	int windowBY = 0;
	
	int maxA = 210;
	int maxB = ((4 * 114) + 50);
	int a = 0;
	float b = (float) (maxB);
	float raiseHeight = 0.5F;
	//113

	int vineMaxSpeed = 10;
	int vineMoveSpeed = 0;
	
	private Player player;
	
	public VerticalClimableVine(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}
	
	@Override
	public void tick(Player player) {
		if(this.player == null) this.player = player;
		if(player.x + 63 - 8 >= x && player.x + 8 <= x + 63 && player.y + 63 >= y && player.y + 4 <= y + (spriteHeight * 64 - 1) && player.input.e && !player.isClimbingVertical){
			player.input.e = false;
			playerOn = true;
			player.isClimbingVertical = true;
			player.jump = false;
			player.gravity = false;
			
			a = 0;
			b = (float) maxB;
			vineMoveSpeed = 0;
			
			
		}
		
		if(player.input.e && player.isClimbingVertical){
			player.input.e = false;
			player.isClimbingVertical = false;
			playerOn = false;
			player.jumpCount = 1;
		}
		
		vineMoveSpeed = (int) Math.ceil(vineCalcA * (Math.pow(a, 2)) + vineMaxSpeed);
		if(vineMoveSpeed == 0)  vineMoveSpeed = 1;
		
		/*if(a >= maxA) {
			subOrAdd = false;
		}else if(a <= -maxA) {
			subOrAdd = true;
		}

		if(subOrAdd) {
			a+=vineMoveSpeed;
			if(a <= 0) {
				//b+= raiseHeight;
			}else {
				//b-= raiseHeight;
			}
		}else {
			a-=vineMoveSpeed;
			if(a >= 0) {
				//b+= raiseHeight;
			}else {
				//b-= raiseHeight;
			}
		}*/
		
		
		if(player.input.a && a <= maxA) {
			a+=vineMoveSpeed;
		}
		
		if(player.input.d && a >= -maxA) {
			a-=vineMoveSpeed;
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation) {
		if(Data == null) Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");
		
		if(!playerOn) {
			screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		}else {
			
			windowLX = screen.screenX;
			windowRX = screen.screenX + screen.width;
			windowTY = screen.screenY;
			windowBY = screen.screenY + screen.height;
			
			if(x + (2 * 64 - 1) >= windowLX && x < windowRX && y + (2 * 64 - 1) >= windowTY && y < windowBY){
				renderVine(screen);
			}
		}
		
	}
	


	
	float vineCalcA = (float) ((-vineMaxSpeed) / Math.pow(-maxA, 2));
	
	boolean subOrAdd = false;

	public void renderVine(Screen screen){


		int vineVisiblity = (int) ((b - 50) / 4);

		if(vineVisiblity > ((maxB - 50) / 4)) {
			vineVisiblity = ((maxB - 50) / 4);
		}
		
		


		for(int y = 0; y < vineVisiblity; y++) {
			int scaledX = 0;
			if(a >= 0) {
				scaledX = (int) (Math.sqrt(Math.pow(a, 2) - ((Math.pow(a, 2) / Math.pow(b, 2) * Math.pow((y * 4) + this.y, 2)))) + (this.x - a) + 28);
			}else {
				scaledX = (int) -(Math.sqrt(Math.pow(a, 2) - ((Math.pow(a, 2) / Math.pow(b, 2) * Math.pow((y * 4) + this.y, 2)))) - (this.x - a) - 28); //28 represents the offset from the side of the tile
			}
			
			System.out.println("Y: " + (y * 4 + this.y));
			System.out.println("Player: " + player.y);
			if((y * 4 + this.y) == player.y || (y * 4 + this.y + 1) == player.y || (y * 4 + this.y + 2) == player.y || (y * 4 + this.y + 3) == player.y) {
				System.out.println("Testing");
				player.x = scaledX;
			}
			
			scaledX -= screen.screenX;
			y -= screen.screenY;

			if(Data[(28 + ((y * 4) * 64))] == -14930161) {
				for(int dy = 0; dy < 4; dy++) {
					for(int dx = 0; dx < 4; dx++) {
						if(scaledX + dx >= windowLX - screen.screenX && scaledX + dx < windowRX - screen.screenX && dy + (y * 4) >= windowTY - screen.screenY && dy + (y * 4) < windowBY - screen.screenY){
							screen.pixels[(scaledX + dx) + (dy + (y * 4)) * (screen.width)] = -14930161;
						}
					}
					for(int dx = 0; dx < 4; dx++) {
						if((scaledX + 4) + dx >= windowLX - screen.screenX && (scaledX + 4) + dx < windowRX - screen.screenX && dy + (y * 4) >= windowTY - screen.screenY && dy + (y * 4) < windowBY - screen.screenY){
							screen.pixels[((scaledX + 4) + dx) + (dy + (y * 4)) * (screen.width)] = -14666223;
						}
					}
				}
			}else if(Data[(28 + ((y * 4) * 64))] == -14666223){
				for(int dy = 0; dy < 4; dy++) {
					for(int dx = 0; dx < 4; dx++) {
						if((scaledX - 4) + dx >= windowLX - screen.screenX && (scaledX - 4) + dx < windowRX - screen.screenX && dy + (y * 4) >= windowTY - screen.screenY && dy + (y * 4) < windowBY - screen.screenY){
							screen.pixels[((scaledX - 4) + dx) + (dy + (y * 4)) * (screen.width)] = -14930161;
						}
					}
					for(int dx = 0; dx < 4; dx++) {
						if((scaledX) + dx >= windowLX - screen.screenX && (scaledX) + dx < windowRX - screen.screenX && dy + (y * 4) >= windowTY - screen.screenY && dy + (y * 4) < windowBY - screen.screenY){
							screen.pixels[((scaledX) + dx) + (dy + (y * 4)) * (screen.width)] = -14666223;
						}
					}
				}
			}else {
				for(int dy = 0; dy < 4; dy++) {
					for(int dx = 0; dx < 4; dx++) {
						if((scaledX + 4) + dx >= windowLX - screen.screenX && (scaledX + 4) + dx < windowRX - screen.screenX && dy + (y * 4) >= windowTY - screen.screenY && dy + (y * 4) < windowBY - screen.screenY){
							screen.pixels[((scaledX + 4) + dx) + (dy + (y * 4)) * (screen.width)] = -14930161;
						}
					}
					for(int dx = 0; dx < 4; dx++) {
						if((scaledX + 8) + dx >= windowLX - screen.screenX && (scaledX + 8) + dx < windowRX - screen.screenX && dy + (y * 4) >= windowTY - screen.screenY && dy + (y * 4) < windowBY - screen.screenY){
							screen.pixels[((scaledX + 8) + dx) + (dy + (y * 4)) * (screen.width)] = -14666223;
						}
					}
				}
			}
		}

	}

}



