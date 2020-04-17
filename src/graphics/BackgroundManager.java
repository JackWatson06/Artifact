package com.murdermaninc.graphics;

import java.util.ArrayList;

import com.murdermaninc.decorations.background.*;
import com.murdermaninc.level.Level;
import com.murdermaninc.main.Benchmark;

public class BackgroundManager {

	private Background currentBackground;
	private int currentLevel;
	private int currentWorld;
	private int height;
	private Level level;
	
	//TODO Do a double check of this class to make sure I am not doing anything to stupid
	//TODO Possibly move this to the level package
	
	//This keeps track of the current background decorations that are in the total level not the ones that are showing.
	public ArrayList<DecorationsBackground> decorationsBackground = new ArrayList<DecorationsBackground>();
	
	//This is the list of all of the animated backgrounds one for each object that is animated(try to keep a minimum of four)
	
	//This keeps track of an id for objects
	private int objectOneId = 0;
	
	private int objectOneCap = 0;
	private int objectTwoCap = 0;
	
	private boolean deletedObject = false;
	
	
	public BackgroundManager(int level, int worldNumber, int width, int height,Screen screen, Level currentLevel){
		
		this.height = height;
		this.currentWorld = worldNumber;
		this.level = currentLevel;
		
		
		//This is the set of background instructions that are going to be hardcoded into the program and background objects that stay there singularly
		//Object number refers to the objects that spawn in the level if there is an object that only spawns on the setup then it gets a 0 for this argument otherwise all spawned obejcts get a 1, 2, etc.
		
		//Level 1-4
		if(worldNumber == 1){
			if(level >= 0 && level <= 4){

				this.currentLevel = level;

				screen.loadSpriteSheet("/1.1-4 Background SpriteSheet.png", "background");

				currentBackground = new Background("/1.1-4 Background.png", true);
				currentBackground.scaleImage(4, width, height);


				//This calculates the number of hills need for the current level based off of the level width and the width of the actual sprite.
				int numberOfHills = (int) Math.ceil( currentLevel.levelWidth / 40.0);

				for(int i = 0; i < numberOfHills; i++){
					decorationsBackground.add(new Hills(-1, 0, i * 40 * 64, (currentLevel.levelHeight * 64) - (4 * 64), 40, 4, 0, 5));
				}
			}else if(level > 4 && level <= 8){

				this.currentLevel = level;

				screen.loadSpriteSheet("/1.5-8 Background SpriteSheet.png", "background");

				currentBackground = new Background("/1.5-8 Background.png", true);
				currentBackground.scaleImage(4, width, height);

				//This calculates the number of hills need for the current level based off of the level width and the width of the actual sprite.
				int numberOfHills = (int) Math.ceil( currentLevel.levelWidth / 40.0);

				for(int i = 0; i < numberOfHills; i++){
					decorationsBackground.add(new Hills(-1, 0, i * 40 * 64, (currentLevel.levelHeight * 64) - (4 * 64), 40, 4, 0, 5));
				}

			}else if(level > 8 && level<= 12){
				this.currentLevel = level;

				screen.loadSpriteSheet("/1.9-12 Background SpriteSheet.png", "background");

				currentBackground = new Background("/1.9-12 Background.png", false);	

				//This calculates the number of hills need for the current level based off of the level width and the width of the actual sprite.
				int numberOfHills = (int) Math.ceil( currentLevel.levelWidth / 40.0);

				for(int i = 0; i < numberOfHills; i++){
					decorationsBackground.add(new Hills(-1, 0, i * 40 * 64, (currentLevel.levelHeight * 64) - (4 * 64), 40, 4, 0, 5));
				}
			}else if(level > 12 && level<= 16){
				this.currentLevel = level;

				screen.loadSpriteSheet("/1.13-16 Background SpriteSheet.png", "background");

				currentBackground = new Background("/1.13-16 Background.png", false);	

				//This calculates the number of hills need for the current level based off of the level width and the width of the actual sprite.
				int numberOfHills = (int) Math.ceil( currentLevel.levelWidth / 40.0);

				for(int i = 0; i < numberOfHills; i++){
					decorationsBackground.add(new Hills(-1, 0, i * 40 * 64, (currentLevel.levelHeight * 64) - (4 * 64), 40, 4, 0, 5));
				}
			}
		
		
		
		
		
		}else if(worldNumber == 2){
			if(level >= 0 && level <= 4){
				
				screen.loadSpriteSheet("/2.1-4 Background SpriteSheet.png", "background");
				
				currentBackground = new Background("/2.1-4 Background.png", true);
				//currentBackground = new Background("/1.1-4 Background.png", true);
				currentBackground.scrolling = true;
				currentBackground.scaleImage(4);
				
				//This is the amount of trees per layer and there are three layers
				int amountOfTrees = (int) Math.round(currentLevel.levelWidth / 30.0F); // 25.0F
				//float[] xScroll = {1.1F, 1.25F, 1.42F};
				float[] xScroll = {1.25F, 1.42F};
				int treeDeviation = 300;
				int minimumDistance = 100;
				//zero is negative, 1 is positive.
				int[] reverseSign = new int[amountOfTrees];
				
				
				for(int layers = 0; layers < 2; layers++){
					for(int amount = 0; amount < amountOfTrees; amount++){
						int tree = (int) (Math.random() * 3);

						int xPosition = (int) (currentLevel.levelWidth * 64 - ((currentLevel.levelWidth * 64  - width) / xScroll[layers]));

						int sectors = xPosition / (amountOfTrees);
						
						
						
						if(layers == 0){
							
							int positiveOrNegative = (int) (Math.random() * 2);
							
							if(positiveOrNegative == 0){
								
								xPosition = (int) ((sectors * (amount)) - (Math.random() * treeDeviation + minimumDistance));	
								reverseSign[amount] = 0;
								
								//System.out.println("Increase 1: " + (int) ((sectors * (amount)) - (Math.random() * treeDeviation)));
								//System.out.println("Increase 1: " + (int) ((sectors * (amount)) - (Math.random() * 0)));
								
								//System.out.println("Negative");
								
							}else{
								
								xPosition = (int) ((sectors * (amount)) + (Math.random() * treeDeviation + minimumDistance));	
								reverseSign[amount] = 1;
								
								//System.out.println("Positive");
								
							}
						}else{

							if(reverseSign[amount] == 0){
								xPosition = (int) ((sectors * (amount)) + (Math.random() * treeDeviation + minimumDistance));	
								//System.out.println(xPosition);

								
								//System.out.println("Positive");
							}else{
								xPosition = (int) ((sectors * (amount)) - (Math.random() * treeDeviation + minimumDistance));
								//System.out.println(xPosition);
								//System.out.println("Negative");
							}
						}
						
						//xPosition = (int) ((sectors * (amount)) + (Math.random() * treeDeviation));	
						
						
						decorationsBackground.add(new Tree(tree, 0, (int) xPosition,  currentLevel.levelHeight * 64 - height + ((2 - layers) * 10), 11, 18, 0, 0, xScroll[layers]));
					}
				}
			}
			
			this.currentLevel = level;
		}
		
		//System.out.println("ScreenX: " + (currentLevel.levelWidth * 64 - ((currentLevel.levelWidth * 64  - 1920) / 1.2)));
		
	}
	
	//TODO update clouds to new animation rendering with loading data(remember each time a new cloud is created I don't want to have to load the data again every time so find a work around)
	//Fix cloud deletion
	long timing = System.currentTimeMillis();
	boolean firsttime = true;
	
	public void tick(Screen screen, Level level){
		
		//This is located for firstTime spawns

		
		//1-12 level spawning
		//This spawns clouds for levels that require clouds
		if(currentWorld == 1){
			if(currentLevel >= 0 && currentLevel <= 16){

				if(firsttime){

					int numberOfClouds = (int) (Math.random() * 5) + 1; // 1-3
					int yDeviation = 50;


					for(int i = 0; i < numberOfClouds; i++){
						int largeOrSmall = (int) (Math.random() * 10) + 1; // 1-10
						if(largeOrSmall < 3 && objectOneCap <= 3){
							int negPos = (int) (Math.random() * 2);
							int spawnY = 0;
							if(negPos == 0){
								spawnY = (int) (540 - (Math.random() * yDeviation));
							}else{
								spawnY = (int) (540 + (Math.random() * yDeviation));
							}
							objectOneId++;
							decorationsBackground.add(new Cloud(objectOneId, 1, (int) (Math.random() * (level.levelWidth * 64)), spawnY + ((level.levelHeight * 64) - height), 9, 3, 0, 0, this));
							objectOneCap++;
						}else if(objectTwoCap <= 5){
							int negPos = (int) (Math.random() * 2);
							int spawnY = 0;
							if(negPos == 0){
								spawnY = (int) (540 - (Math.random() * yDeviation));
							}else{
								spawnY = (int) (540 + (Math.random() * yDeviation));
							}

							objectOneId++;
							decorationsBackground.add(new Cloud(objectOneId, 2, (int) (Math.random() * (level.levelWidth * 64)), spawnY + ((level.levelHeight * 64) - height) , 4, 2, 0, 3, this));
							objectTwoCap++;
						}
					}


					firsttime = false;
				}

				int yDeviation = 50;
				float randomChance = (float) Math.random() * 100F;
				if(randomChance < 0.009F && objectOneCap <= 2){ //0.0001%
					int negPos = (int) (Math.random() * 2);
					int spawnY = 0;
					if(negPos == 0){
						spawnY = (int) (540 - (Math.random() * yDeviation));
					}else{
						spawnY = (int) (540 + (Math.random() * yDeviation));
					}
					objectOneId++;
					decorationsBackground.add(new Cloud(objectOneId, 1, (int) (level.levelWidth * 64 - (screen.screenX / 1.5)), spawnY + ((level.levelHeight * 64) - height), 9, 3, 0, 0, this));
					objectOneCap++;
				}

				if(randomChance < 00.04F && objectTwoCap <= 5){ //0.0004%
					int negPos = (int) (Math.random() * 2);
					int spawnY = 0;
					if(negPos == 0){
						spawnY = (int) (540 - (Math.random() * yDeviation));
					}else{
						spawnY = (int) (540 + (Math.random() * yDeviation));
					}

					objectOneId++;
					decorationsBackground.add(new Cloud(objectOneId, 2,(int) (level.levelWidth * 64 - (screen.screenX / 1.5)), spawnY + ((level.levelHeight * 64) - height) , 4, 2, 0, 3, this));
					objectTwoCap++;
				}


			}
		}
		
		
		
		
		
		
		for(int i = 0; i < decorationsBackground.size(); i++){
			decorationsBackground.get(i).tick(screen, level);
			if(deletedObject){
				deletedObject = false;
				i--;
			}
		}
	}
	
	public void render(Screen screen, float interpolation){
		if(!currentBackground.scrolling){
			
			long renderBackground = System.nanoTime();
			
			for(int i = 0; i < currentBackground.pixels.length; i++){
				screen.pixels[i] = currentBackground.pixels[i];
			}
			
			Benchmark.addBenchmarkFloat("Render Background:", (System.nanoTime() - renderBackground) / 1_000_000F);
			
		}else{
			
			
			int backgroundWidth = currentBackground.scaledWidth;
			//The larger number means it moves slower which makes it appear to follow the player.
			int xOffset = (int) (screen.screenX / 11);	
			int yOffset = (currentBackground.scaledHeight - screen.height) - ((((level.levelHeight * 64) - screen.height) - screen.screenY) / 10);
			int screenWidth = screen.width;
			for(int y = 0; y < screen.height; y++){
				for(int x = 0; x < screen.width; x++){
					screen.pixels[x + y * screenWidth] = currentBackground.pixels[(x + xOffset) + (y + yOffset) * backgroundWidth];
				}
			}


		}
		
		long renderBackgroundDecorations = System.nanoTime();
		
		for(int i = 0; i < decorationsBackground.size(); i++){
			decorationsBackground.get(i).render(screen, interpolation);
		}
		
		Benchmark.addBenchmarkFloat("Render Background Decorations:", (System.nanoTime() - renderBackgroundDecorations) / 1_000_000F);
		
	}
	
	public void renderDecorations(Screen screen, float interpolation) {
		
		for(int i = 0; i < decorationsBackground.size(); i++){
			decorationsBackground.get(i).render(screen, interpolation);
		}
		
	}
	
	public void destroyObject(int id){
		
		for(int i = 0; i < decorationsBackground.size(); i++){
			if(decorationsBackground.get(i).id == id){
				if(decorationsBackground.get(i).objectNumber == 1){
					objectOneCap--;
				}else if(decorationsBackground.get(i).objectNumber == 2){
					objectTwoCap--;
				}
				deletedObject = true;
				decorationsBackground.remove(i);
				i--;
			}
		}
		
	}
	
}
