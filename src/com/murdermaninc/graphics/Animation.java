package com.murdermaninc.graphics;

import java.util.ArrayList;

import com.murdermaninc.entity.Player;
import com.murdermaninc.main.Log;

public class Animation {

	public static int gameFPS = 60;
	public static int lastGameFPS = 0;
	public static boolean updateCounter = false;
	
	public boolean random = false;
	public boolean continuous = false;
	public boolean timer = false;
	public boolean collision = false;
	public boolean once = true;
	
	public int animationCounter = 0;
	private int timerCounter = 0;
	private int fadeCounter = 0;
	private boolean reverseAnimation = false;
	private int currentSprite = 0;
	
	//TODO add a load data method for easy implementation of loading animation data into an arrayList
	
	public Animation(){
		
	}
	public int getCurrentSprite(){
		return currentSprite;
	}
	
	public void setCurrentSprite(int desiredSprite){
		this.currentSprite = desiredSprite;
	}

	
	public ArrayList<int[]> loadAnimationData(Screen screen, String spriteSheet, int scale, int spriteAmount, int xTile, int yTile, int spriteWidth, int spriteHeight){
		ArrayList<int[]> temporaryAnimation = new ArrayList<int[]>();	
		for(int i = 0; i < spriteAmount; i++){
			temporaryAnimation.add(screen.loadData(xTile + (i * spriteWidth), yTile, spriteWidth, spriteHeight, scale, spriteSheet));
		}
		return temporaryAnimation;
	}
	
	public ArrayList<int[]> loadAnimationDataRectangle(Screen screen, String spriteSheet, int scale, int spriteAmount, int xTile, int yTile, int spriteWidth, int spriteHeight, int spriteColumns, int spriteRows){
		ArrayList<int[]> temporaryAnimation = new ArrayList<int[]>();	
		

		for(int y = 0; y < spriteRows; y++){
			for(int x = 0; x < spriteColumns; x++){
				if(x + y * spriteColumns < spriteAmount){
					temporaryAnimation.add(screen.loadData((x * spriteWidth) + xTile, (y * spriteHeight) + yTile, spriteWidth, spriteHeight, scale, spriteSheet));
				}
			}
		}
		
		return temporaryAnimation;
	}
	
	public void resetCurrentAnimation(){
		animationCounter = 0;
		currentSprite = 0;
		timerCounter = 0;
		reverseAnimation = false;
		random = false;
		collision = false;
		continuous = false;
		timer = false;
		once = true;
	}
	
	public void fadeAnimation(Screen screen, int[] Data, int x, int y, int spriteWidth, int spriteHeight, int scale, float time, boolean fadOut, float fadeTimeOut, float timeOut, float interpolation) {
		
		int maxTime = (int) (time * 60);
		
		int fadeOutTime = (int) (fadeTimeOut * 60);
		
		int timeOutFade = (int) (timeOut * 60);
		
		if(fadeCounter < maxTime) {

			int multiplyScale = 16 * scale;

			int pixelWidth = spriteWidth * multiplyScale;


			for(int py = 0; py < spriteHeight * multiplyScale; py++) {
				for(int px = 0; px < pixelWidth; px++) {
					if(Data[px + py * pixelWidth] != -1086453) {

						int color1 = Data[px + py * pixelWidth];
						int color2 = screen.pixels[(x + px) + (y + py) * screen.width];

						int r1 = (color1 >>> 16) & 0xFF;
						int g1 = (color1 >> 8) & 0xFF;
						int b1 = (color1) & 0xFF;

						int r2 = (color2 >>> 16) & 0xFF;
						int g2 = (color2 >> 8) & 0xFF;
						int b2 = (color2) & 0xFF;

						float rat1 = (float) fadeCounter / (float) maxTime;
						float rat2 = 1 - ((float) fadeCounter / (float) maxTime);

						int r = (int) Math.min((r1 * rat1) + (r2 * rat2), 255);
						int g = (int) Math.min((g1 * rat1) + (g2 * rat2), 255);
						int b = (int) Math.min((b1 * rat1) + (b2 * rat2), 255);


						screen.pixels[(x + px) + (y + py) * screen.width] = (-1 << 24) | (r << 16) | (g << 8) | b;
					}
				}
			}

			fadeCounter+=(int) interpolation;
		}else if(fadeCounter >= fadeOutTime && fadOut){
			
			
			int multiplyScale = 16 * scale;

			int pixelWidth = spriteWidth * multiplyScale;


			for(int py = 0; py < spriteHeight * multiplyScale; py++) {
				for(int px = 0; px < pixelWidth; px++) {
					if(Data[px + py * pixelWidth] != -1086453) {

						int color1 = Data[px + py * pixelWidth];
						int color2 = screen.pixels[(x + px) + (y + py) * screen.width];

						int r1 = (color1 >>> 16) & 0xFF;
						int g1 = (color1 >> 8) & 0xFF;
						int b1 = (color1) & 0xFF;

						int r2 = (color2 >>> 16) & 0xFF;
						int g2 = (color2 >> 8) & 0xFF;
						int b2 = (color2) & 0xFF;

						float rat1 = (float) (fadeCounter - fadeOutTime) / (float) timeOutFade;
						float rat2 = 1 - ((float) (fadeCounter - fadeOutTime) / (float) timeOutFade);

						int r = (int) Math.min((r1 * rat2) + (r2 * rat1), 255);
						int g = (int) Math.min((g1 * rat2) + (g2 * rat1), 255);
						int b = (int) Math.min((b1 * rat2) + (b2 * rat1), 255);


						screen.pixels[(x + px) + (y + py) * screen.width] = (-1 << 24) | (r << 16) | (g << 8) | b;
					}
				}
			}

			fadeCounter+=(int) interpolation;

		}else {
			screen.renderData(Data, x, y, spriteWidth, spriteHeight, scale);
			
			if(fadOut) {
				fadeCounter+=(int) interpolation;
			}
		}
	}
	
	public void fadeAnimationArray(Screen screen, ArrayList<int[]> Data, int[] x, int[] y, int spriteWidth, int spriteHeight, int scale, float time, boolean fadOut, float fadeTimeOut, float timeOut, float interpolation) {
		
		int maxTime = (int) (time * 60);
		
		int fadeOutTime = (int) (fadeTimeOut * 60);
		
		int timeOutFade = (int) (timeOut * 60);
		
		if(fadeCounter < maxTime) {
			
			int multiplyScale = 16 * scale;

			int pixelWidth = spriteWidth * multiplyScale; 
			
			for(int i = 0; i < Data.size(); i++) {	
				for(int py = 0; py < spriteHeight * multiplyScale; py++) {
					for(int px = 0; px < pixelWidth; px++) {
						if(Data.get(i)[px + py * pixelWidth] != -1086453) {

							int color1 = Data.get(i)[px + py * pixelWidth];
							int color2 = screen.pixels[(x[i] + px) + (y[i] + py) * screen.width];

							int r1 = (color1 >>> 16) & 0xFF;
							int g1 = (color1 >> 8) & 0xFF;
							int b1 = (color1) & 0xFF;

							int r2 = (color2 >>> 16) & 0xFF;
							int g2 = (color2 >> 8) & 0xFF;
							int b2 = (color2) & 0xFF;

							float rat1 = (float) fadeCounter / (float) maxTime;
							float rat2 = 1 - ((float) fadeCounter / (float) maxTime);

							int r = (int) Math.min((r1 * rat1) + (r2 * rat2), 255);
							int g = (int) Math.min((g1 * rat1) + (g2 * rat2), 255);
							int b = (int) Math.min((b1 * rat1) + (b2 * rat2), 255);


							screen.pixels[(x[i] + px) + (y[i] + py) * screen.width] = (-1 << 24) | (r << 16) | (g << 8) | b;
						}
					}
				}
				
				
			}
			
			fadeCounter+=(int) interpolation;
			
		}else if(fadeCounter >= fadeOutTime && fadOut){
			
			int multiplyScale = 16 * scale;

			int pixelWidth = spriteWidth * multiplyScale; 
			
			for(int i = 0; i < Data.size(); i++) {	
				for(int py = 0; py < spriteHeight * multiplyScale; py++) {
					for(int px = 0; px < pixelWidth; px++) {
						if(Data.get(i)[px + py * pixelWidth] != -1086453) {

							int color1 = Data.get(i)[px + py * pixelWidth];
							int color2 = screen.pixels[(x[i] + px) + (y[i] + py) * screen.width];

							int r1 = (color1 >>> 16) & 0xFF;
							int g1 = (color1 >> 8) & 0xFF;
							int b1 = (color1) & 0xFF;

							int r2 = (color2 >>> 16) & 0xFF;
							int g2 = (color2 >> 8) & 0xFF;
							int b2 = (color2) & 0xFF;

							float rat1 = (float) (fadeCounter - fadeOutTime) / (float) timeOutFade;
							float rat2 = 1 - ((float) (fadeCounter - fadeOutTime) / (float) timeOutFade);

							int r = (int) Math.min((r1 * rat2) + (r2 * rat1), 255);
							int g = (int) Math.min((g1 * rat2) + (g2 * rat1), 255);
							int b = (int) Math.min((b1 * rat2) + (b2 * rat1), 255);


							screen.pixels[(x[i] + px) + (y[i] + py) * screen.width] = (-1 << 24) | (r << 16) | (g << 8) | b;
						}
					}
				}
				
				
			}
			
			fadeCounter+=(int) interpolation;
			
		}else {
			for(int i = 0; i < Data.size(); i++) {
				screen.renderData(Data.get(i), x[i], y[i], spriteWidth, spriteHeight, scale);
			}
			
			if(fadOut) {
				fadeCounter+=(int) interpolation;
			}
		}
	}
	
	
	//  10fps - 6update ticks     currentSprite 5 - 30 updateTicks                 33 / 6 updateTicks * 3 updateTicks  = 16.5
	//  20fps - 3update ticks     currentSprite 5 - 15 updateTicks
	
	
	
// 10fps 0-5 : 0-currentSprite,  6-11 : 1-currentSprite,  12-17 : 2-currentSprite,  18-23 : 3-currentSprite,  24-29 : 4-currentSprite,  30-35 : 5-currentSprite,  36-41 : 6-currentSprite, 42-47 : 7-currentSprite
// 15fps 0-3 : 0-currentSprite,  4-7 : 1-currentSprite,  8-11 : 2-currentSprite,  12-15 : 3-currentSprite,  16-19 : 4-currentSprite,  20-23 : 5-currentSprite,  24-27 : 6-currentSprite, 28-31 : 7-currentSprite
	// 30 / (60 / 10) * (60 / 15)
	// 30 / 6 * 4
	// 5 * 4
	
	//TODO There ARE problems with this
	public void updateContinuous(int lastFPS, int newFPS){
		
		int oldAnimationSpeed = (int) ((float)gameFPS / lastFPS);
		int newAnimationSpeed = (int) ((float)gameFPS / newFPS);
		
		animationCounter = Math.round(((float) animationCounter * newAnimationSpeed) / oldAnimationSpeed);
		
		/*
		//Testing purposes
		animationCounter = oldCounter;
		
		System.out.println("Old Counter: " + animationCounter);
		
		animationCounter = (int) Math.round((float)(animationCounter / (float) (gameFPS / lastFPS)) * (float)(gameFPS / newFPS));
		
		System.out.println("New Counter: " + animationCounter);*/
	}
	
	
	public void animateContinuous(Screen screen, ArrayList<int[]> Data, boolean reverse, float fps, int spriteWidth, int spriteHeight, int x, int y, int totalSprites, int scale, float interpolation){
		
		
		if(updateCounter) {
			
			int oldAnimationSpeed = (int) (lastGameFPS / fps);
			int newAnimationSpeed = (int) (gameFPS / fps);
			
			animationCounter = Math.round(((float) animationCounter * newAnimationSpeed) / oldAnimationSpeed);

		}
		
		if((!continuous || continuous) && !random && !timer && !collision){
			int animationSpeed = (int) (gameFPS / fps);
			
			//Prevents a divide by zero error which is caused by an extremely low gameFPS resulting in the rounding down to zero
			if(animationSpeed == 0) animationSpeed = 1;
			
			continuous = true;
			
			if(animationCounter % animationSpeed == 0 && animationCounter != 0 && animationCounter <= animationSpeed * totalSprites){
				if(reverse){
					int halfway = totalSprites / 2;
					if(currentSprite < halfway && !reverseAnimation){
						currentSprite++;
					}else if (reverseAnimation){
						currentSprite--;
					}
				
					if(currentSprite == halfway){
						reverseAnimation = true;
					}
				}else{
					if(currentSprite < totalSprites){
						currentSprite++;
					}
				}
			}
			
			
			
			//This is overflow protection (this should theoretically never be called)
			if(currentSprite >= Data.size() || currentSprite < 0) {
				
				Log.write("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later):  " + Integer.toString(currentSprite));
				currentSprite = 0;
				System.out.println("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later)");

			}
			
			screen.renderData(Data.get(currentSprite), x, y, spriteWidth, spriteHeight, scale);		
			
			animationCounter++;
			
			if(animationCounter >= animationSpeed * totalSprites){
				//System.out.println("AnimationReset: " + animationCounter);
				//System.out.println("CurrentSprite: " + currentSprite);
				//continuous = false;
				//System.out.println("Testing");
				animationCounter = 0;
				currentSprite = 0;
				reverseAnimation = false;
			}
		}
	}
	
	public void animateRandom(Screen screen, ArrayList<int[]> Data, boolean reverse, float fps, int spriteWidth, int spriteHeight, float threshold, int x, int y, int totalSprites, float interpolation){
		
		if(updateCounter) {
			
			int oldAnimationSpeed = (int) (lastGameFPS / fps);
			int newAnimationSpeed = (int) (gameFPS / fps);
			
			animationCounter = Math.round(((float) animationCounter * newAnimationSpeed) / oldAnimationSpeed);

		}
		
		if((Math.random() <= threshold || random) && !continuous && !timer && !collision){
			int animationSpeed = (int) (gameFPS / fps);
			
			//Prevents a divide by zero error which is caused by an extremely low gameFPS resulting in the rounding down to zero
			if(animationSpeed == 0) animationSpeed = 1;
			
			random = true;
			
			
			if(animationCounter % animationSpeed == 0 && animationCounter != 0){
				if(reverse){
					int halfway = totalSprites / 2;
					if(currentSprite < halfway && !reverseAnimation){
						currentSprite++;
					}else if (reverseAnimation){
						currentSprite--;
					}
				
					if(currentSprite == halfway){
						reverseAnimation = true;
					}
				}else{
					if(currentSprite < totalSprites){
						currentSprite++;
					}
				}
			}
			
			//This is overflow protection (this should theoretically never be called)
			if(currentSprite >= Data.size() || currentSprite < 0) {
				
				Log.write("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later):  " + Integer.toString(currentSprite));
				currentSprite = 0;
				System.out.println("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later)");

			}
			
			screen.renderData(Data.get(currentSprite), x, y, spriteWidth, spriteHeight, 4);		
			
			animationCounter++;
			
			if(animationCounter >= animationSpeed * totalSprites){
				random = false;
				animationCounter = 0;
				currentSprite = 0;
				reverseAnimation = false;
			}
		}
		
	}
	
	public void animateTimer(Screen screen, ArrayList<int[]> Data, boolean reverse, float fps, int spriteWidth, int spriteHeight, int time, int x, int y, int totalSprites, float interpolation){
		
		if(updateCounter) {
			
			int oldAnimationSpeed = (int) (lastGameFPS / fps);
			int newAnimationSpeed = (int) (gameFPS / fps);
			
			animationCounter = Math.round(((float) animationCounter * newAnimationSpeed) / oldAnimationSpeed);

		}
		
		if((timerCounter >= time || timer) && !continuous && !random && !collision){
			int animationSpeed = (int) (gameFPS / fps);
			
			//Prevents a divide by zero error which is caused by an extremely low gameFPS resulting in the rounding down to zero
			if(animationSpeed == 0) animationSpeed = 1;
			
			timer = true;
			
			
			if(animationCounter % animationSpeed == 0 && animationCounter != 0){
				if(reverse){
					int halfway = totalSprites / 2;
					if(currentSprite < halfway && !reverseAnimation){
						currentSprite++;
					}else if (reverseAnimation){
						currentSprite--;
					}
				
					if(currentSprite == halfway){
						reverseAnimation = true;
					}
				}else{
					if(currentSprite < totalSprites){
						currentSprite++;
					}
				}
			}
			
			//This is overflow protection (this should theoretically never be called)
			if(currentSprite >= Data.size() || currentSprite < 0) {
				
				Log.write("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later):  " + Integer.toString(currentSprite));
				currentSprite = 0;
				System.out.println("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later)");

			}
			
			screen.renderData(Data.get(currentSprite), x, y, spriteWidth, spriteHeight, 4);			
			
			animationCounter++;
			
			if(animationCounter >= animationSpeed * totalSprites){
				timer = false;
				animationCounter = 0;
				currentSprite = 0;
				reverseAnimation = false;
				timerCounter = 0;
			}
		}else{
			timerCounter++;
		}
	}
	
	public void animateCollision(Screen screen, Player player,ArrayList<int[]> Data, boolean reverse, float fps, int spriteWidth, int spriteHeight, int x, int y, int yCollisionOffsetU, int yCollisionOffsetB, int xCollisionOffsetL, int xCollisionOffsetR, int totalSprites, float interpolation){
		
		if(updateCounter) {
			
			int oldAnimationSpeed = (int) (lastGameFPS / fps);
			int newAnimationSpeed = (int) (gameFPS / fps);
			
			animationCounter = Math.round(((float) animationCounter * newAnimationSpeed) / oldAnimationSpeed);

		}
		
		if((!collision || collision) && !random && !timer && !continuous){
			int animationSpeed = (int) (gameFPS / fps);
			
			//Prevents a divide by zero error which is caused by an extremely low gameFPS resulting in the rounding down to zero
			if(animationSpeed == 0) animationSpeed = 1;
			
			int yCollisionU = y + yCollisionOffsetU;
			int yCollisionB = y + yCollisionOffsetB;
			int xCollisionL = x + xCollisionOffsetL;
			int xCollisionR = x + xCollisionOffsetR;
			
			collision = true;
			
			
			if(player.y + 4 > yCollisionU && player.y + 63 - 4 < yCollisionB && player.x + 8 < xCollisionR && player.x + 63 - 4 > xCollisionL){
				
				animationCounter++;
				
				if(animationCounter % animationSpeed == 0 && currentSprite < totalSprites){
					currentSprite++;
				}
			}else if(currentSprite > 0 && reverse){
				
				animationCounter+=(int) interpolation;
				
				if(animationCounter % animationSpeed == 0){
					currentSprite--;
				}
				
			}
			
			//This is overflow protection (this should theoretically never be called)
			if(currentSprite >= Data.size() || currentSprite < 0) {
				
				Log.write("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later):  " + Integer.toString(currentSprite));
				currentSprite = 0;
				System.out.println("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later)");

			}
			
			screen.renderData(Data.get(currentSprite), x, y, spriteWidth, spriteHeight, 4);	
			
			
			if(currentSprite == 0 && animationCounter >= fps){
				animationCounter = 0;
			}
		}
	}
	
	

	
	public void animateOnce(Screen screen, ArrayList<int[]> Data, boolean reverse, float fps, int spriteWidth, int spriteHeight, int x, int y, int totalSprites, int scale, float interpolation){
		
		if(updateCounter) {
			
			int oldAnimationSpeed = (int) (lastGameFPS / fps);
			int newAnimationSpeed = (int) (gameFPS / fps);
			
			animationCounter = Math.round(((float) animationCounter * newAnimationSpeed) / oldAnimationSpeed);

		}
		
		if(once && !continuous && !random && !timer && !collision){
			int animationSpeed = (int) (gameFPS / fps);
			
			//Prevents a divide by zero error which is caused by an extremely low gameFPS resulting in the rounding down to zero
			if(animationSpeed == 0) animationSpeed = 1;
			
			once = true;
			
			if(animationCounter % animationSpeed == 0 && animationCounter != 0){
				if(reverse){
					int halfway = totalSprites / 2;
					if(currentSprite < halfway && !reverseAnimation){
						currentSprite++;
					}else if (reverseAnimation){
						currentSprite--;
					}
				
					if(currentSprite == halfway){
						reverseAnimation = true;
					}
				}else{
					if(currentSprite < totalSprites){
						currentSprite++;
					}
				}
			}
			
			//This is overflow protection (this should theoretically never be called)
			if(currentSprite >= Data.size() || currentSprite < 0) {
				
				Log.write("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later):  " + Integer.toString(currentSprite));
				currentSprite = 0;
				System.out.println("|||||||!!!!!!! WARNING WARNING CURRENT SPRITE OVERLOAD !!!!!!!||||||||| (Will delete this message later)");

			}
			
			//System.out.println(currentSprite);
			screen.renderData(Data.get(currentSprite), x, y, spriteWidth, spriteHeight, scale);		
			
			animationCounter++;
			
			if(animationCounter >= animationSpeed * totalSprites){
				once = false;
				animationCounter = 0;
				currentSprite = 0;
				reverseAnimation = false;
			}
		}
	}
	
	
	/*
	 * This event caused the below error the spriteCounter got offtrack due to rounding down:
	 * AnimationCounterBefore: 25
	AnimationCounterExact: 16.666666
	Increase to 15 fps, decrease in numbers!
	AnimationCounterAfter: 16
	CurrentSpriteAfter: 4
	CurrentSprite: 5
	 * 
	 * 
	 *This gave me an error!
	 *
	 * AnimationCounterBefore: 38
	AnimationCounterExact: 25.333334
	Increase to 15 fps, decrease in numbers!
	AnimationCounterAfter: 25
	CurrentSpriteAfter: 7
	 */
	
}
