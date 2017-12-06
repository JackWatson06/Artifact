package com.murdermaninc.decorations;

import com.murdermaninc.graphics.Screen;

public class LumenFlower extends Decoration{

	
	int brightX = x - 20;
	int brightY = y - 44;
	
	int windowLX = 0;
	int windowRX = 0;
	int windowTY = 0;
	int windowBY = 0;
	
	private int[] brightenData; 
	
	public LumenFlower(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}
	
	
	@Override
	public void render(Screen screen, float interpolation){
		if(Data == null){
			Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");
		}
		
		screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
		
		renderLight(screen);
	}
	
	public void renderLight(Screen screen){

		if(brightenData == null) brightenData = screen.loadData(11, 3, 2, 2, 4, "icons");
		
		//float brightenAmount = 2;

		windowLX = screen.screenX;
		windowRX = screen.screenX + screen.width;
		windowTY = screen.screenY;
		windowBY = screen.screenY + screen.height;

		if(brightX + (2 * 64 - 1) >= windowLX && brightX < windowRX && brightY + (2 * 64 - 1) >= windowTY && brightY < windowBY){
			for(int y = 0; y < 128; y++){
				for(int x = 0; x < 128; x++){
					int currentColor = brightenData[x + y * 128];
					if(currentColor == -16777216) brighten(screen, brightX + x, brightY + y, 1.062F);
					if(currentColor == -16448251) brighten(screen, brightX + x, brightY + y, 1.125F);
					if(currentColor == -16119286) brighten(screen, brightX + x, brightY + y, 1.187F);
					if(currentColor == -15790321) brighten(screen, brightX + x, brightY + y, 1.250F);
					if(currentColor == -15461356) brighten(screen, brightX + x, brightY + y, 1.312F);
					if(currentColor == -15066598) brighten(screen, brightX + x, brightY + y, 1.375F);
					if(currentColor == -14737633) brighten(screen, brightX + x, brightY + y, 1.437F);
					if(currentColor == -14408668) brighten(screen, brightX + x, brightY + y, 1.500F);
					if(currentColor == -14079703) brighten(screen, brightX + x, brightY + y, 1.562F);
					if(currentColor == -13750738) brighten(screen, brightX + x, brightY + y, 1.625F);
					if(currentColor == -13421773) brighten(screen, brightX + x, brightY + y, 1.687F);
					if(currentColor == -13092808) brighten(screen, brightX + x, brightY + y, 1.750F);
					if(currentColor == -12763843) brighten(screen, brightX + x, brightY + y, 1.812F);
					if(currentColor == -12434878) brighten(screen, brightX + x, brightY + y, 1.875F);
					if(currentColor == -12105913) brighten(screen, brightX + x, brightY + y, 1.937F);
					if(currentColor == -11711155) brighten(screen, brightX + x, brightY + y, 2.000F);

				}
			}
		}

		
	}

	public void brighten(Screen screen, int x, int y, float brightness){

		x -= screen.screenX;
		y -= screen.screenY;

		if(x >= windowLX - screen.screenX && x < windowRX - screen.screenX && y >= windowTY - screen.screenY && y < windowBY - screen.screenY){

			int color = screen.pixels[x + y * screen.width];

			//System.out.println(brightness);
			int r = (color >>> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = (color) & 0xFF;

			r =  Math.min((int) (r * brightness), 255);
			g =  Math.min((int) (g * brightness), 255);
			b =  Math.min((int) (b * brightness), 255);


			screen.pixels[x + y * screen.width] = (-1 << 24) | (r << 16) | (g << 8) | (b);
		}
	}

}

/*
		for(int i = 0; i < radius; i++){
			for(int equation = 0; equation < 2; equation++){
				if(equation == 0){
					for(int diameter = -i; diameter < i; diameter++){
						double equationX = Math.sqrt(Math.pow(i, 2) - Math.pow((y + diameter) - y, 2)) + x;
						equationX -= screen.screenX;
						int equationY = y + diameter - screen.screenY;
						//System.out.println(equationX);
						//System.out.println(y + diameter);
						int color = screen.pixels[(int) (equationX) + (equationY * screen.width)];
						screen.pixels[(int) (equationX) + (equationY * screen.width)] =  ((((color & 0xFF0000) * 2) & 0xFF0000) | (((color & 0xFF00) * 2) & 0xFF00) | (((color) & 0xFF) * 2));;
					}
				}else{
					for(int diameter = -i; diameter < i; diameter++){
						double equationX = -Math.sqrt(Math.pow(i, 2) - Math.pow((y + diameter) - y, 2)) + x;
						equationX -= screen.screenX;
						int equationY = y + diameter - screen.screenY;
						//System.out.println(equationX);
						//System.out.println(y + diameter);
						int color = screen.pixels[(int) (equationX) + (equationY * screen.width)];
						screen.pixels[(int) (equationX) + (equationY * screen.width)] =  ((((color & 0xFF0000) * 2) & 0xFF0000) | (((color & 0xFF00) * 2) & 0xFF00) | (((color) & 0xFF) * 2));;
					}
				}
			}
		}*/
