package com.murdermaninc.main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.mudermaninc.entity.Player;
import com.murdermaninc.collectionBackground.CollectionBackground;
import com.murdermaninc.graphics.BackgroundManager;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.Level;
import com.murdermaninc.level.LevelSequencing;
import com.murdermaninc.pauseMenu.PauseMenu;



public class Main{
	
	
	/*
	 * 
	 * FINAL FPS GOAL: 100 FPS MINIMUM!!!
	 * 
	 * 
	 */
	
	
	Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final int screenWidth = (int) ScreenSize.getWidth();
    public final int screenHeight = (int) ScreenSize.getHeight();
	public final int width = 1920;
	public final int height = 1080;
	
    private boolean scaledDown;
	public BufferedImage screenScaled;
	public int[] screenScaledPixels;
	public BufferedImage screenScaledTransparent;
	public int[] screenScaledTransparentPixels;
	
	private boolean running = false;
	
	private Player player;
	private Level level;
	private InputManager input;
	private LevelSequencing levelSequence = new LevelSequencing(this);

	private static int tickCount = 0;
	
	private Screen screenTiles;
	private Screen screenCollection;
	private Screen screenShip;
	private Screen screenPause;
	private BackgroundManager backgroundManager;
	private CollectionBackground collectionBackground;
	private PauseMenu pauseMenuManager;
	public MainShipWorldManager mainShipManager;
	public String loadShip = "room";

	public StartData startData = new StartData();
	
	public boolean artifactMenu = false;
	public boolean mainShipWorld = true;
	public boolean pauseMenu = false;
	
	private Font font = new Font();
	private boolean printFPS = false;
	
	int ticks = 0;
	int frames = 0;
	
	Frame mainFrame;
	
	public Main(GraphicsDevice gd){
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
			BufferStrategy bs = mainFrame.getBufferStrategy();
			
			running = true;
			
			init();
			
			System.setProperty("-Dsun.java2d.opengl", "true");
			System.setProperty("-Dsun.java2d.d3d", "false");


			int idealFPS = 60;
			long updatePerTick = 1000000000 / idealFPS;

			long lastTime = System.nanoTime();
			int lastSecondTime = (int) (lastTime / 1000000000);

			boolean shouldRender = false;
			
			int maxUpdatesPerTick = 0;

			int frames = 0;
			int ticks = 0;
			
			long frameTime = 0;

			while(running){

				long now = System.nanoTime();
				
				frameTime = now - lastTime;

				while(now - lastTime > updatePerTick && maxUpdatesPerTick < 10){
					tick();
					lastTime += updatePerTick;
					maxUpdatesPerTick++;
					ticks++;
					shouldRender = true;
				}


				if(shouldRender){
					maxUpdatesPerTick = 0;
					//System.out.println(Math.floor((double) frameTime / updatePerTick));
					float interpolation = Math.min(1.0f, (float) ((now - lastTime) / (float) updatePerTick) );
					render((float) Math.floor((double) frameTime / updatePerTick), interpolation);
					Graphics g = bs.getDrawGraphics();
					if (!bs.contentsLost()) {
						if(input.test){
							idealFPS = 1;
							updatePerTick = 1000000000 / idealFPS;
						}
						if(!artifactMenu && !mainShipWorld && !pauseMenu){
							
							//drawing for the level
							if(!scaledDown) {
								g.drawImage(screenTiles.image, 0, 0, width, height, null);
							}else {
								g.drawImage(screenScaled, 0, 0, screenWidth, screenHeight, null);
							}

						}else if(artifactMenu && !mainShipWorld && !pauseMenu){
							
							//drawing for the collection screen
							
							if(!scaledDown) {
								g.drawImage(screenTiles.image, 0, 0, width, height, null);
								g.drawImage(screenCollection.image, 0, 0, width, height, null);
							}else {
								g.drawImage(screenScaled, 0, 0, screenWidth, screenHeight, null);
								g.drawImage(screenScaledTransparent, 0, 0, screenWidth, screenHeight, null);
							}

						}else if(!artifactMenu && mainShipWorld && !pauseMenu){
							
							//drawing for the mother ship
							if(!scaledDown) {
								g.drawImage(screenShip.image, 0, 0, width, height, null);
							}else {
								g.drawImage(screenScaled, 0, 0, screenWidth, screenHeight, null);
							}
							
						}else if(pauseMenu && !artifactMenu && mainShipWorld){
							
							//drawing for the mother ship with pause menu
							
							if(!scaledDown) {
								g.drawImage(screenShip.image, 0, 0, width, height, null);
								g.drawImage(screenPause.image, 0, 0, width, height, null);
							}else {
								g.drawImage(screenScaled, 0, 0, screenWidth, screenHeight, null);
								g.drawImage(screenScaledTransparent, 0, 0, screenWidth, screenHeight, null);
							}
							
						}else if(pauseMenu && !artifactMenu && !mainShipWorld){
							
							//drawing for the level with pause menu
							
							if(!scaledDown) {
								g.drawImage(screenTiles.image, 0, 0, width, height, null);
								g.drawImage(screenPause.image, 0, 0, width, height, null);
							}else {
								g.drawImage(screenScaled, 0, 0, screenWidth, screenHeight, null);
								g.drawImage(screenScaledTransparent, 0, 0, screenWidth, screenHeight, null);
							}
							
						}
						bs.show();
						g.dispose();
						
					}
					frames++;
					shouldRender = false;
					
				}


				int thisSecond = (int) (lastTime / 1000000000);
				if (thisSecond > lastSecondTime)
				{

					System.out.println("Frames: " + frames + " Ticks: " + ticks);
					this.ticks = ticks;
					this.frames = frames;
					frames = 0;
					ticks = 0;
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

		}finally{
			gd.setFullScreenWindow(null);
		}

	}
	
	
	
	private void init(){

    	if(screenWidth != width) {
    		System.out.println("Scaled Down: ");
    		scaledDown = true;
    		screenScaled = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
    		screenScaledPixels = ((DataBufferInt) screenScaled.getRaster().getDataBuffer()).getData();
    		screenScaledTransparent = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
    		screenScaledTransparentPixels = ((DataBufferInt) screenScaledTransparent.getRaster().getDataBuffer()).getData();
    	}
		
		levelSequence.loadLevelSequence();
		input = new InputManager(mainFrame);
		
		loadShip = startData.startString;

	}
	
	
	//TODO Fix issue where when the player switches direction while jumping then their automatic jumping will stop
	
	
	private void tick(){
		
		tickCount++;
		
		if(input.escape && !artifactMenu){
			pauseMenu = !pauseMenu;
			pauseMenuManager.currentButton = 1;
			input.escape = false;
		}
		
		//This loads in the beginning to make sure the pause menu is always in memory for quick acsess when the game wants to be paused.
		
		if(pauseMenuManager == null){
			pauseMenuManager = new PauseMenu(width, height, input, this);
			screenPause = pauseMenuManager.getScreen();
		}
		                      
		if(!artifactMenu && !mainShipWorld && !pauseMenu){

				//TICK FOR LEVEL
			
				if(input.r){
					input.r = false;
					loadLevel("Level2-1", 1, 2);
				}

				if(level == null){
					loadLevel("Level2-1", 1, 2);

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
	
	private void render(float interpolation, float testInterpolation){ 
		
		//if(interpolation != 1.0F) {
			//System.out.println("Printing Interpolation: " + interpolation);
	//	}
		
		//System.out.println(testInterpolation);
		
		if(!artifactMenu && !mainShipWorld && !pauseMenu){		
			
			//RENDER FOR LEVEL
			
			if(backgroundManager != null){
				backgroundManager.render(screenTiles, interpolation);
			}
			
			if(player!=null) {
				player.renderBeforeAllDecorations(screenTiles);
			}
		
			if(level != null){
				level.renderBeforePlayer(screenTiles, interpolation);
			}
			
			if(player != null){
				player.render(screenTiles, interpolation, testInterpolation);
			}
			
			if(level != null){
				level.render(interpolation, testInterpolation);
			}

		}
		
		//TODO fix studder when the collection screen is first loaded
		if(!pauseMenu && artifactMenu && !mainShipWorld){
			
			//RENDER FOR COLLECTION SCREEN
			
			if(collectionBackground == null){
				collectionBackground = new CollectionBackground(width, height, (float) (level.endTime - level.levelTimer) / 1000, levelSequence.getTime(level.levelNumber, level.worldNumber), level.totalAmountOfSub, level.subArtifactsXYTiles, level.completedSubArtifact, level.currentArtifact(), level.name, levelSequence);
				screenCollection = collectionBackground.getScreen();
				
			}
			
			collectionBackground.render(screenCollection, interpolation);
			
			
		}
		if(!artifactMenu && mainShipWorld && !pauseMenu){
			
			//RENDER FOR MAIN SHIP
			
			if(mainShipManager == null){
				mainShipManager = new MainShipWorldManager(this, loadShip, input, width, height);
				screenShip = mainShipManager.getScreen();
			}
			
			
			mainShipManager.render(screenShip, interpolation);
			
			if(player != null){
				player.render(screenShip, interpolation, testInterpolation);
			}
			
			
		}
		
		if(pauseMenu && !artifactMenu){
		
			
			pauseMenuManager.render();
			
		}
		
		if(printFPS){
			
			if(getScreen() != null){
				
				font.drawText(getScreen(), "Frames: " + frames + " Ticks: " + ticks, 0 + getScreen().screenX, 0 + getScreen().screenY, 4);

				
			}
			
		}
		
		
		if(scaledDown) {
			
			if(!artifactMenu && !mainShipWorld && !pauseMenu){
				
				//drawing for the level
				
				float ratioY = (float) height / screenHeight;
				float ratioX = (float) width / screenWidth;
				
				for(int y = 0; y < screenHeight; y++) {
					for(int x = 0; x < screenWidth; x++) {
						screenScaledPixels[x + y * screenWidth] = screenTiles.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
					}
				}

			}else if(artifactMenu && !mainShipWorld && !pauseMenu){
				
				//drawing for the collection screen

				float ratioY = (float) height / screenHeight;
				float ratioX = (float) width / screenWidth;
				
				for(int y = 0; y < screenHeight; y++) {
					for(int x = 0; x < screenWidth; x++) {
						screenScaledPixels[x + y * screenWidth] = screenTiles.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
						screenScaledTransparentPixels[x + y * screenWidth] = screenCollection.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
					}
				}

			}else if(!artifactMenu && mainShipWorld && !pauseMenu){
				
				
				//drawing for the mother ship
				
				float ratioY = (float) height / screenHeight;
				float ratioX = (float) width / screenWidth;
				
				
				for(int y = 0; y < screenHeight; y++) {
					for(int x = 0; x < screenWidth; x++) {
						screenScaledPixels[x + y * screenWidth] = screenShip.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
					}
				}
				
			}else if(pauseMenu && !artifactMenu && mainShipWorld){
				
				//drawing for the mother ship with pause menu
				
				
				float ratioY = (float) height / screenHeight;
				float ratioX = (float) width / screenWidth;
				
				for(int y = 0; y < screenHeight; y++) {
					for(int x = 0; x < screenWidth; x++) {
						screenScaledPixels[x + y * screenWidth] = screenShip.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
						screenScaledTransparentPixels[x + y * screenWidth] = screenPause.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
					}
				}
				
			}else if(pauseMenu && !artifactMenu && !mainShipWorld){
				
				//drawing for the level with pause menu
				
				float ratioY = (float) height / screenHeight;
				float ratioX = (float) width / screenWidth;
				
				for(int y = 0; y < screenHeight; y++) {
					for(int x = 0; x < screenWidth; x++) {
						screenScaledPixels[x + y * screenWidth] = screenTiles.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
						screenScaledTransparentPixels[x + y * screenWidth] = screenPause.pixels[((int)(ratioX * x)) + ((int)(ratioY * y) * width)];
					}
				}
				
			}
		}
        
		
	}
	
	public void loadLevel(String loadLevel, int levelNumber, int worldNumber){
		collectionBackground = null;
		mainShipManager = null;
		screenShip = null;
		screenCollection = null;
		artifactMenu = false;
		mainShipWorld = false;
	
		level = new Level(loadLevel, levelNumber, worldNumber, width, height, levelSequence);
		screenTiles = level.getScreen();
		player = level.setPlayer(input);
		player.noControl = true;

		backgroundManager = new BackgroundManager(levelNumber, worldNumber, width, height, screenTiles, level);
		
	}
	
	public int[] getCurrentWorldAndLevel(){
		return new int[] {level.worldNumber, level.levelNumber};
	}
	
	private Screen getScreen(){
		if(screenTiles != null) return screenTiles;
		if(screenCollection != null) return screenCollection;
		if(screenShip != null) return screenShip;
		if(screenPause != null) return screenPause;

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
		screenCollection.removeSpriteSheets();
	}
	
	public static int getTicks(){
		return tickCount;
	}
	
	
	//Sets the display mode !!!REMEMBER TO ADD MORE!!!
	private DisplayMode[] bestDisplayModes = new DisplayMode[]{
			new DisplayMode(1920, 1080, 32, 0),
			new DisplayMode(1600, 900, 32, 0),
			new DisplayMode(1366, 768, 32, 0)
		};
	
	private DisplayMode getBestDisplayMode(GraphicsDevice gd){
		for(int i = 0; i < bestDisplayModes.length; i++){
			DisplayMode[] modes = gd.getDisplayModes();
			for(int j = 0; j < modes.length; j++){
				if(modes[j].getHeight() == bestDisplayModes[i].getHeight() &&
					modes[j].getWidth() == bestDisplayModes[i].getWidth() &&
					modes[j].getBitDepth() == bestDisplayModes[i].getBitDepth()){
					return bestDisplayModes[i];
				}
			}
		}
		return null;
	}
	
	private void chooseBestDisplayMode(GraphicsDevice gd){
		DisplayMode display = getBestDisplayMode(gd);
		if(display != null){
			gd.setDisplayMode(display);
		}else{
			gd.setDisplayMode(gd.getDisplayMode());
		}
	}
	
	public static void main(String[] args){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		new Main(gd);
	}
}
