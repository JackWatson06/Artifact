
package com.murdermaninc.main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.ImageCapabilities;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.murdermaninc.entity.Player;
import com.murdermaninc.collectionBackground.CollectionBackground;
import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.BackgroundManager;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;
import com.murdermaninc.level.LevelSequencing;
import com.murdermaninc.pauseMenu.PauseMenu;



public class Main implements Runnable{
	
	
	
	
	Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final int screenWidth = (int) ScreenSize.getWidth();
    public final int screenHeight = (int) ScreenSize.getHeight();
	public final int width = 1920;
	public final int height = 1080;
	
	private boolean running = false;
	
	private Player player;
	private Level level;
	private InputManager input;
	private LevelSequencing levelSequence = new LevelSequencing(this);

	private static int tickCount = 0;
	
	private Screen screenTiles;
	//private Screen screenCollection;
	private Screen screenShip;
	//private Screen screenPause;
	private BackgroundManager backgroundManager;
	private CollectionBackground collectionBackground;
	private PauseMenu pauseMenuManager;
	public MainShipWorldManager mainShipManager;
	public String loadShip = "room";
	public int loadLevelNumber = 1;

	public StartData startData = new StartData();
	
	public boolean artifactMenu = false;
	public boolean mainShipWorld = false;
	public boolean pauseMenu = false;
	
	private Font font = new Font();
	private boolean printFPS = false;
	
	int ticks = 0;
	int frames = 0;
	boolean countFPS = false;
	
	public boolean skipRender = false;
	
	Frame mainFrame;
	
	ImageCapabilities vcap;

	public Main() throws InvocationTargetException, InterruptedException {
		
		java.awt.EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();

				try{


					GraphicsConfiguration gc = gd.getDefaultConfiguration();

					mainFrame = new Frame(gc);
					mainFrame.setUndecorated(true);
					mainFrame.setIgnoreRepaint(true);

					//set Cursor to blank
					BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

					Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

					mainFrame.setCursor(blankCursor);

					gd.setFullScreenWindow(mainFrame);
					if(gd.isDisplayChangeSupported()){
						chooseBestDisplayMode(gd);
					}

					mainFrame.createBufferStrategy(2);

				}catch(Exception e){

					gd.setFullScreenWindow(null);
				}

				System.out.println("Event Queue: " + EventQueue.isDispatchThread());
			}
		});
		
		new Thread(this).start();
		

	}

	public void run(){

		System.out.println("Event Queue: " + EventQueue.isDispatchThread());
		
		
		init();

		BufferStrategy bs = mainFrame.getBufferStrategy();

		running = true;


		int idealFPS = 60;
		long updatePerTick = 1000000000 / idealFPS;

		long lastTime = System.nanoTime();
		int lastSecondTime = (int) (lastTime / 1000000000);

		boolean shouldRender = false;

		int maxUpdatesPerTick = 0;

		int frames = 0;
		int ticks = 0;

		long lastFrame = System.nanoTime();

		while(running){

			long now = System.nanoTime();

			//frameTime = now - lastTime;

			while(now - lastTime > updatePerTick && maxUpdatesPerTick < 10){
				tick();
				lastTime += updatePerTick;
				maxUpdatesPerTick++;
				ticks++;
				shouldRender = true;
			}


			if(shouldRender){

				long renderCycle = System.nanoTime();

				maxUpdatesPerTick = 0;

				float interpolation = (float) (System.nanoTime() - lastFrame) / updatePerTick;
				lastFrame = System.nanoTime();

				long renderMethod = System.nanoTime();

				render(interpolation);

				Benchmark.addBenchmarkFloat("Entire Render Method:", (System.nanoTime() - renderMethod) / 1_000_000F);
				try {
					java.awt.EventQueue.invokeAndWait(new Runnable() {
						public void run() {
							Graphics2D g = (Graphics2D) bs.getDrawGraphics();

							if (!bs.contentsLost()) {
								if(input.test){
									Benchmark.calculateAndStoreAverage();
									System.exit(0);
								}
								if(!mainShipWorld){

									//drawing for the level

									//drawing for the mother ship
									if(screenWidth != 1920 || screenHeight != 1080) {

										long drawImage = System.nanoTime();

										g.drawImage(screenTiles.image, 0, 0, screenWidth, screenHeight, null);
										//g.drawImage(screenTiles.solidColors, 0, 0, screenWidth, screenHeight, null);
										//g.drawImage(screenTiles.image, (2560 - 1920) / 2, (1440 - 1080) / 2, null);

										Benchmark.addBenchmarkFloat("Draw Image:", (System.nanoTime() - drawImage) / 1_000_000F);



									}else {

										g.drawImage(screenTiles.solidColors, 0, 0, null);
										g.drawImage(screenTiles.image, 0, 0, null);

									}


								}else{

									//drawing for the mother ship
									if(screenWidth != 1920 || screenHeight != 1080) {

										g.drawImage(screenShip.image, 0, 0, screenWidth, screenHeight, null);

									}else {

										g.drawImage(screenShip.image, 0, 0, null);

									}

								}

								long bsShow = System.nanoTime();

								bs.show();

								Benchmark.addBenchmarkFloat("Bs.show:", (System.nanoTime() - bsShow) / 1_000_000F);

								//Log.write("++++++Buffer Lost Contents: " + Boolean.toString(bs.contentsLost()));


								g.dispose();

							}
						}
					});
				} catch (InvocationTargetException | InterruptedException e) {
					Log.write("Frame missed it's rendering!!!");
				}

				frames++;
				shouldRender = false;


				Benchmark.addBenchmarkFloat("Entire Render Cycle:", (System.nanoTime() - renderCycle) / 1_000_000F);

			}


			int thisSecond = (int) (lastTime / 1000000000);
			if (thisSecond > lastSecondTime)
			{

				System.out.println("Frames: " + frames + " Ticks: " + ticks);
				this.ticks = ticks;
				this.frames = frames;
				frames = 0;
				ticks = 0;
				countFPS = true;
				lastSecondTime = thisSecond;

			}


			while(now - lastTime < updatePerTick){

				Thread.yield();

				try {
					Thread.sleep(1);
				}catch(Exception e) {
					e.printStackTrace();
				} 

				now = System.nanoTime();
			}
		}
		

	}
	


	
	
	private void init(){

		String path = new String("Log");
		
		File file = new File(path);
		
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.flush();
			bw.close();
			
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		
		
		levelSequence.loadLevelSequence();
		input = new InputManager(mainFrame);
		
		loadShip = startData.startString;
		
		//This loads in the beginning to make sure the pause menu is always in memory for quick acsess when the game wants to be paused.
		
		if(pauseMenuManager == null){
			pauseMenuManager = new PauseMenu(width, height, input, this);
		}
		      

	}
	
	
	//TODO Fix issue where when the player switches direction while jumping then their automatic jumping will stop
	
	
	private void tick(){
		
		if(skipRender) skipRender = false;
		
		tickCount++;
		
		if(input.escape && !artifactMenu){
			pauseMenu = !pauseMenu;
			if(pauseMenu) pauseMenuManager.pauseButtonHit = true;
			pauseMenuManager.currentButton = 1;
			input.escape = false;
		}
		
                
		if(!artifactMenu && !mainShipWorld && !pauseMenu){

				//TICK FOR LEVEL
			
				if(input.r){
					input.r = false;
					loadLevel("Level2-1", 1, 2);
				}

				if(level == null){
					loadLevel("Level1-13", 13, 1);

				}

				
				if(player != null){
					player.tick(1.0F, tickCount);
				}

				if(level != null){
					level.tick(player);
				}


				//Scrolling for levels
				if(level != null){
					if(level.levelWidth != 30){
						screenTiles.screenX = (player.x + 32) - (width / 2);

						if(screenTiles.screenX < 0){
							int add = 0 - screenTiles.screenX;
							screenTiles.screenX += add;
						}

						if(screenTiles.screenX > (level.levelWidth * 64) - width){
							int subtract = screenTiles.screenX - ((level.levelWidth * 64) - width);
							screenTiles.screenX -= subtract;
						}
					}

					if(level.levelHeight != 17){
						screenTiles.screenY = (player.y + 32) - (height / 2);

						if(screenTiles.screenY < 0){
							int add = 0 - screenTiles.screenY;
							screenTiles.screenY += add;
						}

						if(screenTiles.screenY > (level.levelHeight * 64) - height){
							int subtract = screenTiles.screenY - ((level.levelHeight * 64) - height);
							screenTiles.screenY -= subtract;
						}
					}
				}

				if(backgroundManager != null){
					backgroundManager.tick(screenTiles, level);
				}
				
				if(level != null){
					level.touchingArtifact(player.x + 8, player.x + 63 - 8, player.y + 4, player.y + 63);
				}
				

			
		}else if(mainShipWorld && !artifactMenu && !pauseMenu){
			
			//TICK FOR MOTHERSHIP
			
			if(mainShipManager != null){
				mainShipManager.tick(input);
			}
			
			
			if(player != null){
				player.tick(1.0F, tickCount);
			}
			
			
			//Scrolling for the main ship corridor
			
			if(mainShipManager != null){
				if(mainShipManager.currentRoom.equals(new String("corridor"))){
					screenShip.screenX = (player.x + 32) - (width / 2);

					if(screenShip.screenX < 0){
						int add = 0 - screenShip.screenX;
						screenShip.screenX += add;
					}

					if(screenShip.screenX > mainShipManager.currentBackground.scaledWidth - width){
						int subtract = screenShip.screenX - (mainShipManager.currentBackground.scaledWidth - width);
						screenShip.screenX -= subtract;
					}
				}
			}
			
		}else if(!pauseMenu && artifactMenu && !mainShipWorld){
			
			
			//TICK FOR COLLECTION SCREEN
			
			if(collectionBackground != null){
				collectionBackground.tick();
			}
			
			//Maybe do something with this if it is a problem because when the level switches it will first render the level before the tick
			//because of boolean shifts that this triggers.
			
			if(input.enter){
			
				levelSequence.levelSequence(level);
				
			}
			

		}else if(pauseMenu && !artifactMenu){
			
			//TICK FOR PAUSE MENU
			
			if(pauseMenuManager != null){
				pauseMenuManager.tick();
			}
			
		}

		if(input.f){
			printFPS = !printFPS;
			input.f = false;
		}
		
	}
	
	//This array is meant to average the fps for 5 frames then this
	//will be used to determine the frame rate at which animations should run at for
	//lower fps systems.
	//int[] fpsCounting = new int[3];
	//int fpsTimer = 0;
	boolean animationUpdate = false;
	float constInterpolation = 1.0F;
	float deviation = 0.1F;
	int deviationCounter = 0;
	
	
	private void render(float interpolation){ 

		
		//System.out.println("Interpolation: " + interpolation);
		
		interpolation = (float) (Math.round(interpolation * 10.0) / 10.0);
		
		//System.out.println("Interpolation Rounded: " + interpolation);
		
		//Be wary of this becuase if 0.9 becomes the new constant
		//then all the animations will be running at 60 / 0.9
		
		/*if(interpolation != constInterpolation && !(interpolation >= constInterpolation - deviation && interpolation <= constInterpolation + deviation)) {
			
			//System.out.println("Deviated from standard");
			//Log.write("++++++Deviated from standard: " + Float.toString(interpolation));
			//This basically updates the animation frame rate if the intepolation deviates for more than 4 frames
			//because we only want to update it if it is not normal such as if the game runs at a slower fps
			//then this will cause the animation to speed up and setting the new standard of deviation.
			if(deviationCounter >= 3) {		
				//System.out.println("Update FPS");
				//Log.write("++++++Update to FPS: " + Integer.toString((int) (60 / interpolation)));
				Animation.lastGameFPS = Animation.gameFPS;
				Animation.gameFPS = (int) (60 / interpolation);
				Animation.updateCounter = true;
				animationUpdate = true;	
				deviationCounter = 0;
				constInterpolation = interpolation;
				//Log.write("++++++New Constant: " + Float.toString(constInterpolation));
			}else {
				deviationCounter++;
			}
		}else {
			deviationCounter = 0;
		}*/
		
		interpolation = 1.0F;
		
		if(skipRender) return;
		
		if(!artifactMenu && !mainShipWorld && !pauseMenu){		
			
			screenTiles.totalLoops = 0;
			
			//RENDER FOR LEVEL
			//long BackgroundRender = System.nanoTime();
			
			if(backgroundManager != null){
				backgroundManager.render(screenTiles, interpolation);
			}
			
			//Log.write("|||Background Render: " + ((System.nanoTime() - BackgroundRender) / 1_000_000F));
			
			long EverythingRender = System.nanoTime();
			
			if(player!=null) {
				player.renderBeforeAllDecorations(screenTiles, interpolation);
			}
		
			if(level != null){
				level.renderBeforePlayer(screenTiles, interpolation);
			}
			
			if(player != null){
				player.render(screenTiles, interpolation);
			}
			
			if(level != null){
				level.render(interpolation);
			}

			
			Benchmark.addBenchmarkFloat("Blocks, Dec, Enti:", (System.nanoTime() - EverythingRender) / 1_000_000F);
			
			
			Benchmark.addBenchmarkLong("Total Loops:", (long) screenTiles.totalLoops);

		}
		
		//TODO fix studder when the collection screen is first loaded
		if(!pauseMenu && artifactMenu && !mainShipWorld){
			
			//RENDER FOR COLLECTION SCREEN
			
			if(collectionBackground == null){
				collectionBackground = new CollectionBackground(width, height, (float) (level.endTime - level.levelTimer) / 1000, levelSequence.getTime(level.levelNumber, level.worldNumber), level.totalAmountOfSub, level.subArtifactsXYTiles, level.completedSubArtifact, level.currentArtifact(), level.name, levelSequence);
			}
			
			
			
			collectionBackground.render(screenTiles, interpolation);

			
			
		}
		if(!artifactMenu && mainShipWorld && !pauseMenu){
			
			//RENDER FOR MAIN SHIP
			
			if(mainShipManager == null){
				System.out.println("Testing2");
				mainShipManager = new MainShipWorldManager(this, loadShip, loadLevelNumber, input, width, height);
				screenShip = mainShipManager.getScreen();
			}
			
			System.out.println("Render Ship");
			
			mainShipManager.render(screenShip, interpolation);
			
			if(player != null){
				player.render(screenShip, interpolation);
			}
			
			
		}
		
		if(pauseMenu && !artifactMenu){
		
			if(!mainShipWorld) {
				pauseMenuManager.render(screenTiles);
			}else {
				pauseMenuManager.render(screenShip);
			}
			
		}
		
		if(printFPS){
			
			if(getScreen() != null){
				
				font.drawText(getScreen(), "Frames: " + frames + " Ticks: " + ticks, 0 + getScreen().screenX, 0 + getScreen().screenY, 4);

				
			}
			
		}
		if(animationUpdate) {
			Animation.updateCounter = false; 
			animationUpdate = false;
		}
        
		
	}
	
	public void loadLevel(String loadLevel, int levelNumber, int worldNumber){
		collectionBackground = null;
		mainShipManager = null;
		screenShip = null;
		artifactMenu = false;
		mainShipWorld = false;
	
		level = new Level(loadLevel, levelNumber, worldNumber, width, height, levelSequence);
		screenTiles = level.getScreen();
		player = level.setPlayer(input);
		player.noControl = true;
		skipRender = true;

		backgroundManager = new BackgroundManager(levelNumber, worldNumber, width, height, screenTiles, level);
		
	}
	
	public void reset() {
		collectionBackground = null;
		mainShipManager = null;
		screenShip = null;
		artifactMenu = false;
		mainShipWorld = false;
	}
	
	public int[] getCurrentWorldAndLevel(){
		return new int[] {level.worldNumber, level.levelNumber};
	}
	
	private Screen getScreen(){
		if(screenTiles != null) return screenTiles;
		if(screenShip != null) return screenShip;

		return null;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public void removeSpriteSheets(){
		
		//This removes the current level sprite sheets after a artifact pickup as it is easier to acsess these
		//variables in the main class and to remove them from there and it also prepares the new screens for 
		//more sprite sheets.
		screenTiles.removeSpriteSheets();
	}
	
	public static int getTicks(){
		return tickCount;
	}
	
	
	//Sets the display mode !!!REMEMBER TO ADD MORE!!!
	private DisplayMode[] bestDisplayModes = {
			new DisplayMode(2560, 1440, 32, 0), 
			new DisplayMode(1920, 1080, 32, 0), 
			new DisplayMode(1600, 900, 32, 0), 
			new DisplayMode(1366, 768, 32, 0) };

	private DisplayMode getBestDisplayMode(GraphicsDevice gd)
	{
		for (int i = 0; i < bestDisplayModes.length; i++)
		{
			DisplayMode[] modes = gd.getDisplayModes();
			for (int j = 0; j < modes.length; j++) {
				if ((modes[j].getHeight() == bestDisplayModes[i].getHeight()) && 
						(modes[j].getWidth() == bestDisplayModes[i].getWidth()) && 
						(modes[j].getBitDepth() == bestDisplayModes[i].getBitDepth())) {
					return bestDisplayModes[i];
				}
			}
		}
		return null;
	}

	private void chooseBestDisplayMode(GraphicsDevice gd)
	{
		DisplayMode display = getBestDisplayMode(gd);
		if (display != null) {
			gd.setDisplayMode(display);
		} else {
			gd.setDisplayMode(gd.getDisplayMode());
		}
	}

	public static void main(String[] args)
	{
		
		System.out.println("Event Queue: " + EventQueue.isDispatchThread());
		
		try {
			new Main();
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}

