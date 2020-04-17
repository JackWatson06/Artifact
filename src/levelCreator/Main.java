package com.murdermaninc.levelCreator;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;









public class Main extends Canvas implements Runnable{

	/*DONE Major revamp of the level editor class (FILL ALL REQUIREMENTS)
		
		A.) Make it work with the mouse and clicking - COMPLETED!!!
		B.) Make it compatible with the level setBlocks method for easy implementation and no rewriting the id's and such (or make it as easy as a copy and paste) - COMPLETED!!!
		C.) Make it scroll bassed off mouse cureser - COMPLETED!
		D.) Make a drop down menu for the blocks, deocrations, and subArtifacts. - COMPLETED!!!
		E.) Revamp the render method to the newer modern render method - COMPLETED!
		F.) Make it work with cnt + z - COMPLETED!!!
		G.) Make a small minimap that just has pixels for where blocks are or a substitute would be making the ability to scroll outward. - COMPLETED!!!
	
	*/
	
	private static final long serialVersionUID = 1L;
	
	static JLabel nameLabel = new JLabel("Enter the name");
	static JTextField nameText = new JTextField(8);
	static JLabel loadLabel = new JLabel("Load");
	static JTextField loadText = new JTextField(8);
	static JLabel widthLabel = new JLabel("Enter the width");
	static JTextField widthText = new JTextField(3);
	static JLabel heightLabel = new JLabel("Enter the height");
	static JTextField heightText = new JTextField(3);
	static JButton confirm = new JButton("Confirm");
	
	public int level = 0;
	public int world = 0;
	
	static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int screenWidth = (int) ScreenSize.getWidth();
	public static final int screenHeight = (int) ScreenSize.getHeight();
	
	public static int width = 1920;
	public static int height = 1080;
	
	private static float scale = (float) (screenWidth) / width;
	
	public int levelWidth;
	public int levelHeight;
	public static boolean waiting = true;
	
	public boolean running = true;

	public InputManager input = new InputManager(this);
	public Cursor cursor;
	
	public File levelD;
	public int[] levelData;
	public int[] decorationData;
	public int[] subArtifactData;

	private Screen screen;
	private BackgroundManager backgroundManager;
	
	public int editorId = 0;
	public Block editorBlock;
	public Decoration editorDecoration;
	public DecorationArtifact editorDecorationArtifact;
	public DecorationSubArtifact editorSubArtifact;
	
	private int lastXPlace = -1;
	private int lastXPlaceShift = -1;
	private int lastXDeletePlace = -1;
	private int lastYPlace = -1;
	private int lastYPlaceShift = -1;
	private int lastYDeletePlace = -1;
	private int lastIdPlace = -1;
	private boolean applyLeftClick = false;
	private boolean applyRightClick = false;
	//0 is leftClick and 1 is rightClick
	private int lastAction = 0;
	
	
	private Font font = new Font();
	private boolean showSaveFont = false;
	private int saveCounter = 0;
	
	public Menu menu;
	public MiniMap miniMap;
	
	private ArrayList<int[]> olderBlocks = new ArrayList<int[]>();
	private ArrayList<int[]> olderDecorations = new ArrayList<int[]>();
	private ArrayList<int[]> olderSubArtifacts = new ArrayList<int[]>();
	
	private boolean displayCoords = false;
	
	public void start(){
		new Thread(this).start();
	}
	
	public void init(){
		System.out.println(loadText.getText());
		if(loadText.getText().length() == 0){
			
			String levelName = nameText.getText();
			
			//Creates a new level
			for(int i = 0; i < levelName.length(); i++){
				char currentChar = levelName.charAt(i);
				if(currentChar == '-'){
					world = Integer.parseInt(levelName.substring(5, i));
					level = Integer.parseInt(levelName.substring(i + 1));
				}
			}
			
			levelWidth = (Integer.parseInt(widthText.getText()));
			levelHeight = (Integer.parseInt(heightText.getText()));
				
			levelData = new int[levelWidth * levelHeight];
			decorationData = new int[0];
			subArtifactData = new int[0];
		}else{
			
			//Loads a current level if possible
			
			File loadFile = new File("levels/" + loadText.getText());
			if(loadFile.exists()){
				String levelName = loadText.getText();
				
				//Creates a new level
				for(int i = 0; i < levelName.length(); i++){
					char currentChar = levelName.charAt(i);
					if(currentChar == '-'){
						world = Integer.parseInt(levelName.substring(5, i));
						level = Integer.parseInt(levelName.substring(i + 1));
					}
				}
				try {
					FileReader reader = new FileReader(loadFile);
					BufferedReader br = new BufferedReader(reader);
					try {
						levelWidth = Integer.parseInt(br.readLine());
						levelHeight = Integer.parseInt(br.readLine());
						levelData = new int[levelWidth * levelHeight];
						for(int i = 0; i < levelHeight; i++){
							ArrayList<String> currentLineIDs = new ArrayList<String>();
							String currentLine = br.readLine();
							String currentID = "";
							for(int j = 0; j < currentLine.length(); j++){
								if(currentLine.charAt(j) == ','){
									currentLineIDs.add(currentID);
									currentID = "";
								}else{
									currentID += currentLine.charAt(j);
								}
								//levelData[j + i * levelWidth] = Character.getNumericValue(currentLine.charAt(j));
							}
							for(int j = 0; j < levelWidth; j++){
								levelData[j + i * levelWidth] = Integer.parseInt(currentLineIDs.get(j));
							}
						}
						
						int subArtifactAmount = Integer.parseInt(br.readLine());
						if(subArtifactAmount > 0){
							
							subArtifactData = new int[subArtifactAmount * 3];
							for(int i = 0; i < subArtifactAmount; i++){
								br.readLine();
								for(int j = 0; j < 3; j++){
									subArtifactData[j + (i * 3)] = Integer.parseInt(br.readLine());
								}
							}
						}else{
							subArtifactData = new int[0];
						}
						
						
						int decorationAmount = Integer.parseInt(br.readLine());
						if(decorationAmount > 0){
							
							decorationData = new int[decorationAmount * 3];
							for(int i = 0; i < decorationAmount; i++){
								//decorations.add(new Decorations(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())));
								for(int j = 0; j < 3; j++){
									decorationData[j + (i * 3)] = Integer.parseInt(br.readLine());
								}
							}
						}else{
							decorationData = new int[0];
						}
						

						reader.close();
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		screen = new Screen(width, height, 0, 0, 0);
		cursor = new Cursor();
		
		screen.loadSpriteSheet("/icons.png", "iconsPlains");
		screen.loadSpriteSheet("/icons_Forest.png", "iconsForest");
		screen.loadSpriteSheet("/trees_Decoration.png", "iconsTree");
		screen.loadSpriteSheet("/font.png", "font");
		
		menu = new Menu(this, screen);
		miniMap = new MiniMap(this, levelWidth, levelHeight, levelData, decorationData, subArtifactData);
		if(world == 1){
			if(level >= 1 && level <= 4 || level == 17){
				backgroundManager = new BackgroundManager("/1.1-4 Background.png", width, height, true);
			}else if(level >= 5 && level <= 8){
				backgroundManager = new BackgroundManager("/1.5-8 Background.png", width, height, true);
			}else if(level > 8 && level<= 12){
				backgroundManager = new BackgroundManager("/1.9-12 Background.png", width, height, false);
			}else if(level > 12 && level <= 16){
				backgroundManager = new BackgroundManager("/1.13-16 Background.png", width, height, false);
			}
		}else if(world == 2){
			if(level >= 1 && level <= 4){
				backgroundManager = new BackgroundManager("/2.1-4 Background.png", width, height, true, 0, 768);
			}
		}
		
		Block.blocks = new Block[levelWidth * levelHeight];
		loadBDData();
		
		
		
		olderBlocks.add(levelData.clone());
		olderDecorations.add(decorationData.clone());
		olderSubArtifacts.add(subArtifactData.clone());
		
	}

	public void run() {
	
		long lastTime = System.nanoTime();
		int desiredUPS = 1000;
		long nanoTimeTicks = (1000*1000*1000) / desiredUPS;
		int frames = 0;
		int ticks = 0;
		long timePassedTicks = 0;
		long reset = System.nanoTime();
		boolean shouldRender = false;
		
		init();

		while(running){
			long now = System.nanoTime();
			timePassedTicks += (now - lastTime);
			lastTime = now;
			
			while(timePassedTicks >= nanoTimeTicks){
				ticks++;
				timePassedTicks = 0;
				tick();
				shouldRender = true;
			
			}
			if(shouldRender){
				frames++;
				shouldRender = false;
				render();
			}
			
			if(System.nanoTime() - reset > (1000*1000*1000)){
				reset += (1000*1000*1000);
				System.out.println("Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
				}
			}	
			
	}
	
	public void tick(){
		
		int scrollSpeed = 16;
		
		if(MouseInfo.getPointerInfo().getLocation().x == screenWidth - 1){
			screen.screenX += scrollSpeed;
			
			if(screen.screenX < 0){
				int add = 0 - screen.screenX;
				screen.screenX += add;
			}
			
			if(screen.screenX > (levelWidth * 64) - width){
				int subtract = screen.screenX - ((levelWidth * 64) - width);
				screen.screenX -= subtract;
			}
		}
		
		if(MouseInfo.getPointerInfo().getLocation().y == screenHeight - 1){
			screen.screenY += scrollSpeed;
			
			if(screen.screenY < 0){
				int add = 0 - screen.screenY;
				screen.screenY += add;
			}
			
			if(screen.screenY > (levelHeight * 64) - height){
				int subtract = screen.screenY - ((levelHeight * 64) - height);
				screen.screenY -= subtract;
			}
		}
		
		if(MouseInfo.getPointerInfo().getLocation().x == 0){
			screen.screenX -= scrollSpeed;
			
			if(screen.screenX < 0){
				int add = 0 - screen.screenX;
				screen.screenX += add;
			}
			
			if(screen.screenX > (levelWidth * 64) - width){
				int subtract = screen.screenX - ((levelWidth * 64) - width);
				screen.screenX -= subtract;
			}
		}
		
		if(MouseInfo.getPointerInfo().getLocation().y == 0){
			screen.screenY -= scrollSpeed;
			
			if(screen.screenY < 0){
				int add = 0 - screen.screenY;
				screen.screenY += add;
			}
			
			if(screen.screenY > (levelHeight * 64) - height){
				int subtract = screen.screenY - ((levelHeight * 64) - height);
				screen.screenY -= subtract;
			}
		}
		
		cursor.tick(screen, scale);
		
		
		if(input.controlZ){
			input.controlZ = false;
			
			if(olderBlocks.size() > 1){
				
				levelData = olderBlocks.get(olderBlocks.size() - 2).clone();
				olderBlocks.remove(olderBlocks.size() - 1);
				decorationData = olderDecorations.get(olderDecorations.size() - 2).clone();
				olderDecorations.remove(olderDecorations.size() - 1);
				subArtifactData = olderSubArtifacts.get(olderSubArtifacts.size() - 2).clone();
				olderSubArtifacts.remove(olderSubArtifacts.size() - 1);	
				
				loadBDData();
			}
		}
		
		if(input.plus) {
			
			//This prevents
			int originalHeight = height;
			int originalWidth = width;
			
			//Multiplying by the aspect ration of the monitor that is why it is multiples of 16 and also 9
			height = height + 18;
			width = width + 32;
			
			
			
			if(height > levelHeight * 64 && !(width > levelWidth * 64)) {
				
				//This is used to maintain aspect ratio because if the height is set to the same as the levelHeight it would 
				//loose it's aspect ratio as a levelHeight is not divisable by 64
				
				height = levelHeight * 64;
				
				int heightChange = originalHeight - height;
				
				float widthChange = heightChange * ((float) 16 / 9);
				
				width = width - (int) Math.round(widthChange);
				

			}else if(width > levelWidth * 64) {
				
				//This is used to maintain aspect ratio because if the height is set to the same as the levelHeight it would 
				//loose it's aspect ratio as a levelHeight is not divisable by 64
				
				width = levelWidth * 64;
				
				int widthChange = originalWidth - width;
				
				float heightChange = widthChange * ((float) 9 / 16);
				
				height = height - (int) Math.round(heightChange);
				
			}
			
			
			if(originalHeight != height && originalWidth != width) {

				screen.height = height;
				screen.width = width;

				screen.image = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_RGB);
				screen.pixels = ((DataBufferInt) screen.image.getRaster().getDataBuffer()).getData();
			}else {
				height = originalHeight;
				width = originalWidth;
			}
			
			scale = (float) (screenWidth) / width;
			
			if(height + screen.screenY > levelHeight * 64) {
				screen.screenY-=((screen.screenY + height) - (levelHeight * 64));
			}
			
			if(width + screen.screenX > levelWidth * 64) {
				screen.screenX-=((screen.screenX + width) - (levelWidth * 64));
			}
			
			input.plus = false;
		}
		
		if(input.minus) {
			
			//This prevents
			int originalHeight = height;
			int originalWidth = width;
			
			height = height - 18;
			width = width - 32;
			
			if(height <= 0 || width <= 0) {
				height = 18;
				width = 32;
			}

			if(originalHeight != height && originalWidth != width) {

				screen.height = height;
				screen.width = width;

				screen.image = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_RGB);
				screen.pixels = ((DataBufferInt) screen.image.getRaster().getDataBuffer()).getData();
			}else {
				height = originalHeight;
				width = originalWidth;
			}
			
			scale = (float) (screenWidth) / width;
			
			
			input.minus = false;
		}
		
		if(input.n) {
			
			
			height = 1080;
			width = 1920;
			
			scale = (float) (screenWidth) / width;
			
			screen.height = height;
			screen.width = width;

			screen.image = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_RGB);
			screen.pixels = ((DataBufferInt) screen.image.getRaster().getDataBuffer()).getData();
			
		}

		
		//Editor Checks
		editorBlock = editorBlock(editorId, cursor.currentMouseXNS, cursor.currentMouseYNS);
		editorDecoration = editorDecoration(editorId, cursor.currentMouseXS, cursor.currentMouseYS);
		editorDecorationArtifact = editorDecorationArtifact(editorId, cursor.currentMouseXS, cursor.currentMouseYS);
		editorSubArtifact = editorDecorationSubArtifact(editorId, cursor.currentMouseXS, cursor.currentMouseYS);
		

		if(input.leftClick){

			int mouseX = cursor.currentMouseXNS;
			int mouseY = cursor.currentMouseYNS;

			if(mouseX != lastXPlace || mouseY != lastYPlace || editorId != lastIdPlace || lastAction == 1){
				applyLeftClick = true;
			}
		}

		//THIS ADDS BLOCKS OR DECORATIONS ON LEFT CLICK

		if(applyLeftClick){

			int mouseX = cursor.currentMouseXNS;
			int mouseY = cursor.currentMouseYNS;

			System.out.println("Testing123");


			if(editorBlock != null){
				levelData[mouseX + mouseY * levelWidth] = editorId;
			}

			if(editorDecoration != null){
				int preDecorationData[] = new int[decorationData.length + 3];

				int lastIValue = 0;
				for(int i = 0; i < decorationData.length; i++){
					preDecorationData[i] = decorationData[i];
					lastIValue = i;
				}

				if(preDecorationData.length != 3){
					preDecorationData[lastIValue + 1] = editorDecoration.id;
					preDecorationData[lastIValue + 2] = editorDecoration.x;
					preDecorationData[lastIValue + 3] = editorDecoration.y;
				}else{
					preDecorationData[lastIValue] = editorDecoration.id;
					preDecorationData[lastIValue + 1] = editorDecoration.x;
					preDecorationData[lastIValue + 2] = editorDecoration.y;
				}




				decorationData = preDecorationData.clone();

			}

			if(editorDecorationArtifact != null){

				int preDecorationData[] = new int[decorationData.length + 3];

				int lastIValue = 0;
				for(int i = 0; i < decorationData.length; i++){
					preDecorationData[i] = decorationData[i];
					lastIValue = i;
				}
				if(preDecorationData.length != 3){
					preDecorationData[lastIValue + 1] = editorDecorationArtifact.id;
					preDecorationData[lastIValue + 2] = editorDecorationArtifact.x;
					preDecorationData[lastIValue + 3] = editorDecorationArtifact.y;
				}else{
					preDecorationData[lastIValue] = editorDecorationArtifact.id;
					preDecorationData[lastIValue + 1] = editorDecorationArtifact.x;
					preDecorationData[lastIValue + 2] = editorDecorationArtifact.y;
				}

				decorationData = preDecorationData.clone();

			}

			if(editorSubArtifact != null){
				int preSubArtifactData[] = new int[subArtifactData.length + 3];

				int lastIValue = 0;
				for(int i = 0; i < subArtifactData.length; i++){
					preSubArtifactData[i] = subArtifactData[i];
					lastIValue = i;
				}

				if(preSubArtifactData.length != 3){
					preSubArtifactData[lastIValue + 1] = editorSubArtifact.id;
					preSubArtifactData[lastIValue + 2] = editorSubArtifact.x;
					preSubArtifactData[lastIValue + 3] = editorSubArtifact.y;
				}else{
					preSubArtifactData[lastIValue] = editorSubArtifact.id;
					preSubArtifactData[lastIValue + 1] = editorSubArtifact.x;
					preSubArtifactData[lastIValue + 2] = editorSubArtifact.y;
				}

				subArtifactData = preSubArtifactData.clone();
			}

			lastXPlace = mouseX;
			lastYPlace = mouseY;
			lastIdPlace = editorId;
			lastAction = 0;

			loadBDData();

			applyLeftClick = false;


		}

		//THIS REMOVES BLOCKS OR DECORATIONS ON RIGHT CLICK"
		if(input.rightClick){

			int mouseX = cursor.currentMouseXNS;
			int mouseY = cursor.currentMouseYNS;

			if(mouseX != lastXDeletePlace || mouseY != lastYDeletePlace || lastAction == 0){
				applyRightClick = true;
			}
		}

		if(applyRightClick){

			int mouseX = cursor.currentMouseXNS;
			int mouseY = cursor.currentMouseYNS;


			boolean foundDelete = false;

			System.out.println("Delete");

			if(editorBlock != null){
				if(levelData[mouseX + mouseY * levelWidth] != 0){
					levelData[mouseX + mouseY * levelWidth] = 0;
					foundDelete = true;
				}
			}

			if(editorDecoration != null){
				int objectDeleteNumber = 0;

				for(int i = 0; i < decorationData.length; i += 3){
					if(decorationData[i] >= 2000 && decorationData[i] < 2750 && decorationData[i + 1] == mouseX * 64  && decorationData[i + 2] == mouseY * 64){
						objectDeleteNumber++;
						foundDelete = true;
					}
				}

				if(foundDelete){
					int preDecorationData[] = new int[decorationData.length - (3 * objectDeleteNumber)];

					int deleteSubtract = 0;
					for(int i = 0; i < decorationData.length; i+=3){
						if(decorationData[i] >= 2000 && decorationData[i] < 2750 && decorationData[i + 1] == mouseX * 64  && decorationData[i + 2] == mouseY * 64){
							deleteSubtract += 3;
						}else{
							preDecorationData[i - deleteSubtract] = decorationData[i];
							preDecorationData[i + 1 - deleteSubtract] = decorationData[i + 1];
							preDecorationData[i + 2 - deleteSubtract] = decorationData[i + 2];
						}

					}


					decorationData = preDecorationData.clone();


				}
			}

			if(editorDecorationArtifact != null){

				int objectDeleteNumber = 0;

				for(int i = 0; i < decorationData.length; i += 3){
					if(decorationData[i] >= 2750 && decorationData[i] < 3000 && decorationData[i + 1] == mouseX * 64  && decorationData[i + 2] == mouseY * 64){
						foundDelete = true;
						objectDeleteNumber++;
					}
				}

				if(foundDelete){
					int preDecorationData[] = new int[decorationData.length - (3 * objectDeleteNumber)];

					int deleteSubtract = 0;
					for(int i = 0; i < decorationData.length; i+=3){
						if(decorationData[i] >= 2750 && decorationData[i] < 3000 && decorationData[i + 1] == mouseX * 64  && decorationData[i + 2] == mouseY * 64){
							deleteSubtract += 3;
						}else{
							preDecorationData[i - deleteSubtract] = decorationData[i];
							preDecorationData[i + 1 - deleteSubtract] = decorationData[i + 1];
							preDecorationData[i + 2 - deleteSubtract] = decorationData[i + 2];
						}

					}

					decorationData = preDecorationData.clone();

				}

			}

			if(editorSubArtifact != null){

				int objectDeleteNumber = 0;

				for(int i = 0; i < subArtifactData.length; i += 3){
					if(subArtifactData[i + 1] == mouseX * 64  && subArtifactData[i + 2] == mouseY * 64){
						foundDelete = true;
						objectDeleteNumber++;
					}
				}

				if(foundDelete){
					int preSubArtifactData[] = new int[subArtifactData.length - (3 * objectDeleteNumber)];

					int deleteSubtract = 0;
					for(int i = 0; i < subArtifactData.length; i+=3){
						if(subArtifactData[i + 1] == mouseX * 64  && subArtifactData[i + 2] == mouseY * 64){
							deleteSubtract += 3;
						}else{
							preSubArtifactData[i - deleteSubtract] = subArtifactData[i];
							preSubArtifactData[i + 1 - deleteSubtract] = subArtifactData[i + 1];
							preSubArtifactData[i + 2 - deleteSubtract] = subArtifactData[i + 2];
						}

					}

					subArtifactData = preSubArtifactData.clone();

				}

			}
			if(foundDelete){
				lastXDeletePlace = mouseX;
				lastYDeletePlace = mouseY;

				lastAction = 1;


				loadBDData();
			}

			applyRightClick = false;

		}
		
		if(input.middleClick){
			int currentId = 0;
			currentId = pickBlock();
			if(currentId == 0){
				currentId = pickEntity();
			}
			editorId = currentId;
			input.middleClick = false;
		}
	
		if(input.s){
			saveData();
			input.s = false;
		}
		
		if(input.c){
			displayCoords = !displayCoords;
			input.c = false;
		}

		if(olderBlocks.size() >= 40){
			olderBlocks.remove(0);
			olderDecorations.remove(0);
			olderSubArtifacts.remove(0);
		}
		
		menu.tick(scale);
		
		miniMap.updateMiniMap(levelData, decorationData, subArtifactData);
		
	}
	
	public void render(){
		//Sets up a buffere strategy of 2
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		
		
		backgroundManager.render(screen);

		
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).render == 3){
				Decoration.decorations.get(i).render(screen);
			}
		}
		
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).render == 2){
				Decoration.decorations.get(i).render(screen);
			}
		}
		
		for(int i = 0; i < Block.blocks.length; i++){
			if(Block.blocks[i].id != 0){
				Block.blocks[i].render(screen);
			}
		}
		
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).render == 1){
				Decoration.decorations.get(i).render(screen);
			}
		}
		
		//This renders the specific object id to the editor icon to indicate what is being placed down.
		
		if(editorBlock != null){
			editorBlock.render(screen);
		}
		
		if(editorDecoration != null){
			editorDecoration.render(screen);
		}
		
		if(editorDecorationArtifact != null){
			editorDecorationArtifact.render(screen);
		}
		
		if(editorSubArtifact != null){
			editorSubArtifact.render(screen);
		}
		
		cursor.render(screen);
		
		miniMap.render(screen, input, width, height);
		
		menu.render();
		
		if(displayCoords){
			font.drawText(screen, ":", (1920 / 2) - (font.getTextLength(":", 4) / 2) + screen.screenX, 0 + screen.screenY, 4);

			font.drawText(screen, Integer.toString(cursor.currentMouseXNS) + " ", (1920 / 2) - (font.getTextLength(":", 4) / 2) - (font.getTextLength(Integer.toString(cursor.currentMouseXNS) + " ", 4)) + screen.screenX, 0 + screen.screenY, 4);

			font.drawText(screen, " " + Integer.toString(cursor.currentMouseYNS), (1920 / 2) + (font.getTextLength(":", 4) / 2) + screen.screenX, 0 + screen.screenY, 4);
		}
		if(showSaveFont){
			if(saveCounter < 120){
				font.drawText(screen, "!!!Saved!!!", 1920 / 2 + 400 + screen.screenX, 0 + screen.screenY, 4);
				saveCounter++;
			}else{
				saveCounter = 0;
				showSaveFont = false;
			}
		}
		
		//Draws the buffered images to the screen
		Graphics g = bs.getDrawGraphics();
		g.drawImage(screen.image, 0, 0, screenWidth, screenHeight, null);
		g.dispose();
		bs.show();
	}
	
	public int pickBlock(){
		int currentX = cursor.currentMouseXNS;
		int currentY = cursor.currentMouseYNS;
		if(levelData[currentX + currentY * levelWidth] == 0){
			return 0;
		}else{
			return levelData[currentX + currentY * levelWidth];
		}

	}
	
	public int pickEntity(){
		int currentX = cursor.currentMouseXS;
		int currentY = cursor.currentMouseYS;
		
		for(int i = 0; i < decorationData.length; i+=3){
			if(currentX == decorationData[i + 1] && currentY == decorationData[i + 2]){
				return decorationData[i];
			}
		}
		
		for(int i = 0; i < subArtifactData.length; i+=3){
			if(currentX == subArtifactData[i + 1] && currentY == subArtifactData[i + 2]){
				return subArtifactData[i];
			}
		}
		
		return 0;
	}
	
	
	public void storeData(){
		
		lastXPlaceShift = cursor.currentMouseXNS;
		lastYPlaceShift = cursor.currentMouseYNS;
		
		olderBlocks.add(levelData.clone());
		olderDecorations.add(decorationData.clone());
		olderSubArtifacts.add(subArtifactData.clone());
		
		
	}
	
	public void fillShiftClick(){
		
		int xCurrentMouseNS = cursor.currentMouseXNS;
		int yCurrentMouseNS = cursor.currentMouseYNS;
		float slope;
		
		//System.out.println("LastYPlace: " + lastYPlaceShift);
		//System.out.println("CurrentYPlace: " + yCurrentMouseNS);
		//System.out.println("LastXPlace: " + lastXPlaceShift);
		//System.out.println("CurrentXPlace: " + xCurrentMouseNS);
		
		if(xCurrentMouseNS != lastXPlaceShift){
			slope = (float)(lastYPlaceShift - yCurrentMouseNS) / (float)(lastXPlaceShift - xCurrentMouseNS);
		}else{
			slope = 0;
		}
		
		
		if(lastYPlaceShift != -1){
			if(xCurrentMouseNS < lastXPlaceShift && slope >= -1F && slope <= 1F){
				for(int i = xCurrentMouseNS + 1; i < lastXPlaceShift; i++){
					if(editorBlock != null){
						int b = (int) Math.round(lastYPlaceShift + (-lastXPlaceShift * slope));
						int y = (int) Math.round(slope * i + b);
							
						levelData[(i + y * levelWidth)] = editorId;
					}
				}

			}else if(yCurrentMouseNS > lastYPlaceShift && (slope < -1F || slope > 1F)){
				for(int i = lastYPlaceShift + 1; i < yCurrentMouseNS; i++){
					if(editorBlock != null){
						int b = (int) Math.round(lastYPlaceShift + (-lastXPlaceShift * slope));
						System.out.println("TEsting");
						int x = (int) Math.round((i - b) / slope);
						System.out.println(slope);
						
						levelData[(x + i * levelWidth)] = editorId;
					}
				}
			}else if(xCurrentMouseNS > lastXPlaceShift && slope >= -1F && slope <= 1F){
				for(int i = lastXPlaceShift + 1; i < xCurrentMouseNS; i++){
					if(editorBlock != null){
						int b = (int) Math.round(lastYPlaceShift + (-lastXPlaceShift * slope));
						int y = (int) Math.round(slope * i + b);
							
						levelData[(i + y * levelWidth)] = editorId;
					}
				}

			}else if(yCurrentMouseNS < lastYPlaceShift && (slope < -1F || slope > 1F)){
				for(int i = yCurrentMouseNS + 1; i < lastYPlaceShift; i++){
					if(editorBlock != null){
						int b = (int) Math.round(lastYPlaceShift + (-lastXPlaceShift * slope));
						int x = (int) Math.round((i - b) / slope);
						
						levelData[(x + i * levelWidth)] = editorId;
					}
				}
			}else{
				if(yCurrentMouseNS > lastYPlaceShift){
					for(int i = lastYPlaceShift + 1; i < yCurrentMouseNS; i++){
						levelData[(xCurrentMouseNS + i * levelWidth)] = editorId;
					}
				}else{
					for(int i = yCurrentMouseNS + 1; i < lastYPlaceShift; i++){
						levelData[(xCurrentMouseNS + i * levelWidth)] = editorId;
					}
				}
			}
		}
		
		loadBDData();
	}
	
	//Saves the data of the level into a file determined by the file name text field
	public void saveData(){
		if(loadText.getText().length() == 0){
			levelD = new File("levels/" + nameText.getText());
		}else{
			levelD = new File("levels/" + loadText.getText());
		}
		try {
			FileWriter writer = new FileWriter(levelD);
			BufferedWriter br = new BufferedWriter(writer);
			String sw = Integer.toString(levelWidth);
			String sh = Integer.toString(levelHeight);
			br.write(sw);
			br.write("\n");
			br.write(sh);
			br.write("\n");
			for(int j = 0; j < levelHeight; j++){
				for(int i = 0; i < levelWidth; i++){
					String ld = Integer.toString(levelData[i + j * levelWidth]);
					br.write(ld);
					br.write(",");
				}
				br.write("\n");
			}
			
			int amountOfSub = 0;
			ArrayList<DecorationSubArtifact> subArtifactData = new ArrayList<DecorationSubArtifact>();
			
			for(int i = 0; i < Decoration.decorations.size(); i++){
				if(Decoration.decorations.get(i) instanceof DecorationSubArtifact){
					amountOfSub++;
					subArtifactData.add((DecorationSubArtifact) Decoration.decorations.get(i));
				}
			}
			
			
			if(amountOfSub > 0){
				br.write(Integer.toString(amountOfSub));
				br.write("\n");
				for(int i = 0; i < subArtifactData.size(); i++){
					br.write(subArtifactData.get(i).name);
					br.write("\n");
					br.write(Integer.toString(subArtifactData.get(i).id));
					br.write("\n");
					br.write(Integer.toString(subArtifactData.get(i).x));
					br.write("\n");
					br.write(Integer.toString(subArtifactData.get(i).y));
					br.write("\n");
				}
			}else{
				br.write("0");
				br.write("\n");
			}
			
			
			//Saves Decorations
			int amountOfDecorations = 0;
			
			for(int i = 0; i < Decoration.decorations.size(); i++){
				if(!(Decoration.decorations.get(i) instanceof DecorationSubArtifact)){
					amountOfDecorations++;
				} 	
			}
			
			
			if(amountOfDecorations > 0){
				br.write(Integer.toString(amountOfDecorations));
				br.write("\n");
				for(int i = 0; i < Decoration.decorations.size(); i++){
					if(!(Decoration.decorations.get(i) instanceof DecorationSubArtifact)){
						br.write(Integer.toString(Decoration.decorations.get(i).id));
						br.write("\n");
						br.write(Integer.toString(Decoration.decorations.get(i).x));
						br.write("\n");
						br.write(Integer.toString(Decoration.decorations.get(i).y));
						br.write("\n");
					}
				}
			}else{
				br.write("0");
			}
			
			br.flush();
			br.close();

		} catch (IOException e) {


		}
		
		showSaveFont = true;
	}
		
		//THIS IS FOR LOADING ACTUAL LEVEL DATA
	
	public void loadBDData(){
		
		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){
				
				//Sets up the ids for the blocks and the block type (id, x, y, xTile, yTile) for each new block type (a block type shares similar characteristics such as sound)
				//REMEMBER Blocks are specificaly for objects that will have collisions and also objects that cast shadows otherwise it is put in decorations
				if(levelData[x + y * levelWidth] == 0) Block.blocks[x + y * levelWidth] = new Block(0, x, y);
				
				//GrassBlocks
				if(levelData[x + y * levelWidth] == 1) Block.blocks[x + y * levelWidth] = new Block(1, x, y, 1, 0);
				if(levelData[x + y * levelWidth] == 2) Block.blocks[x + y * levelWidth] = new Block(2, x, y, 0, 0);
				if(levelData[x + y * levelWidth] == 3) Block.blocks[x + y * levelWidth] = new Block(3, x, y, 2, 0);
				if(levelData[x + y * levelWidth] == 4) Block.blocks[x + y * levelWidth] = new Block(4, x, y, 3, 0);
				if(levelData[x + y * levelWidth] == 5) Block.blocks[x + y * levelWidth] = new Block(5, x, y, 4, 0);
				if(levelData[x + y * levelWidth] == 6) Block.blocks[x + y * levelWidth] = new Block(6, x, y, 5, 0);
				if(levelData[x + y * levelWidth] == 7) Block.blocks[x + y * levelWidth] = new Block(7, x, y, 6, 0);
				if(levelData[x + y * levelWidth] == 8) Block.blocks[x + y * levelWidth] = new Block(8, x, y, 7, 0);
				if(levelData[x + y * levelWidth] == 9) Block.blocks[x + y * levelWidth] = new Block(9, x, y, 0, 1);
				if(levelData[x + y * levelWidth] == 10) Block.blocks[x + y * levelWidth] = new Block(10, x, y, 1, 1);
				if(levelData[x + y * levelWidth] == 11) Block.blocks[x + y * levelWidth] = new Block(11, x, y, 2, 1);
				if(levelData[x + y * levelWidth] == 12) Block.blocks[x + y * levelWidth] = new Block(12, x, y, 3, 1);
				if(levelData[x + y * levelWidth] == 13) Block.blocks[x + y * levelWidth] = new Block(13, x, y, 4, 1);
				if(levelData[x + y * levelWidth] == 14) Block.blocks[x + y * levelWidth] = new Block(14, x, y, 5, 1);
				if(levelData[x + y * levelWidth] == 15) Block.blocks[x + y * levelWidth] = new Block(15, x, y, 6, 1);
				if(levelData[x + y * levelWidth] == 16) Block.blocks[x + y * levelWidth] = new Block(16, x, y, 7, 1);
				if(levelData[x + y * levelWidth] == 17) Block.blocks[x + y * levelWidth] = new Block(17, x, y, 0, 2);
				if(levelData[x + y * levelWidth] == 18) Block.blocks[x + y * levelWidth] = new Block(18, x, y, 1, 2);
				if(levelData[x + y * levelWidth] == 19) Block.blocks[x + y * levelWidth] = new Block(19, x, y, 2, 2);
				if(levelData[x + y * levelWidth] == 20) Block.blocks[x + y * levelWidth] = new Block(20, x, y, 3, 2);
				if(levelData[x + y * levelWidth] == 21) Block.blocks[x + y * levelWidth] = new Block(21, x, y, 4, 2);
				if(levelData[x + y * levelWidth] == 22) Block.blocks[x + y * levelWidth] = new Block(22, x, y, 5, 2);
				if(levelData[x + y * levelWidth] == 23) Block.blocks[x + y * levelWidth] = new Block(23, x, y, 6, 2);
				if(levelData[x + y * levelWidth] == 24) Block.blocks[x + y * levelWidth] = new Block(24, x, y, 7, 2);
				if(levelData[x + y * levelWidth] == 25) Block.blocks[x + y * levelWidth] = new Block(25, x, y, 0, 3);
				if(levelData[x + y * levelWidth] == 26) Block.blocks[x + y * levelWidth] = new Block(26, x, y, 1, 3);
				if(levelData[x + y * levelWidth] == 27) Block.blocks[x + y * levelWidth] = new Block(27, x, y, 2, 3);
				if(levelData[x + y * levelWidth] == 28) Block.blocks[x + y * levelWidth] = new Block(28, x, y, 3, 3);
				if(levelData[x + y * levelWidth] == 29) Block.blocks[x + y * levelWidth] = new Block(29, x, y, 4, 3);
				if(levelData[x + y * levelWidth] == 30) Block.blocks[x + y * levelWidth] = new Block(30, x, y, 5, 3);
				if(levelData[x + y * levelWidth] == 31) Block.blocks[x + y * levelWidth] = new Block(31, x, y, 1, 4);
				if(levelData[x + y * levelWidth] == 32) Block.blocks[x + y * levelWidth] = new Block(32, x, y, 0, 4);
				if(levelData[x + y * levelWidth] == 33) Block.blocks[x + y * levelWidth] = new Block(33, x, y, 2, 4);
				if(levelData[x + y * levelWidth] == 34) Block.blocks[x + y * levelWidth] = new Block(34, x, y, 3, 4);
				if(levelData[x + y * levelWidth] == 35) Block.blocks[x + y * levelWidth] = new Block(35, x, y, 4, 4);
				if(levelData[x + y * levelWidth] == 36) Block.blocks[x + y * levelWidth] = new Block(36, x, y, 5, 4);
				if(levelData[x + y * levelWidth] == 37) Block.blocks[x + y * levelWidth] = new Block(37, x, y, 6, 4);
				if(levelData[x + y * levelWidth] == 38) Block.blocks[x + y * levelWidth] = new Block(38, x, y, 0, 5); 
				if(levelData[x + y * levelWidth] == 39) Block.blocks[x + y * levelWidth] = new Block(39, x, y, 6, 3); 
				if(levelData[x + y * levelWidth] == 40) Block.blocks[x + y * levelWidth] = new Block(40, x, y, 3, 5); 
				if(levelData[x + y * levelWidth] == 41) Block.blocks[x + y * levelWidth] = new Block(41, x, y, 1, 13); 
				if(levelData[x + y * levelWidth] == 42) Block.blocks[x + y * levelWidth] = new Block(42, x, y, 2, 13); 
				if(levelData[x + y * levelWidth] == 43) Block.blocks[x + y * levelWidth] = new Block(43, x, y, 3, 13); 
				if(levelData[x + y * levelWidth] == 44) Block.blocks[x + y * levelWidth] = new Block(44, x, y, 4, 13); 
				if(levelData[x + y * levelWidth] == 45) Block.blocks[x + y * levelWidth] = new Block(45, x, y, 5, 13); 
				if(levelData[x + y * levelWidth] == 46) Block.blocks[x + y * levelWidth] = new Block(46, x, y, 7, 4);
				if(levelData[x + y * levelWidth] == 47) Block.blocks[x + y * levelWidth] = new Block(47, x, y, 0, 0);
				if(levelData[x + y * levelWidth] == 48) Block.blocks[x + y * levelWidth] = new Block(48, x, y, 1, 0);
				if(levelData[x + y * levelWidth] == 49) Block.blocks[x + y * levelWidth] = new Block(49, x, y, 2, 0);
				if(levelData[x + y * levelWidth] == 50) Block.blocks[x + y * levelWidth] = new Block(50, x, y, 3, 0);
				if(levelData[x + y * levelWidth] == 51) Block.blocks[x + y * levelWidth] = new Block(51, x, y, 4, 0);
				if(levelData[x + y * levelWidth] == 52) Block.blocks[x + y * levelWidth] = new Block(52, x, y, 5, 0);
				if(levelData[x + y * levelWidth] == 53) Block.blocks[x + y * levelWidth] = new Block(53, x, y, 6, 0);
				if(levelData[x + y * levelWidth] == 54) Block.blocks[x + y * levelWidth] = new Block(54, x, y, 7, 0);
				if(levelData[x + y * levelWidth] == 55) Block.blocks[x + y * levelWidth] = new Block(55, x, y, 8, 0);
				if(levelData[x + y * levelWidth] == 56) Block.blocks[x + y * levelWidth] = new Block(56, x, y, 9, 0);
				if(levelData[x + y * levelWidth] == 57) Block.blocks[x + y * levelWidth] = new Block(57, x, y, 0, 1);
				if(levelData[x + y * levelWidth] == 58) Block.blocks[x + y * levelWidth] = new Block(58, x, y, 1, 1);
				if(levelData[x + y * levelWidth] == 59) Block.blocks[x + y * levelWidth] = new Block(59, x, y, 2, 1);
				if(levelData[x + y * levelWidth] == 60) Block.blocks[x + y * levelWidth] = new Block(60, x, y, 3, 1);
				if(levelData[x + y * levelWidth] == 61) Block.blocks[x + y * levelWidth] = new Block(61, x, y, 4, 1);
				if(levelData[x + y * levelWidth] == 62) Block.blocks[x + y * levelWidth] = new Block(62, x, y, 5, 1);
				if(levelData[x + y * levelWidth] == 63) Block.blocks[x + y * levelWidth] = new Block(63, x, y, 6, 1);
				if(levelData[x + y * levelWidth] == 64) Block.blocks[x + y * levelWidth] = new Block(64, x, y, 7, 1);
				if(levelData[x + y * levelWidth] == 65) Block.blocks[x + y * levelWidth] = new Block(65, x, y, 8, 1);
				if(levelData[x + y * levelWidth] == 66) Block.blocks[x + y * levelWidth] = new Block(66, x, y, 9, 1);
				if(levelData[x + y * levelWidth] == 67) Block.blocks[x + y * levelWidth] = new Block(67, x, y, 0, 2);
				if(levelData[x + y * levelWidth] == 68) Block.blocks[x + y * levelWidth] = new Block(68, x, y, 1, 2);
				if(levelData[x + y * levelWidth] == 69) Block.blocks[x + y * levelWidth] = new Block(69, x, y, 2, 2);
				if(levelData[x + y * levelWidth] == 70) Block.blocks[x + y * levelWidth] = new Block(70, x, y, 3, 2);
				if(levelData[x + y * levelWidth] == 71) Block.blocks[x + y * levelWidth] = new Block(71, x, y, 4, 2);
				if(levelData[x + y * levelWidth] == 72) Block.blocks[x + y * levelWidth] = new Block(72, x, y, 5, 2);
				if(levelData[x + y * levelWidth] == 73) Block.blocks[x + y * levelWidth] = new Block(73, x, y, 6, 2);
				if(levelData[x + y * levelWidth] == 74) Block.blocks[x + y * levelWidth] = new Block(74, x, y, 7, 2);
				if(levelData[x + y * levelWidth] == 75) Block.blocks[x + y * levelWidth] = new Block(75, x, y, 8, 2);
				if(levelData[x + y * levelWidth] == 76) Block.blocks[x + y * levelWidth] = new Block(76, x, y, 9, 2);
				if(levelData[x + y * levelWidth] == 77) Block.blocks[x + y * levelWidth] = new Block(77, x, y, 0, 3);
				if(levelData[x + y * levelWidth] == 78) Block.blocks[x + y * levelWidth] = new Block(78, x, y, 1, 3);
				if(levelData[x + y * levelWidth] == 79) Block.blocks[x + y * levelWidth] = new Block(79, x, y, 2, 3);
				if(levelData[x + y * levelWidth] == 80) Block.blocks[x + y * levelWidth] = new Block(80, x, y, 3, 3);
				if(levelData[x + y * levelWidth] == 81) Block.blocks[x + y * levelWidth] = new Block(81, x, y, 4, 3);
				if(levelData[x + y * levelWidth] == 82) Block.blocks[x + y * levelWidth] = new Block(82, x, y, 5, 3);
				if(levelData[x + y * levelWidth] == 83) Block.blocks[x + y * levelWidth] = new Block(83, x, y, 6, 3);
				if(levelData[x + y * levelWidth] == 84) Block.blocks[x + y * levelWidth] = new Block(84, x, y, 7, 3);
				if(levelData[x + y * levelWidth] == 85) Block.blocks[x + y * levelWidth] = new Block(85, x, y, 8, 3);
				if(levelData[x + y * levelWidth] == 86) Block.blocks[x + y * levelWidth] = new Block(86, x, y, 9, 3);
				if(levelData[x + y * levelWidth] == 87) Block.blocks[x + y * levelWidth] = new Block(87, x, y, 0, 4);
				if(levelData[x + y * levelWidth] == 88) Block.blocks[x + y * levelWidth] = new Block(88, x, y, 0, 9);
				if(levelData[x + y * levelWidth] == 89) Block.blocks[x + y * levelWidth] = new Block(89, x, y, 1, 9);
				if(levelData[x + y * levelWidth] == 90) Block.blocks[x + y * levelWidth] = new Block(90, x, y, 2, 9);
				if(levelData[x + y * levelWidth] == 91) Block.blocks[x + y * levelWidth] = new Block(91, x, y, 3, 9);
				if(levelData[x + y * levelWidth] == 92) Block.blocks[x + y * levelWidth] = new Block(92, x, y, 0, 10);
				if(levelData[x + y * levelWidth] == 93) Block.blocks[x + y * levelWidth] = new Block(93, x, y, 1, 10);
				if(levelData[x + y * levelWidth] == 94) Block.blocks[x + y * levelWidth] = new Block(94, x, y, 2, 10);
				if(levelData[x + y * levelWidth] == 95) Block.blocks[x + y * levelWidth] = new Block(95, x, y, 3, 10);
				if(levelData[x + y * levelWidth] == 96) Block.blocks[x + y * levelWidth] = new Block(96, x, y, 0, 8);
				if(levelData[x + y * levelWidth] == 97) Block.blocks[x + y * levelWidth] = new Block(97, x, y, 1, 8);
				if(levelData[x + y * levelWidth] == 98) Block.blocks[x + y * levelWidth] = new Block(98, x, y, 2, 8);
				if(levelData[x + y * levelWidth] == 99) Block.blocks[x + y * levelWidth] = new Block(99, x, y, 3, 8);
				if(levelData[x + y * levelWidth] == 100) Block.blocks[x + y * levelWidth] = new Block(100, x, y, 15, 24);
			}
		}
		
		
		Decoration.decorations.clear();
		for(int i = 0; i < decorationData.length; i+=3){
			//Sets the id for the decoration and also certain parameters  (id, x, y, leftMostXTile, topMostYTile, spriteWidth(tiles), spriteHeight(tiles),
			// render in front or behind blocks (true = front, false = behind), animated or not (true = animated, false = not animated), xTile (artifacts only), yTile (artifacts only),
			// name (artifacts only))
			//(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean render, boolean animated)
			
			//REMEMBER Animations are added in the actual class as a replacement for the render method
			
			//Decorations ID's     2000 - 2750
			
			int currentX = decorationData[i + 1];
			int currentY = decorationData[i + 2];
			
	
			if(decorationData[i] == 2000) Decoration.decorations.add(new Decoration(2000, currentX, currentY, 13, 0, 3, 3, 1)); //Ship 100 - 2005
			if(decorationData[i] == 2001) Decoration.decorations.add(new Decoration(2001, currentX, currentY, 1, 6, 1, 1, 2)); //ShortGrass  95 - 2000
			if(decorationData[i] == 2002) Decoration.decorations.add(new Decoration(2002, currentX, currentY, 0, 6, 1, 1, 2)); //LongGrass 96 - 2001
			if(decorationData[i] == 2003) Decoration.decorations.add(new Decoration(2003, currentX, currentY, 8, 6, 1, 1, 2)); //ShortGrass - 2 97 - 2002
			if(decorationData[i] == 2004) Decoration.decorations.add(new Decoration(2004, currentX, currentY, 3, 6, 1, 1, 2)); //LongGrass - 2 98 - 2003
			if(decorationData[i] == 2005) Decoration.decorations.add(new Decoration(2005, currentX, currentY, 8, 5, 1, 1, 2)); //Prickly Flower 99 - 2004
			if(decorationData[i] == 2006) Decoration.decorations.add(new Decoration(2006, currentX, currentY, 0, 8, 1, 2, 1)); // 101 - 2006
			if(decorationData[i] == 2007) Decoration.decorations.add(new Decoration(2007, currentX, currentY, 2, 5, 1, 2, 1)); //TallGrass 102 - 2007
			if(decorationData[i] == 2008) Decoration.decorations.add(new Decoration(2008, currentX, currentY, 6, 5, 2, 2, 2)); //CurvedGrassLeft 103 - 2008
			if(decorationData[i] == 2009) Decoration.decorations.add(new Decoration(2009, currentX, currentY, 4, 5, 2, 2, 2)); //CurvedGrassRight 104 - 2009
			if(decorationData[i] == 2010) Decoration.decorations.add(new Decoration(2010, currentX, currentY, 9, 5, 2, 2, 1)); //MegaGrass 105 - 2010
			if(decorationData[i] == 2011) Decoration.decorations.add(new Decoration(2011, currentX, currentY, 9, 1, 1, 1, 1)); //BladeGrass 105 - 2010
			if(decorationData[i] == 2012) Decoration.decorations.add(new Decoration(2012, currentX, currentY, 8, 4, 1, 1, 1)); //BladeVine 105 - 2010
			if(decorationData[i] == 2013) Decoration.decorations.add(new Decoration(2013, currentX, currentY, 9, 3, 1, 2, 1)); //TallBladeGrass 105 - 2010
			if(decorationData[i] == 2014) Decoration.decorations.add(new Decoration(2014, currentX, currentY, 8, 1, 1, 1, 1)); //RegularVine 105 - 2010
			if(decorationData[i] == 2015) Decoration.decorations.add(new Decoration(2015, currentX, currentY, 8, 2, 1, 1, 1)); //RegularVineFlower 105 - 2010
			if(decorationData[i] == 2016) Decoration.decorations.add(new Decoration(2016, currentX, currentY, 8, 3, 1, 1, 1)); //TransitionVine 105 - 2010
			if(decorationData[i] == 2017) Decoration.decorations.add(new Decoration(2017, currentX, currentY, 10, 4, 1, 1, 1)); // 101 - 2006
			if(decorationData[i] == 2018) Decoration.decorations.add(new Decoration(2018, currentX, currentY, 0, 10, 1, 1, 1));
			if(decorationData[i] == 2019) Decoration.decorations.add(new Decoration(2019, currentX, currentY, 1, 10, 1, 1, 1));
			if(decorationData[i] == 2020) Decoration.decorations.add(new Decoration(2020, currentX, currentY, 2, 10, 1, 1, 1));
			if(decorationData[i] == 2021) Decoration.decorations.add(new Decoration(2021, currentX, currentY, 3, 10, 1, 1, 1));
			if(decorationData[i] == 2022) Decoration.decorations.add(new Decoration(2022, currentX, currentY, 4, 10, 1, 1, 1));
			if(decorationData[i] == 2023) Decoration.decorations.add(new Decoration(2023, currentX, currentY, 5, 10, 1, 1, 1));
			if(decorationData[i] == 2024) Decoration.decorations.add(new Decoration(2024, currentX, currentY, 6, 10, 1, 1, 1));
			if(decorationData[i] == 2025) Decoration.decorations.add(new Decoration(2025, currentX, currentY, 7, 10, 1, 1, 1));
			if(decorationData[i] == 2026) Decoration.decorations.add(new Decoration(2026, currentX, currentY, 8, 10, 1, 1, 1));
			if(decorationData[i] == 2027) Decoration.decorations.add(new Decoration(2027, currentX, currentY, 1, 11, 1, 1, 1));
			if(decorationData[i] == 2028) Decoration.decorations.add(new Decoration(2028, currentX, currentY, 2, 11, 1, 1, 1));
			if(decorationData[i] == 2029) Decoration.decorations.add(new Decoration(2029, currentX, currentY, 3, 11, 1, 1, 1));
			if(decorationData[i] == 2030) Decoration.decorations.add(new Decoration(2030, currentX, currentY, 4, 11, 1, 1, 1));
			if(decorationData[i] == 2031) Decoration.decorations.add(new Decoration(2031, currentX, currentY, 5, 11, 1, 1, 1));
			if(decorationData[i] == 2032) Decoration.decorations.add(new Decoration(2032, currentX, currentY, 6, 11, 1, 1, 1));
			if(decorationData[i] == 2033) Decoration.decorations.add(new Decoration(2033, currentX, currentY, 7, 11, 1, 1, 1));
			if(decorationData[i] == 2034) Decoration.decorations.add(new Decoration(2034, currentX, currentY, 1, 12, 1, 1, 3));
			if(decorationData[i] == 2035) Decoration.decorations.add(new Decoration(2035, currentX, currentY, 3, 15, 3, 3, 1));
			if(decorationData[i] == 2036) Decoration.decorations.add(new Decoration(2036, currentX, currentY, 6, 15, 2, 3, 1));
			if(decorationData[i] == 2037) Decoration.decorations.add(new Decoration(2037, currentX, currentY, 8, 16, 1, 2, 1));
			if(decorationData[i] == 2038) Decoration.decorations.add(new Decoration(2038, currentX, currentY, 2, 16, 1, 2, 1));
			if(decorationData[i] == 2039) Decoration.decorations.add(new Tree(2039, currentX, currentY, -1, -1, -1, -1, 3));
			if(decorationData[i] == 2040) Decoration.decorations.add(new Tree(2040, currentX, currentY, -1, -1, -1, -1, 3));
			if(decorationData[i] == 2041) Decoration.decorations.add(new Tree(2041, currentX, currentY, -1, -1, -1, -1, 3));
			if(decorationData[i] == 2042) Decoration.decorations.add(new Decoration(2042, currentX, currentY, 0, 12, 1, 1, 2));
			if(decorationData[i] == 2043) Decoration.decorations.add(new Decoration(2043, currentX, currentY, 1, 12, 1, 1, 2));
			if(decorationData[i] == 2044) Decoration.decorations.add(new Decoration(2044, currentX, currentY, 0, 13, 1, 5, 2));
			if(decorationData[i] == 2045) Decoration.decorations.add(new Decoration(2045, currentX, currentY, 1, 13, 1, 5, 2));
			if(decorationData[i] == 2046) Decoration.decorations.add(new Decoration(2046, currentX, currentY, 2, 15, 1, 3, 2));
			if(decorationData[i] == 2047) Decoration.decorations.add(new Decoration(2047, currentX, currentY, 3, 15, 2, 2, 3));
			if(decorationData[i] == 2048) Decoration.decorations.add(new Decoration(2048, currentX, currentY, 3, 17, 4, 1, 2));
			if(decorationData[i] == 2049) Decoration.decorations.add(new Decoration(2049, currentX, currentY, 7, 17, 1, 1, 2));
			if(decorationData[i] == 2050) Decoration.decorations.add(new Decoration(2050, currentX, currentY, 5, 15, 2, 2, 3));
			if(decorationData[i] == 2051) Decoration.decorations.add(new Decoration(2051, currentX, currentY, 8, 15, 1, 3, 3));
			if(decorationData[i] == 2052) Decoration.decorations.add(new Decoration(2052, currentX, currentY, 9, 17, 1, 1, 3));
			if(decorationData[i] == 2053) Decoration.decorations.add(new Decoration(2053, currentX, currentY, 10, 17, 1, 1, 3));
			if(decorationData[i] == 2054) Decoration.decorations.add(new Decoration(2054, currentX, currentY, 11, 17, 1, 1, 3));
			if(decorationData[i] == 2055) Decoration.decorations.add(new Decoration(2055, currentX, currentY, 12, 15, 1, 3, 3));
			if(decorationData[i] == 2056) Decoration.decorations.add(new Decoration(2056, currentX, currentY, 3, 5, 1, 1, 1));
			if(decorationData[i] == 2057) Decoration.decorations.add(new Decoration(2057, currentX, currentY, 0, 11, 2, 1, 2));
			if(decorationData[i] == 2058) Decoration.decorations.add(new Decoration(2058, currentX, currentY, 4, 6, 1, 8, 3));
			if(decorationData[i] == 2059) Decoration.decorations.add(new Decoration(2059, currentX, currentY, 0, 20, 3, 4, 1));
			if(decorationData[i] == 2060) Decoration.decorations.add(new Decoration(2060, currentX, currentY, 3, 20, 3, 4, 1));
			if(decorationData[i] == 2061) Decoration.decorations.add(new Decoration(2061, currentX, currentY, 6, 20, 3, 4, 1));
			if(decorationData[i] == 2062) Decoration.decorations.add(new Decoration(2062, currentX, currentY, 4, 5, 1, 1, 1));
			if(decorationData[i] == 2063) Decoration.decorations.add(new Decoration(2063, currentX, currentY, 5, 6, 1, 1, 1));
			if(decorationData[i] == 2064) Decoration.decorations.add(new Decoration(2064, currentX, currentY, 10, 18, 5, 6, 1));
			
			
			//Artifact ID's    2750 - 3000
			
			if(decorationData[i] == 2750) Decoration.decorations.add(new DecorationArtifact(2750, currentX, currentY, 8, 0, 10, 10, 1, 1, 2, "Arrow")); //150 - 2750
			if(decorationData[i] == 2751) Decoration.decorations.add(new DecorationArtifact(2751, currentX, currentY, 9, 0, 12, 12, 1, 1, 2, "Bow")); // 151 - 2751
			if(decorationData[i] == 2752) Decoration.decorations.add(new DecorationArtifact(2752, currentX, currentY, 11, 0, 10, 10, 1, 1, 2, "Sword")); // 151 - 2751
			if(decorationData[i] == 2753) Decoration.decorations.add(new DecorationArtifact(2753, currentX, currentY, 10, 0, 10, 10, 1, 1, 2, "Rusty Chestplate")); // 151 - 2751
			if(decorationData[i] == 2754) Decoration.decorations.add(new DecorationArtifact(2754, currentX, currentY, 12, 0, 11, 11, 1, 1, 2, "Hammer")); // 151 - 2751
			if(decorationData[i] == 2755) Decoration.decorations.add(new DecorationArtifact(2755, currentX, currentY, 10, 1, 12, 12, 1, 1, 2, "War Axe")); // 151 - 2751
			if(decorationData[i] == 2756) Decoration.decorations.add(new DecorationArtifact(2756, currentX, currentY, 11, 1, 13, 13, 1, 1, 2, "Moldy Wheel")); // 151 - 2751
			if(decorationData[i] == 2757) Decoration.decorations.add(new DecorationArtifact(2757, currentX, currentY, 12, 1, 10, 7, 1, 1, 2, "Dual Daggers")); // 151 - 2751
			if(decorationData[i] == 2758) Decoration.decorations.add(new DecorationArtifact(2758, currentX, currentY, 13, 3, 11, 11, 1, 1, 2, "Paper")); // 151 - 2751
			if(decorationData[i] == 2759) Decoration.decorations.add(new DecorationArtifact(2759, currentX, currentY, 11, 2, 10, 12, 1, 1, 2, "Bucket")); // 151 - 2751
			if(decorationData[i] == 2760) Decoration.decorations.add(new DecorationArtifact(2760, currentX, currentY, 10, 2, 11, 14, 1, 1, 2, "Lantern")); // 151 - 2751
			if(decorationData[i] == 2761) Decoration.decorations.add(new DecorationArtifact(2761, currentX, currentY, 12, 2, 8, 11,  1, 1, 2, "Boots")); // 151 - 2751
			if(decorationData[i] == 2762) Decoration.decorations.add(new DecorationArtifact(2762, currentX, currentY, 10, 3, 9, 10, 1, 1, 2, "Shield")); // 151 - 2751
			if(decorationData[i] == 2763) Decoration.decorations.add(new DecorationArtifact(2763, currentX, currentY, 11, 3, 13, 10, 1, 1, 2, "Scroll")); // 151 - 2751
			if(decorationData[i] == 2764) Decoration.decorations.add(new DecorationArtifact(2764, currentX, currentY, 12, 3, 13, 13, 1, 1, 2, "Cauldron")); // 151 - 2751
			if(decorationData[i] == 2765) Decoration.decorations.add(new DecorationArtifact(2765, currentX, currentY, 14, 3, 11, 11,  1, 1, 2, "Book")); // 151 - 2751
			
			if(decorationData[i] == 2766) Decoration.decorations.add(new DecorationArtifact(2766, currentX, currentY, 10, 0, 11, 10, 1, 1, 2, "Scythe")); // 151 - 2751
			if(decorationData[i] == 2767) Decoration.decorations.add(new DecorationArtifact(2767, currentX, currentY, 11, 1, 10, 12, 1, 1, 2, "Hoe")); // 151 - 2751
			if(decorationData[i] == 2768) Decoration.decorations.add(new DecorationArtifact(2768, currentX, currentY, 10, 2, 16, 15, 1, 1, 2, "Map")); // 151 - 2751
			if(decorationData[i] == 2769) Decoration.decorations.add(new DecorationArtifact(2769, currentX, currentY, 12, 0, 16, 16, 1, 1, 2, "Calender")); // 151 - 2751
			if(decorationData[i] == 2770) Decoration.decorations.add(new DecorationArtifact(2770, currentX, currentY, 11, 0, 14, 16, 1, 1, 2, "Piller")); // 151 - 2751
			if(decorationData[i] == 2771) Decoration.decorations.add(new DecorationArtifact(2771, currentX, currentY, 10, 1, 15, 13, 1, 1, 2, "Alter")); // 151 - 2751
			if(decorationData[i] == 2772) Decoration.decorations.add(new DecorationArtifact(2772, currentX, currentY, 12, 1, 11, 12, 1, 1, 2, "Clay Tablet")); // 151 - 2751
			if(decorationData[i] == 2773) Decoration.decorations.add(new DecorationArtifact(2773, currentX, currentY, 11, 2, 15, 12, 1, 1, 2, "Statue")); // 151 - 2751
			if(decorationData[i] == 2774) Decoration.decorations.add(new DecorationArtifact(2774, currentX, currentY, 12, 2, 11, 14, 1, 1, 2, "Painting")); // 151 - 2751
			if(decorationData[i] == 2775) Decoration.decorations.add(new DecorationArtifact(2775, currentX, currentY, 11, 3, 12, 15, 1, 1, 2, "Rake")); // 151 - 2751
			if(decorationData[i] == 2776) Decoration.decorations.add(new DecorationArtifact(2776, currentX, currentY, 9, 4, 10, 8, 1, 1, 2, "Axe")); // 151 - 2751
			if(decorationData[i] == 2777) Decoration.decorations.add(new DecorationArtifact(2777, currentX, currentY, 9, 5, 13, 13, 1, 1, 2, "Cutters")); // 151 - 2751
			if(decorationData[i] == 2778) Decoration.decorations.add(new DecorationArtifact(2778, currentX, currentY, 10, 4, 13, 13, 1, 1, 2, "Quill")); // 151 - 2751
			if(decorationData[i] == 2779) Decoration.decorations.add(new DecorationArtifact(2779, currentX, currentY, 10, 5, 15, 15, 1, 1, 2, "Crystal Amulet")); // 151 - 2751
			if(decorationData[i] == 2780) Decoration.decorations.add(new DecorationArtifact(2780, currentX, currentY, 11, 4, 15, 11, 1, 1, 2, "Fabric")); // 151 - 2751
			if(decorationData[i] == 2781) Decoration.decorations.add(new DecorationArtifact(2781, currentX, currentY, 10, 3, 11, 16, 1, 1, 2, "Carpet")); // 151 - 2751
		}
		
		for(int i = 0; i < subArtifactData.length; i += 3){
			
			int currentX = subArtifactData[i + 1];
			int currentY = subArtifactData[i + 2];
			
			
			//Sub Artifact ID's 3000 - infinity
			
			//(id, the x value, the y value, the xTile, the yTile, artifact bar position, width(pixels), height(pixels),
			//spriteWidth(!pixels), spriteHeight(!pixels), render in front or behind blocks,  the name, levelSequencing class)
			
			if(subArtifactData[i] == 3000) Decoration.decorations.add(new DecorationSubArtifact(3000, currentX, currentY, 13, 8, 0, 10, 9, 1, 1, 2, "Artifact 1.1.1"));
			if(subArtifactData[i] == 3001) Decoration.decorations.add(new DecorationSubArtifact(3001, currentX, currentY, 14, 8, 1, 8, 7, 1, 1, 2, "Artifact 1.1.2"));
			if(subArtifactData[i] == 3002) Decoration.decorations.add(new DecorationSubArtifact(3002, currentX, currentY, 11, 9, 0, 5, 5, 1, 1, 2,"Artifact 1.2.1"));
			if(subArtifactData[i] == 3003) Decoration.decorations.add(new DecorationSubArtifact(3003, currentX, currentY, 12, 9, 1, 4, 5, 1, 1, 2, "Artifact 1.2.2"));
			if(subArtifactData[i] == 3004) Decoration.decorations.add(new DecorationSubArtifact(3004, currentX, currentY, 13, 9, 2, 5, 5, 1, 1, 2, "Artifact 1.2.3"));
			if(subArtifactData[i] == 3005) Decoration.decorations.add(new DecorationSubArtifact(3005, currentX, currentY, 14, 9, 3, 4, 5, 1, 1, 2, "Artifact 1.2.4"));
			if(subArtifactData[i] == 3006) Decoration.decorations.add(new DecorationSubArtifact(3006, currentX, currentY, 13, 10, 0, 10, 9, 1, 1, 2, "Artifact 1.3.1"));
			if(subArtifactData[i] == 3007) Decoration.decorations.add(new DecorationSubArtifact(3007, currentX, currentY, 14, 10, 1, 4, 4, 1, 1, 2, "Artifact 1.3.2"));
			if(subArtifactData[i] == 3008) Decoration.decorations.add(new DecorationSubArtifact(3008, currentX, currentY, 14, 11, 0, 6, 3, 1, 1, 2, "Artifact 1.4.1"));
			if(subArtifactData[i] == 3009) Decoration.decorations.add(new DecorationSubArtifact(3009, currentX, currentY, 13, 11, 1, 6, 5, 1, 1, 2, "Artifact 1.4.2"));
			if(subArtifactData[i] == 3010) Decoration.decorations.add(new DecorationSubArtifact(3010, currentX, currentY, 12, 11, 2, 6, 2, 1, 1, 2, "Artifact 1.4.3"));
			if(subArtifactData[i] == 3011) Decoration.decorations.add(new DecorationSubArtifact(3011, currentX, currentY, 14, 13, 0, 5, 4, 1, 1, 2, "Artifact 1.5.1"));
			if(subArtifactData[i] == 3012) Decoration.decorations.add(new DecorationSubArtifact(3012, currentX, currentY, 13, 13, 1, 5, 5, 1, 1, 2, "Artifact 1.5.2"));
			if(subArtifactData[i] == 3013) Decoration.decorations.add(new DecorationSubArtifact(3013, currentX, currentY, 12, 13, 2, 4, 4, 1, 1, 2, "Artifact 1.5.3"));
			if(subArtifactData[i] == 3014) Decoration.decorations.add(new DecorationSubArtifact(3014, currentX, currentY, 11, 13, 3, 4, 5, 1, 1, 2, "Artifact 1.5.4"));
			if(subArtifactData[i] == 3015) Decoration.decorations.add(new DecorationSubArtifact(3015, currentX, currentY, 14, 12, 0, 5, 5, 1, 1, 2, "Artifact 1.6.1"));
			if(subArtifactData[i] == 3016) Decoration.decorations.add(new DecorationSubArtifact(3016, currentX, currentY, 13, 12, 1, 5, 5, 1, 1, 2, "Artifact 1.6.2"));
			if(subArtifactData[i] == 3017) Decoration.decorations.add(new DecorationSubArtifact(3017, currentX, currentY, 14, 14, 0, 9, 5, 1, 1, 2, "Artifact 1.7.1"));
			if(subArtifactData[i] == 3018) Decoration.decorations.add(new DecorationSubArtifact(3018, currentX, currentY, 13, 14, 1, 4, 7, 1, 1, 2, "Artifact 1.7.2"));	
			if(subArtifactData[i] == 3019) Decoration.decorations.add(new DecorationSubArtifact(3019, currentX, currentY, 12, 14, 2, 5, 7, 1, 1, 2, "Artifact 1.7.3"));
			if(subArtifactData[i] == 3020) Decoration.decorations.add(new DecorationSubArtifact(3020, currentX, currentY, 14, 15, 0, 5, 5, 1, 1, 2, "Artifact 1.8.1"));
			if(subArtifactData[i] == 3021) Decoration.decorations.add(new DecorationSubArtifact(3021, currentX, currentY, 13, 15, 1, 5, 5, 1, 1, 2, "Artifact 1.8.2"));
			if(subArtifactData[i] == 3022) Decoration.decorations.add(new DecorationSubArtifact(3022, currentX, currentY, 12, 15, 2, 5, 10, 1, 1, 2, "Artifact 1.8.3"));
			if(subArtifactData[i] == 3023) Decoration.decorations.add(new DecorationSubArtifact(3023, currentX, currentY, 14, 7, 0, 8, 5, 1, 1, 2, "Artifact 1.9.1"));
			if(subArtifactData[i] == 3024) Decoration.decorations.add(new DecorationSubArtifact(3024, currentX, currentY, 13, 7, 1, 4, 4, 1, 1, 2, "Artifact 1.9.2"));
			if(subArtifactData[i] == 3025) Decoration.decorations.add(new DecorationSubArtifact(3025, currentX, currentY, 12, 7, 2, 8, 7, 1, 1, 2, "Artifact 1.9.3"));
			if(subArtifactData[i] == 3026) Decoration.decorations.add(new DecorationSubArtifact(3026, currentX, currentY, 14, 4, 0, 6, 4, 1, 1, 2, "Artifact 1.10.1"));
			if(subArtifactData[i] == 3027) Decoration.decorations.add(new DecorationSubArtifact(3027, currentX, currentY, 13, 4, 1, 6, 6, 1, 1, 2, "Artifact 1.10.2"));
			if(subArtifactData[i] == 3028) Decoration.decorations.add(new DecorationSubArtifact(3028, currentX, currentY, 14, 6, 0, 7, 6, 1, 1, 2, "Artifact 1.11.1"));
			if(subArtifactData[i] == 3029) Decoration.decorations.add(new DecorationSubArtifact(3029, currentX, currentY, 13, 6, 1, 5, 6, 1, 1, 2, "Artifact 1.11.2"));
			if(subArtifactData[i] == 3030) Decoration.decorations.add(new DecorationSubArtifact(3030, currentX, currentY, 12, 6, 2, 7, 6, 1, 1, 2, "Artifact 1.11.3"));
			if(subArtifactData[i] == 3031) Decoration.decorations.add(new DecorationSubArtifact(3031, currentX, currentY, 11, 6, 3, 4, 6, 1, 1, 2, "Artifact 1.11.4"));
			if(subArtifactData[i] == 3032) Decoration.decorations.add(new DecorationSubArtifact(3032, currentX, currentY, 14, 5, 0, 3, 7, 1, 1, 2, "Artifact 1.12.1"));
			if(subArtifactData[i] == 3033) Decoration.decorations.add(new DecorationSubArtifact(3033, currentX, currentY, 13, 5, 1, 9, 4, 1, 1, 2, "Artifact 1.12.2"));
			if(subArtifactData[i] == 3034) Decoration.decorations.add(new DecorationSubArtifact(3034, currentX, currentY, 12, 5, 2, 3, 6, 1, 1, 2, "Artifact 1.12.3"));
			if(subArtifactData[i] == 3035) Decoration.decorations.add(new DecorationSubArtifact(3035, currentX, currentY, 14, 16, 0, 2, 4, 1, 1, 2, "Artifact 1.13.1"));
			if(subArtifactData[i] == 3036) Decoration.decorations.add(new DecorationSubArtifact(3036, currentX, currentY, 13, 16, 1, 5, 4, 1, 1, 2, "Artifact 1.13.2"));
			if(subArtifactData[i] == 3037) Decoration.decorations.add(new DecorationSubArtifact(3037, currentX, currentY, 12, 16, 2, 5, 3, 1, 1, 2, "Artifact 1.13.3"));
			if(subArtifactData[i] == 3038) Decoration.decorations.add(new DecorationSubArtifact(3038, currentX, currentY, 14, 17, 0, 7, 7, 1, 1, 2, "Artifact 1.14.1"));
			if(subArtifactData[i] == 3039) Decoration.decorations.add(new DecorationSubArtifact(3039, currentX, currentY, 13, 17, 1, 5, 5, 1, 1, 2, "Artifact 1.14.2"));
			if(subArtifactData[i] == 3040) Decoration.decorations.add(new DecorationSubArtifact(3040, currentX, currentY, 12, 17, 2, 3, 3, 1, 1, 2, "Artifact 1.14.3"));
			if(subArtifactData[i] == 3041) Decoration.decorations.add(new DecorationSubArtifact(3041, currentX, currentY, 10, 17, 0, 6, 8, 1, 1, 2, "Artifact 1.15.1"));
			if(subArtifactData[i] == 3042) Decoration.decorations.add(new DecorationSubArtifact(3042, currentX, currentY, 9, 17, 1, 5, 8, 1, 1, 2, "Artifact 1.15.2"));
			if(subArtifactData[i] == 3043) Decoration.decorations.add(new DecorationSubArtifact(3043, currentX, currentY, 6, 14, 0, 7, 5, 1, 1, 2, "Artifact 1.16.1"));
			if(subArtifactData[i] == 3044) Decoration.decorations.add(new DecorationSubArtifact(3044, currentX, currentY, 5, 14, 1, 8, 8, 1, 1, 2, "Artifact 1.16.2"));
			if(subArtifactData[i] == 3045) Decoration.decorations.add(new DecorationSubArtifact(3045, currentX, currentY, 4, 14, 2, 7, 8, 1, 1, 2, "Artifact 1.16.3"));
			if(subArtifactData[i] == 3046) Decoration.decorations.add(new DecorationSubArtifact(3046, currentX, currentY, 3, 14, 3, 7, 7, 1, 1, 2, "Artifact 1.16.4"));
			if(subArtifactData[i] == 3047) Decoration.decorations.add(new DecorationSubArtifact(3047, currentX, currentY, 2, 14, 4, 8, 7, 1, 1, 2, "Artifact 1.16.5"));
			
			//2.1
			if(subArtifactData[i] == 3048) Decoration.decorations.add(new DecorationSubArtifact(3048, currentX, currentY, 14, 5, 0, 5, 5, 1, 1, 2, "Artifact 2.1.1"));
			if(subArtifactData[i] == 3049) Decoration.decorations.add(new DecorationSubArtifact(3049, currentX, currentY, 13, 5, 1, 5, 4, 1, 1, 2, "Artifact 2.1.2"));
			if(subArtifactData[i] == 3050) Decoration.decorations.add(new DecorationSubArtifact(3050, currentX, currentY, 12, 5, 2, 5, 4, 1, 1, 2, "Artifact 2.1.3"));
			if(subArtifactData[i] == 3051) Decoration.decorations.add(new DecorationSubArtifact(3051, currentX, currentY, 11, 5, 3, 5, 5, 1, 1, 2, "Artifact 2.1.4"));
			
			//2.2
			if(subArtifactData[i] == 3052) Decoration.decorations.add(new DecorationSubArtifact(3052, currentX, currentY, 14, 8, 0, 4, 4, 1, 1, 2, "Artifact 2.2.1"));
			if(subArtifactData[i] == 3053) Decoration.decorations.add(new DecorationSubArtifact(3053, currentX, currentY, 13, 8, 1, 3, 3, 1, 1, 2, "Artifact 2.2.2"));
			if(subArtifactData[i] == 3054) Decoration.decorations.add(new DecorationSubArtifact(3054, currentX, currentY, 12, 8, 2, 5, 5, 1, 1, 2, "Artifact 2.2.3"));
			
			//2.3
			if(subArtifactData[i] == 3055) Decoration.decorations.add(new DecorationSubArtifact(3055, currentX, currentY, 14, 10, 0, 3, 6, 1, 1, 2, "Artifact 2.3.1"));
			if(subArtifactData[i] == 3056) Decoration.decorations.add(new DecorationSubArtifact(3056, currentX, currentY, 13, 10, 1, 4, 6, 1, 1, 2, "Artifact 2.3.2"));
			if(subArtifactData[i] == 3057) Decoration.decorations.add(new DecorationSubArtifact(3057, currentX, currentY, 12, 10, 2, 5, 5, 1, 1, 2, "Artifact 2.3.3"));
		
			//2.4
			if(subArtifactData[i] == 3058) Decoration.decorations.add(new DecorationSubArtifact(3058, currentX, currentY, 9, 11, 0, 4, 8, 1, 1, 2, "Artifact 2.4.1"));
			if(subArtifactData[i] == 3059) Decoration.decorations.add(new DecorationSubArtifact(3059, currentX, currentY, 8, 11, 1, 4, 8, 1, 1, 2, "Artifact 2.4.2"));
			if(subArtifactData[i] == 3060) Decoration.decorations.add(new DecorationSubArtifact(3060, currentX, currentY, 7, 11, 2, 5, 8, 1, 1, 2, "Artifact 2.4.3"));
			if(subArtifactData[i] == 3061) Decoration.decorations.add(new DecorationSubArtifact(3061, currentX, currentY, 6, 11, 3, 5, 8, 1, 1, 2, "Artifact 2.4.4"));
			
			//2.5
			if(subArtifactData[i] == 3062) Decoration.decorations.add(new DecorationSubArtifact(3062, currentX, currentY, 14, 3, 0, 6, 6, 1, 1, 2, "Artifact 2.5.1"));
			if(subArtifactData[i] == 3063) Decoration.decorations.add(new DecorationSubArtifact(3063, currentX, currentY, 13, 3, 1, 14, 4, 1, 1, 2, "Artifact 2.5.2"));
			if(subArtifactData[i] == 3064) Decoration.decorations.add(new DecorationSubArtifact(3064, currentX, currentY, 12, 3, 2, 8, 6, 1, 1, 2, "Artifact 2.5.3"));
			
			//2.6
			if(subArtifactData[i] == 3065) Decoration.decorations.add(new DecorationSubArtifact(3065, currentX, currentY, 14, 6, 0, 9, 6, 1, 1, 2, "Artifact 2.6.1"));
			if(subArtifactData[i] == 3066) Decoration.decorations.add(new DecorationSubArtifact(3066, currentX, currentY, 13, 6, 1, 7, 6, 1, 1, 2, "Artifact 2.6.2"));
			
			//2.7
			if(subArtifactData[i] == 3067) Decoration.decorations.add(new DecorationSubArtifact(3067, currentX, currentY, 14, 7, 0, 3, 6, 1, 1, 2, "Artifact 2.7.1"));
			if(subArtifactData[i] == 3068) Decoration.decorations.add(new DecorationSubArtifact(3068, currentX, currentY, 13, 7, 1, 3, 6, 1, 1, 2, "Artifact 2.7.2"));
			
			//2.8
			if(subArtifactData[i] == 3069) Decoration.decorations.add(new DecorationSubArtifact(3069, currentX, currentY, 14, 9, 0, 2, 5, 1, 1, 2, "Artifact 2.8.1"));
			if(subArtifactData[i] == 3070) Decoration.decorations.add(new DecorationSubArtifact(3070, currentX, currentY, 13, 9, 1, 3, 5, 1, 1, 2, "Artifact 2.8.2"));
			
			//2.9
			if(subArtifactData[i] == 3071) Decoration.decorations.add(new DecorationSubArtifact(3071, currentX, currentY, 14, 11, 0, 7, 4, 1, 1, 2, "Artifact 2.9.1"));
			if(subArtifactData[i] == 3072) Decoration.decorations.add(new DecorationSubArtifact(3072, currentX, currentY, 13, 11, 1, 7, 4, 1, 1, 2, "Artifact 2.9.2"));
			if(subArtifactData[i] == 3073) Decoration.decorations.add(new DecorationSubArtifact(3073, currentX, currentY, 12, 11, 2, 7, 3, 1, 1, 2, "Artifact 2.9.3"));
			if(subArtifactData[i] == 3074) Decoration.decorations.add(new DecorationSubArtifact(3074, currentX, currentY, 11, 11, 3, 7, 3, 1, 1, 2, "Artifact 2.9.4"));
			
			//2.10
			if(subArtifactData[i] == 3075) Decoration.decorations.add(new DecorationSubArtifact(3075, currentX, currentY, 14, 12, 0, 7, 7, 1, 1, 2, "Artifact 2.10.1"));
			if(subArtifactData[i] == 3076) Decoration.decorations.add(new DecorationSubArtifact(3076, currentX, currentY, 13, 12, 1, 3, 4, 1, 1, 2, "Artifact 2.10.2"));
	
			//2.11
			if(subArtifactData[i] == 3077) Decoration.decorations.add(new DecorationSubArtifact(3077, currentX, currentY, 8, 13, 0, 11, 5, 1, 1, 2, "Artifact 2.11.1"));
			if(subArtifactData[i] == 3078) Decoration.decorations.add(new DecorationSubArtifact(3078, currentX, currentY, 7, 13, 1, 5, 5, 1, 1, 2, "Artifact 2.11.2"));
			if(subArtifactData[i] == 3079) Decoration.decorations.add(new DecorationSubArtifact(3079, currentX, currentY, 6, 13, 2, 6, 5, 1, 1, 2, "Artifact 2.11.3"));
			
			//2.12
			if(subArtifactData[i] == 3080) Decoration.decorations.add(new DecorationSubArtifact(3080, currentX, currentY, 10, 15, 0, 7, 4, 1, 1, 2, "Artifact 2.12.1"));
			if(subArtifactData[i] == 3081) Decoration.decorations.add(new DecorationSubArtifact(3081, currentX, currentY, 9, 15, 1, 9, 5, 1, 1, 2, "Artifact 2.12.2"));
			if(subArtifactData[i] == 3082) Decoration.decorations.add(new DecorationSubArtifact(3082, currentX, currentY, 9, 16, 2, 7, 5, 1, 1, 2, "Artifact 2.12.3"));
			if(subArtifactData[i] == 3083) Decoration.decorations.add(new DecorationSubArtifact(3083, currentX, currentY, 10, 16, 3, 7, 5, 1, 1, 2, "Artifact 2.12.4"));
			
			//2.13
			if(subArtifactData[i] == 3084) Decoration.decorations.add(new DecorationSubArtifact(3084, currentX, currentY, 10, 10, 0, 7, 7, 1, 1, 2, "Artifact 2.13.1"));
			if(subArtifactData[i] == 3085) Decoration.decorations.add(new DecorationSubArtifact(3085, currentX, currentY, 9, 10, 1, 7, 5, 1, 1, 2, "Artifact 2.13.2"));
			if(subArtifactData[i] == 3086) Decoration.decorations.add(new DecorationSubArtifact(3086, currentX, currentY, 8, 10, 2, 6, 7, 1, 1, 2, "Artifact 2.13.3"));
			//2.14
			if(subArtifactData[i] == 3087) Decoration.decorations.add(new DecorationSubArtifact(3087, currentX, currentY, 11, 12, 0, 9, 5, 1, 1, 2, "Artifact 2.14.1"));
			if(subArtifactData[i] == 3088) Decoration.decorations.add(new DecorationSubArtifact(3088, currentX, currentY, 10, 12, 1, 4, 5, 1, 1, 2, "Artifact 2.14.2"));
			if(subArtifactData[i] == 3089) Decoration.decorations.add(new DecorationSubArtifact(3089, currentX, currentY, 9, 12, 2, 5, 5, 1, 1, 2, "Artifact 2.14.3"));
			
			//2.15
			if(subArtifactData[i] == 3090) Decoration.decorations.add(new DecorationSubArtifact(3090, currentX, currentY, 14, 4, 0, 6, 6, 1, 1, 2, "Artifact 2.15.1"));
			if(subArtifactData[i] == 3091) Decoration.decorations.add(new DecorationSubArtifact(3091, currentX, currentY, 13, 4, 1, 12, 5, 1, 1, 2, "Artifact 2.15.2"));
			if(subArtifactData[i] == 3092) Decoration.decorations.add(new DecorationSubArtifact(3092, currentX, currentY, 12, 4, 2, 6, 6, 1, 1, 2, "Artifact 2.15.3"));
			
			//2.16
			if(subArtifactData[i] == 3093) Decoration.decorations.add(new DecorationSubArtifact(3093, currentX, currentY, 14, 13, 0, 3, 7, 1, 1, 2, "Artifact 2.16.1"));
			if(subArtifactData[i] == 3094) Decoration.decorations.add(new DecorationSubArtifact(3094, currentX, currentY, 13, 13, 1, 5, 5, 1, 1, 2, "Artifact 2.16.2"));
			if(subArtifactData[i] == 3095) Decoration.decorations.add(new DecorationSubArtifact(3095, currentX, currentY, 12, 13, 2, 4, 5, 1, 1, 2, "Artifact 2.16.3"));
			if(subArtifactData[i] == 3096) Decoration.decorations.add(new DecorationSubArtifact(3096, currentX, currentY, 11, 13, 3, 8, 9, 1, 1, 2, "Artifact 2.16.4"));
			if(subArtifactData[i] == 3097) Decoration.decorations.add(new DecorationSubArtifact(3097, currentX, currentY, 10, 13, 4, 5, 5, 1, 1, 2, "Artifact 2.16.5"));


		}
	}
	
	
	//THIS IS FOR RENDERING THE CENTER BLOCK TO THE EDITOR
	
	//!!!REMEMBER TO CHANGE THIS!!!
	
	public int maxBlockId = 100;
	public int maxDecorationId = 2064;
	public int maxArtifactId = 2781;
	public int maxSubArtifactId = 3097;
	
	//last block is not added to the registry due to maxBlockId
	
	public Block editorBlock(int id, int x, int y){
		
		
		if(id == 0) return new AirBlock (0, x, y);	
		
		//GrassBlocks
		if(id == 1) return new Block(1, x, y, 1, 0);
		if(id == 2) return new Block(2, x, y, 0, 0);
		if(id == 3) return new Block(3, x, y, 2, 0);
		if(id == 4) return new Block(4, x, y, 3, 0);
		if(id == 5) return new Block(5, x, y, 4, 0);
		if(id == 6) return new Block(6, x, y, 5, 0);
		if(id == 7) return new Block(7, x, y, 6, 0);
		if(id == 8) return new Block(8, x, y, 7, 0);
		if(id == 9) return new Block(9, x, y, 0, 1);
		if(id == 10) return new Block(10, x, y, 1, 1);
		if(id == 11) return new Block(11, x, y, 2, 1);
		if(id == 12) return new Block(12, x, y, 3, 1);
		if(id == 13) return new Block(13, x, y, 4, 1);
		if(id == 14) return new Block(14, x, y, 5, 1);
		if(id == 15) return new Block(15, x, y, 6, 1);
		if(id == 16) return new Block(16, x, y, 7, 1);
		if(id == 17) return new Block(17, x, y, 0, 2);
		if(id == 18) return new Block(18, x, y, 1, 2);
		if(id == 19) return new Block(19, x, y, 2, 2);
		if(id == 20) return new Block(20, x, y, 3, 2);
		if(id == 21) return new Block(21, x, y, 4, 2);
		if(id == 22) return new Block(22, x, y, 5, 2);
		if(id == 23) return new Block(23, x, y, 6, 2);
		if(id == 24) return new Block(24, x, y, 7, 2);
		if(id == 25) return new Block(25, x, y, 0, 3);
		if(id == 26) return new Block(26, x, y, 1, 3);
		if(id == 27) return new Block(27, x, y, 2, 3);
		if(id == 28) return new Block(28, x, y, 3, 3);
		if(id == 29) return new Block(29, x, y, 4, 3);
		if(id == 30) return new Block(30, x, y, 5, 3);
		if(id == 31) return new Block(31, x, y, 1, 4);
		if(id == 32) return new Block(32, x, y, 0, 4);
		if(id == 33) return new Block(33, x, y, 2, 4);
		if(id == 34) return new Block(34, x, y, 3, 4);
		if(id == 35) return new Block(35, x, y, 4, 4);
		if(id == 36) return new Block(36, x, y, 5, 4);
		if(id == 37) return new Block(37, x, y, 6, 4);
		if(id == 38) return new Block(38, x, y, 0, 5); 
		if(id == 39) return new Block(39, x, y, 6, 3); 
		if(id == 40) return new Block(40, x, y, 3, 5); 
		if(id == 41) return new Block(41, x, y, 1, 13); 
		if(id == 42) return new Block(42, x, y, 2, 13); 
		if(id == 43) return new Block(43, x, y, 3, 13); 
		if(id == 44) return new Block(44, x, y, 4, 13); 
		if(id == 45) return new Block(45, x, y, 5, 13); 
		if(id == 46) return new Block(46, x, y, 7, 4);
		if(id == 47) return new Block(47, x, y, 0, 0);
		if(id == 48) return new Block(48, x, y, 1, 0);
		if(id == 49) return new Block(49, x, y, 2, 0);
		if(id == 50) return new Block(50, x, y, 3, 0);
		if(id == 51) return new Block(51, x, y, 4, 0);
		if(id == 52) return new Block(52, x, y, 5, 0);
		if(id == 53) return new Block(53, x, y, 6, 0);
		if(id == 54) return new Block(54, x, y, 7, 0);
		if(id == 55) return new Block(55, x, y, 8, 0);
		if(id == 56) return new Block(56, x, y, 9, 0);
		if(id == 57) return new Block(57, x, y, 0, 1);
		if(id == 58) return new Block(58, x, y, 1, 1);
		if(id == 59) return new Block(59, x, y, 2, 1);
		if(id == 60) return new Block(60, x, y, 3, 1);
		if(id == 61) return new Block(61, x, y, 4, 1);
		if(id == 62) return new Block(62, x, y, 5, 1);
		if(id == 63) return new Block(63, x, y, 6, 1);
		if(id == 64) return new Block(64, x, y, 7, 1);
		if(id == 65) return new Block(65, x, y, 8, 1);
		if(id == 66) return new Block(66, x, y, 9, 1);
		if(id == 67) return new Block(67, x, y, 0, 2);
		if(id == 68) return new Block(68, x, y, 1, 2);
		if(id == 69) return new Block(69, x, y, 2, 2);
		if(id == 70) return new Block(70, x, y, 3, 2);
		if(id == 71) return new Block(71, x, y, 4, 2);
		if(id == 72) return new Block(72, x, y, 5, 2);
		if(id == 73) return new Block(73, x, y, 6, 2);
		if(id == 74) return new Block(74, x, y, 7, 2);
		if(id == 75) return new Block(75, x, y, 8, 2);
		if(id == 76) return new Block(76, x, y, 9, 2);
		if(id == 77) return new Block(77, x, y, 0, 3);
		if(id == 78) return new Block(78, x, y, 1, 3);
		if(id == 79) return new Block(79, x, y, 2, 3);
		if(id == 80) return new Block(80, x, y, 3, 3);
		if(id == 81) return new Block(81, x, y, 4, 3);
		if(id == 82) return new Block(82, x, y, 5, 3);
		if(id == 83) return new Block(83, x, y, 6, 3);
		if(id == 84) return new Block(84, x, y, 7, 3);
		if(id == 85) return new Block(85, x, y, 8, 3);
		if(id == 86) return new Block(86, x, y, 9, 3);
		if(id == 87) return new Block(87, x, y, 0, 4);
		if(id == 88) return new Block(88, x, y, 0, 9);
		if(id == 89) return new Block(89, x, y, 1, 9);
		if(id == 90) return new Block(90, x, y, 2, 9);
		if(id == 91) return new Block(91, x, y, 3, 9);
		if(id == 92) return new Block(92, x, y, 0, 10);
		if(id == 93) return new Block(93, x, y, 1, 10);
		if(id == 94) return new Block(94, x, y, 2, 10);
		if(id == 95) return new Block(95, x, y, 3, 10);
		if(id == 96) return new Block(96, x, y, 0, 8);
		if(id == 97) return new Block(97, x, y, 1, 8);
		if(id == 98) return new Block(98, x, y, 2, 8);
		if(id == 99) return new Block(99, x, y, 3, 8);
		if(id == 100) return new Block(100, x, y, 15, 24);
				
		return null;
	}
	
	public Decoration editorDecoration(int id, int currentX, int currentY){	
	
		if(id == 2000) return new Decoration(2000, currentX, currentY, 13, 0, 3, 3, 1); //Ship 100 - 2005
		if(id == 2001) return new Decoration(2001, currentX, currentY, 1, 6, 1, 1, 2); //ShortGrass  95 - 2000
		if(id == 2002) return new Decoration(2002, currentX, currentY, 0, 6, 1, 1, 2); //LongGrass 96 - 2001
		if(id == 2003) return new Decoration(2003, currentX, currentY, 8, 6, 1, 1, 2); //ShortGrass - 2 97 - 2002
		if(id == 2004) return new Decoration(2004, currentX, currentY, 3, 6, 1, 1, 2); //LongGrass - 2 98 - 2003
		if(id == 2005) return new Decoration(2005, currentX, currentY, 8, 5, 1, 1, 2); //Prickly Flower 99 - 2004
		if(id == 2006) return new Decoration(2006, currentX, currentY, 0, 8, 1, 2, 1); // 101 - 2006
		if(id == 2007) return new Decoration(2007, currentX, currentY, 2, 5, 1, 2, 1); //TallGrass 102 - 2007
		if(id == 2008) return new Decoration(2008, currentX, currentY, 6, 5, 2, 2, 2); //CurvedGrassLeft 103 - 2008
		if(id == 2009) return new Decoration(2009, currentX, currentY, 4, 5, 2, 2, 2); //CurvedGrassRight 104 - 2009
		if(id == 2010) return new Decoration(2010, currentX, currentY, 9, 5, 2, 2, 1); //MegaGrass 105 - 2010
		if(id == 2011) return new Decoration(2011, currentX, currentY, 9, 1, 1, 1, 1); //BladeGrass 105 - 2010
		if(id == 2012) return new Decoration(2012, currentX, currentY, 8, 4, 1, 1, 1); //BladeVine 105 - 2010
		if(id == 2013) return new Decoration(2013, currentX, currentY, 9, 3, 1, 2, 1); //TallBladeGrass 105 - 2010
		if(id == 2014) return new Decoration(2014, currentX, currentY, 8, 1, 1, 1, 1); //RegularVine 105 - 2010
		if(id == 2015) return new Decoration(2015, currentX, currentY, 8, 2, 1, 1, 1); //RegularVineFlower 105 - 2010
		if(id == 2016) return new Decoration(2016, currentX, currentY, 8, 3, 1, 1, 1); //TransitionVine 105 - 2010
		if(id == 2017) return new Decoration(2017, currentX, currentY, 10, 4, 1, 1, 1); // 101 - 2006
		if(id == 2018) return new Decoration(2018, currentX, currentY, 0, 10, 1, 1, 1);
		if(id == 2019) return new Decoration(2019, currentX, currentY, 1, 10, 1, 1, 1);
		if(id == 2020) return new Decoration(2020, currentX, currentY, 2, 10, 1, 1, 1);
		if(id == 2021) return new Decoration(2021, currentX, currentY, 3, 10, 1, 1, 1);
		if(id == 2022) return new Decoration(2022, currentX, currentY, 4, 10, 1, 1, 1);
		if(id == 2023) return new Decoration(2023, currentX, currentY, 5, 10, 1, 1, 1);
		if(id == 2024) return new Decoration(2024, currentX, currentY, 6, 10, 1, 1, 1);
		if(id == 2025) return new Decoration(2025, currentX, currentY, 7, 10, 1, 1, 1);
		if(id == 2026) return new Decoration(2026, currentX, currentY, 8, 10, 1, 1, 1);
		if(id == 2027) return new Decoration(2027, currentX, currentY, 1, 11, 1, 1, 1);
		if(id == 2028) return new Decoration(2028, currentX, currentY, 2, 11, 1, 1, 1);
		if(id == 2029) return new Decoration(2029, currentX, currentY, 3, 11, 1, 1, 1);
		if(id == 2030) return new Decoration(2030, currentX, currentY, 4, 11, 1, 1, 1);
		if(id == 2031) return new Decoration(2031, currentX, currentY, 5, 11, 1, 1, 1);
		if(id == 2032) return new Decoration(2032, currentX, currentY, 6, 11, 1, 1, 1);
		if(id == 2033) return new Decoration(2033, currentX, currentY, 7, 11, 1, 1, 1);
		if(id == 2034) return new Decoration(2034, currentX, currentY, 1, 12, 1, 1, 3);
		if(id == 2035) return new Decoration(2035, currentX, currentY, 3, 15, 3, 3, 1);
		if(id == 2036) return new Decoration(2036, currentX, currentY, 6, 15, 2, 3, 1);
		if(id == 2037) return new Decoration(2037, currentX, currentY, 8, 16, 1, 2, 1);
		if(id == 2038) return new Decoration(2038, currentX, currentY, 2, 16, 1, 2, 1);
		if(id == 2039) return new Tree(2039, currentX, currentY, -1, -1, 1, 1, 3);
		if(id == 2040) return new Tree(2040, currentX, currentY, -1, -1, 1, 1, 3);
		if(id == 2041) return new Tree(2041, currentX, currentY, -1, -1, 1, 1, 3);
		if(id == 2042) return new Decoration(2042, currentX, currentY, 0, 12, 1, 1, 2);
		if(id == 2043) return new Decoration(2043, currentX, currentY, 1, 12, 1, 1, 2);
		if(id == 2044) return new Decoration(2044, currentX, currentY, 0, 13, 1, 5, 2);
		if(id == 2045) return new Decoration(2045, currentX, currentY, 1, 13, 1, 5, 2);
		if(id == 2046) return new Decoration(2046, currentX, currentY, 2, 15, 1, 3, 2);
		if(id == 2047) return new Decoration(2047, currentX, currentY, 3, 15, 2, 2, 2);
		if(id == 2048) return new Decoration(2048, currentX, currentY, 3, 17, 4, 1, 2);
		if(id == 2049) return new Decoration(2049, currentX, currentY, 7, 17, 1, 1, 2);
		if(id == 2050) return new Decoration(2050, currentX, currentY, 5, 15, 2, 2, 2);
		if(id == 2051) return new Decoration(2051, currentX, currentY, 8, 15, 1, 3, 3);
		if(id == 2052) return new Decoration(2052, currentX, currentY, 9, 17, 1, 1, 3);
		if(id == 2053) return new Decoration(2053, currentX, currentY, 10, 17, 1, 1, 3);
		if(id == 2054) return new Decoration(2054, currentX, currentY, 11, 17, 1, 1, 3);
		if(id == 2055) return new Decoration(2055, currentX, currentY, 12, 15, 1, 3, 3);
		if(id == 2056) return new Decoration(2056, currentX, currentY, 3, 5, 1, 1, 1);
		if(id == 2057) return new Decoration(2057, currentX, currentY, 0, 11, 2, 1, 2);
		if(id == 2058) return new Decoration(2058, currentX, currentY, 4, 6, 1, 8, 3);
		if(id == 2059) return new Decoration(2059, currentX, currentY, 0, 20, 3, 4, 1);
		if(id == 2060) return new Decoration(2060, currentX, currentY, 3, 20, 3, 4, 1);
		if(id == 2061) return new Decoration(2061, currentX, currentY, 6, 20, 3, 4, 1);
		if(id == 2062) return new Decoration(2062, currentX, currentY, 4, 5, 1, 1, 1);
		if(id == 2063) return new Decoration(2063, currentX, currentY, 5, 6, 1, 1, 1);
		if(id == 2064) return new Decoration(2064, currentX, currentY, 10, 18, 5, 6, 1);
		
		return null;
	}
	
	public DecorationArtifact editorDecorationArtifact(int id, int currentX, int currentY){
		
		if(id == 2750) return new DecorationArtifact(2750, currentX, currentY, 8, 0, 10, 10, 1, 1, 2, "Arrow"); //150 - 2750
		if(id == 2751) return new DecorationArtifact(2751, currentX, currentY, 9, 0, 12, 12, 1, 1, 2, "Bow"); // 151 - 2751
		if(id == 2752) return new DecorationArtifact(2752, currentX, currentY, 11, 0, 10, 10, 1, 1, 2, "Sword"); // 151 - 2751
		if(id == 2753) return new DecorationArtifact(2753, currentX, currentY, 10, 0, 10, 10, 1, 1, 2, "Rusty Chestplate"); // 151 - 2751
		if(id == 2754) return new DecorationArtifact(2754, currentX, currentY, 12, 0, 11, 11, 1, 1, 2, "Hammer"); // 151 - 2751
		if(id == 2755) return new DecorationArtifact(2755, currentX, currentY, 10, 1, 12, 12, 1, 1, 2, "War Axe"); // 151 - 2751
		if(id == 2756) return new DecorationArtifact(2756, currentX, currentY, 11, 1, 13, 13, 1, 1, 2, "Moldy Wheel"); // 151 - 2751
		if(id == 2757) return new DecorationArtifact(2757, currentX, currentY, 12, 1, 10, 7, 1, 1, 2, "Dual Daggers"); // 151 - 2751
		if(id == 2758) return new DecorationArtifact(2758, currentX, currentY, 13, 3, 11, 11, 1, 1, 2, "Paper"); // 151 - 2751
		if(id == 2759) return new DecorationArtifact(2759, currentX, currentY, 11, 2, 10, 12, 1, 1, 2, "Bucket"); // 151 - 2751
		if(id == 2760) return new DecorationArtifact(2760, currentX, currentY, 10, 2, 11, 14, 1, 1, 2, "Lantern"); // 151 - 2751
		if(id == 2761) return new DecorationArtifact(2761, currentX, currentY, 12, 2, 8, 11,  1, 1, 2, "Boots"); // 151 - 2751
		if(id == 2762) return new DecorationArtifact(2762, currentX, currentY, 10, 3, 9, 10, 1, 1, 2, "Shield"); // 151 - 2751
		if(id == 2763) return new DecorationArtifact(2763, currentX, currentY, 11, 3, 13, 10, 1, 1, 2, "Scroll"); // 151 - 2751
		if(id == 2764) return new DecorationArtifact(2764, currentX, currentY, 12, 3, 13, 13, 1, 1, 2, "Cauldron"); // 151 - 2751
		if(id == 2765) return new DecorationArtifact(2765, currentX, currentY, 14, 3, 11, 11,  1, 1, 2, "Book"); // 151 - 2751
		
		if(id == 2766) return new DecorationArtifact(2766, currentX, currentY, 10, 0, 11, 10, 1, 1, 2, "Scythe"); // 151 - 2751
		if(id == 2767) return new DecorationArtifact(2767, currentX, currentY, 11, 1, 10, 12, 1, 1, 2, "Hoe"); // 151 - 2751
		if(id == 2768) return new DecorationArtifact(2768, currentX, currentY, 10, 2, 16, 15, 1, 1, 2, "Map"); // 151 - 2751
		if(id == 2769) return new DecorationArtifact(2769, currentX, currentY, 12, 0, 16, 16, 1, 1, 2, "Calender"); // 151 - 2751
		if(id == 2770) return new DecorationArtifact(2770, currentX, currentY, 11, 0, 14, 16, 1, 1, 2, "Piller"); // 151 - 2751
		if(id == 2771) return new DecorationArtifact(2771, currentX, currentY, 10, 1, 15, 13, 1, 1, 2, "Alter"); // 151 - 2751
		if(id == 2772) return new DecorationArtifact(2772, currentX, currentY, 12, 1, 11, 12, 1, 1, 2, "Clay Tablet"); // 151 - 2751
		if(id == 2773) return new DecorationArtifact(2773, currentX, currentY, 11, 2, 15, 12, 1, 1, 2, "Statue"); // 151 - 2751
		if(id == 2774) return new DecorationArtifact(2774, currentX, currentY, 12, 2, 11, 14, 1, 1, 2, "Painting"); // 151 - 2751
		if(id == 2775) return new DecorationArtifact(2775, currentX, currentY, 11, 3, 12, 15, 1, 1, 2, "Rake"); // 151 - 2751
		if(id == 2776) return new DecorationArtifact(2776, currentX, currentY, 9, 4, 10, 8, 1, 1, 2, "Axe"); // 151 - 2751
		if(id == 2777) return new DecorationArtifact(2777, currentX, currentY, 9, 5, 13, 13, 1, 1, 2, "Cutters"); // 151 - 2751
		if(id == 2778) return new DecorationArtifact(2778, currentX, currentY, 10, 4, 13, 13, 1, 1, 2, "Quill"); // 151 - 2751
		if(id == 2779) return new DecorationArtifact(2779, currentX, currentY, 10, 5, 15, 15, 1, 1, 2, "Crystal Amulet"); // 151 - 2751
		if(id == 2780) return new DecorationArtifact(2780, currentX, currentY, 11, 4, 15, 11, 1, 1, 2, "Fabric"); // 151 - 2751
		if(id == 2781) return new DecorationArtifact(2781, currentX, currentY, 10, 3, 11, 16, 1, 1, 2, "Carpet"); // 151 - 2751
		
		
		return null;
	}
	
	
	public DecorationSubArtifact editorDecorationSubArtifact(int id, int currentX, int currentY){

		
		if(id == 3000) return new DecorationSubArtifact(3000, currentX, currentY, 13, 8, 0, 10, 9, 1, 1, 2, "Artifact 1.1.1");
		if(id == 3001) return new DecorationSubArtifact(3001, currentX, currentY, 14, 8, 1, 8, 7, 1, 1, 2, "Artifact 1.1.2");
		if(id == 3002) return new DecorationSubArtifact(3002, currentX, currentY, 11, 9, 0, 5, 5, 1, 1, 2,"Artifact 1.2.1");
		if(id == 3003) return new DecorationSubArtifact(3003, currentX, currentY, 12, 9, 1, 4, 5, 1, 1, 2, "Artifact 1.2.2");
		if(id == 3004) return new DecorationSubArtifact(3004, currentX, currentY, 13, 9, 2, 5, 5, 1, 1, 2, "Artifact 1.2.3");
		if(id == 3005) return new DecorationSubArtifact(3005, currentX, currentY, 14, 9, 3, 4, 5, 1, 1, 2, "Artifact 1.2.4");
		if(id == 3006) return new DecorationSubArtifact(3006, currentX, currentY, 13, 10, 0, 10, 9, 1, 1, 2, "Artifact 1.3.1");
		if(id == 3007) return new DecorationSubArtifact(3007, currentX, currentY, 14, 10, 1, 4, 4, 1, 1, 2, "Artifact 1.3.2");
		if(id == 3008) return new DecorationSubArtifact(3008, currentX, currentY, 14, 11, 0, 6, 3, 1, 1, 2, "Artifact 1.4.1");
		if(id == 3009) return new DecorationSubArtifact(3009, currentX, currentY, 13, 11, 1, 6, 5, 1, 1, 2, "Artifact 1.4.2");
		if(id == 3010) return new DecorationSubArtifact(3010, currentX, currentY, 12, 11, 2, 6, 2, 1, 1, 2, "Artifact 1.4.3");
		if(id == 3011) return new DecorationSubArtifact(3011, currentX, currentY, 14, 13, 0, 5, 4, 1, 1, 2, "Artifact 1.5.1");
		if(id == 3012) return new DecorationSubArtifact(3012, currentX, currentY, 13, 13, 1, 5, 5, 1, 1, 2, "Artifact 1.5.2");
		if(id == 3013) return new DecorationSubArtifact(3013, currentX, currentY, 12, 13, 2, 4, 4, 1, 1, 2, "Artifact 1.5.3");
		if(id == 3014) return new DecorationSubArtifact(3014, currentX, currentY, 11, 13, 3, 4, 5, 1, 1, 2, "Artifact 1.5.4");
		if(id == 3015) return new DecorationSubArtifact(3015, currentX, currentY, 14, 12, 0, 5, 5, 1, 1, 2, "Artifact 1.6.1");
		if(id == 3016) return new DecorationSubArtifact(3016, currentX, currentY, 13, 12, 1, 5, 5, 1, 1, 2, "Artifact 1.6.2");
		if(id == 3017) return new DecorationSubArtifact(3017, currentX, currentY, 14, 14, 0, 9, 5, 1, 1, 2, "Artifact 1.7.1");
		if(id == 3018) return new DecorationSubArtifact(3018, currentX, currentY, 13, 14, 1, 4, 7, 1, 1, 2, "Artifact 1.7.2");	
		if(id == 3019) return new DecorationSubArtifact(3019, currentX, currentY, 12, 14, 2, 5, 7, 1, 1, 2, "Artifact 1.7.3");
		if(id == 3020) return new DecorationSubArtifact(3020, currentX, currentY, 14, 15, 0, 5, 5, 1, 1, 2, "Artifact 1.8.1");
		if(id == 3021) return new DecorationSubArtifact(3021, currentX, currentY, 13, 15, 1, 5, 5, 1, 1, 2, "Artifact 1.8.2");
		if(id == 3022) return new DecorationSubArtifact(3022, currentX, currentY, 12, 15, 2, 5, 10, 1, 1, 2, "Artifact 1.8.3");
		if(id == 3023) return new DecorationSubArtifact(3023, currentX, currentY, 14, 7, 0, 8, 5, 1, 1, 2, "Artifact 1.9.1");
		if(id == 3024) return new DecorationSubArtifact(3024, currentX, currentY, 13, 7, 1, 4, 4, 1, 1, 2, "Artifact 1.9.2");
		if(id == 3025) return new DecorationSubArtifact(3025, currentX, currentY, 12, 7, 2, 8, 7, 1, 1, 2, "Artifact 1.9.3");
		if(id == 3026) return new DecorationSubArtifact(3026, currentX, currentY, 14, 4, 0, 6, 4, 1, 1, 2, "Artifact 1.10.1");
		if(id == 3027) return new DecorationSubArtifact(3027, currentX, currentY, 13, 4, 1, 6, 6, 1, 1, 2, "Artifact 1.10.2");
		if(id == 3028) return new DecorationSubArtifact(3028, currentX, currentY, 14, 6, 0, 7, 6, 1, 1, 2, "Artifact 1.11.1");
		if(id == 3029) return new DecorationSubArtifact(3029, currentX, currentY, 13, 6, 1, 5, 6, 1, 1, 2, "Artifact 1.11.2");
		if(id == 3030) return new DecorationSubArtifact(3030, currentX, currentY, 12, 6, 2, 7, 6, 1, 1, 2, "Artifact 1.11.3");
		if(id == 3031) return new DecorationSubArtifact(3031, currentX, currentY, 11, 6, 3, 4, 6, 1, 1, 2, "Artifact 1.11.4");
		if(id == 3032) return new DecorationSubArtifact(3032, currentX, currentY, 14, 5, 0, 3, 7, 1, 1, 2, "Artifact 1.12.1");
		if(id == 3033) return new DecorationSubArtifact(3033, currentX, currentY, 13, 5, 1, 9, 4, 1, 1, 2, "Artifact 1.12.2");
		if(id == 3034) return new DecorationSubArtifact(3034, currentX, currentY, 12, 5, 2, 3, 6, 1, 1, 2, "Artifact 1.12.3");
		if(id == 3035) return new DecorationSubArtifact(3035, currentX, currentY, 14, 16, 0, 2, 4, 1, 1, 2, "Artifact 1.13.1");
		if(id == 3036) return new DecorationSubArtifact(3036, currentX, currentY, 13, 16, 1, 5, 4, 1, 1, 2, "Artifact 1.13.2");
		if(id == 3037) return new DecorationSubArtifact(3037, currentX, currentY, 12, 16, 2, 5, 3, 1, 1, 2, "Artifact 1.13.3");
		if(id == 3038) return new DecorationSubArtifact(3038, currentX, currentY, 14, 17, 0, 7, 7, 1, 1, 2, "Artifact 1.14.1");
		if(id == 3039) return new DecorationSubArtifact(3039, currentX, currentY, 13, 17, 1, 5, 5, 1, 1, 2, "Artifact 1.14.2");
		if(id == 3040) return new DecorationSubArtifact(3040, currentX, currentY, 12, 17, 2, 3, 3, 1, 1, 2, "Artifact 1.14.3");
		if(id == 3041) return new DecorationSubArtifact(3041, currentX, currentY, 10, 17, 0, 6, 8, 1, 1, 2, "Artifact 1.15.1");
		if(id == 3042) return new DecorationSubArtifact(3042, currentX, currentY, 9, 17, 1, 5, 8, 1, 1, 2, "Artifact 1.15.2");
		if(id == 3043) return new DecorationSubArtifact(3043, currentX, currentY, 6, 14, 0, 7, 5, 1, 1, 2, "Artifact 1.16.1");
		if(id == 3044) return new DecorationSubArtifact(3044, currentX, currentY, 5, 14, 1, 8, 8, 1, 1, 2, "Artifact 1.16.2");
		if(id == 3045) return new DecorationSubArtifact(3045, currentX, currentY, 4, 14, 2, 7, 8, 1, 1, 2, "Artifact 1.16.3");
		if(id == 3046) return new DecorationSubArtifact(3046, currentX, currentY, 3, 14, 3, 7, 7, 1, 1, 2, "Artifact 1.16.4");
		if(id == 3047) return new DecorationSubArtifact(3047, currentX, currentY, 2, 14, 4, 8, 7, 1, 1, 2, "Artifact 1.16.5");
		
		//2.1
		if(id == 3048) return new DecorationSubArtifact(3048, currentX, currentY, 14, 5, 0, 5, 5, 1, 1, 2, "Artifact 2.1.1");
		if(id == 3049) return new DecorationSubArtifact(3049, currentX, currentY, 13, 5, 1, 5, 4, 1, 1, 2, "Artifact 2.1.2");
		if(id == 3050) return new DecorationSubArtifact(3050, currentX, currentY, 12, 5, 2, 5, 4, 1, 1, 2, "Artifact 2.1.3");
		if(id == 3051) return new DecorationSubArtifact(3051, currentX, currentY, 11, 5, 3, 5, 5, 1, 1, 2, "Artifact 2.1.4");
		
		//2.2
		if(id == 3052) return new DecorationSubArtifact(3052, currentX, currentY, 14, 8, 0, 4, 4, 1, 1, 2, "Artifact 2.2.1");
		if(id == 3053) return new DecorationSubArtifact(3053, currentX, currentY, 13, 8, 1, 3, 3, 1, 1, 2, "Artifact 2.2.2");
		if(id == 3054) return new DecorationSubArtifact(3054, currentX, currentY, 12, 8, 2, 5, 5, 1, 1, 2, "Artifact 2.2.3");
		
		//2.3
		if(id == 3055) return new DecorationSubArtifact(3055, currentX, currentY, 14, 10, 0, 3, 6, 1, 1, 2, "Artifact 2.3.1");
		if(id == 3056) return new DecorationSubArtifact(3056, currentX, currentY, 13, 10, 1, 4, 6, 1, 1, 2, "Artifact 2.3.2");
		if(id == 3057) return new DecorationSubArtifact(3057, currentX, currentY, 12, 10, 2, 5, 5, 1, 1, 2, "Artifact 2.3.3");
	
		//2.4
		if(id == 3058) return new DecorationSubArtifact(3058, currentX, currentY, 9, 11, 0, 4, 8, 1, 1, 2, "Artifact 2.4.1");
		if(id == 3059) return new DecorationSubArtifact(3059, currentX, currentY, 8, 11, 1, 4, 8, 1, 1, 2, "Artifact 2.4.2");
		if(id == 3060) return new DecorationSubArtifact(3060, currentX, currentY, 7, 11, 2, 5, 8, 1, 1, 2, "Artifact 2.4.3");
		if(id == 3061) return new DecorationSubArtifact(3061, currentX, currentY, 6, 11, 3, 5, 8, 1, 1, 2, "Artifact 2.4.4");
		
		//2.5
		if(id == 3062) return new DecorationSubArtifact(3062, currentX, currentY, 14, 3, 0, 6, 6, 1, 1, 2, "Artifact 2.5.1");
		if(id == 3063) return new DecorationSubArtifact(3063, currentX, currentY, 13, 3, 1, 14, 4, 1, 1, 2, "Artifact 2.5.2");
		if(id == 3064) return new DecorationSubArtifact(3064, currentX, currentY, 12, 3, 2, 8, 6, 1, 1, 2, "Artifact 2.5.3");
		
		//2.6
		if(id == 3065) return new DecorationSubArtifact(3065, currentX, currentY, 14, 6, 0, 9, 6, 1, 1, 2, "Artifact 2.6.1");
		if(id == 3066) return new DecorationSubArtifact(3066, currentX, currentY, 13, 6, 1, 7, 6, 1, 1, 2, "Artifact 2.6.2");
		
		//2.7
		if(id == 3067) return new DecorationSubArtifact(3067, currentX, currentY, 14, 7, 0, 3, 6, 1, 1, 2, "Artifact 2.7.1");
		if(id == 3068) return new DecorationSubArtifact(3068, currentX, currentY, 13, 7, 1, 3, 6, 1, 1, 2, "Artifact 2.7.2");
		
		//2.8
		if(id == 3069) return new DecorationSubArtifact(3069, currentX, currentY, 14, 9, 0, 2, 5, 1, 1, 2, "Artifact 2.8.1");
		if(id == 3070) return new DecorationSubArtifact(3070, currentX, currentY, 13, 9, 1, 3, 5, 1, 1, 2, "Artifact 2.8.2");
		
		//2.9
		if(id == 3071) return new DecorationSubArtifact(3071, currentX, currentY, 14, 11, 0, 7, 4, 1, 1, 2, "Artifact 2.9.1");
		if(id == 3072) return new DecorationSubArtifact(3072, currentX, currentY, 13, 11, 1, 7, 4, 1, 1, 2, "Artifact 2.9.2");
		if(id == 3073) return new DecorationSubArtifact(3073, currentX, currentY, 12, 11, 2, 7, 3, 1, 1, 2, "Artifact 2.9.3");
		if(id == 3074) return new DecorationSubArtifact(3074, currentX, currentY, 11, 11, 3, 7, 3, 1, 1, 2, "Artifact 2.9.4");
		
		//2.10
		if(id == 3075) return new DecorationSubArtifact(3075, currentX, currentY, 14, 12, 0, 7, 7, 1, 1, 2, "Artifact 2.10.1");
		if(id == 3076) return new DecorationSubArtifact(3076, currentX, currentY, 13, 12, 1, 3, 4, 1, 1, 2, "Artifact 2.10.2");

		//2.11
		if(id == 3077) return new DecorationSubArtifact(3077, currentX, currentY, 8, 13, 0, 11, 5, 1, 1, 2, "Artifact 2.11.1");
		if(id == 3078) return new DecorationSubArtifact(3078, currentX, currentY, 7, 13, 1, 5, 5, 1, 1, 2, "Artifact 2.11.2");
		if(id == 3079) return new DecorationSubArtifact(3079, currentX, currentY, 6, 13, 2, 6, 5, 1, 1, 2, "Artifact 2.11.3");
		
		//2.12
		if(id == 3080) return new DecorationSubArtifact(3080, currentX, currentY, 10, 15, 0, 7, 4, 1, 1, 2, "Artifact 2.12.1");
		if(id == 3081) return new DecorationSubArtifact(3081, currentX, currentY, 9, 15, 1, 9, 5, 1, 1, 2, "Artifact 2.12.2");
		if(id == 3082) return new DecorationSubArtifact(3082, currentX, currentY, 9, 16, 2, 7, 5, 1, 1, 2, "Artifact 2.12.3");
		if(id == 3083) return new DecorationSubArtifact(3083, currentX, currentY, 10, 16, 3, 7, 5, 1, 1, 2, "Artifact 2.12.4");
		
		//2.13
		if(id == 3084) return new DecorationSubArtifact(3084, currentX, currentY, 10, 10, 0, 7, 7, 1, 1, 2, "Artifact 2.13.1");
		if(id == 3085) return new DecorationSubArtifact(3085, currentX, currentY, 9, 10, 1, 7, 5, 1, 1, 2, "Artifact 2.13.2");
		if(id == 3086) return new DecorationSubArtifact(3086, currentX, currentY, 8, 10, 2, 6, 7, 1, 1, 2, "Artifact 2.13.3");
		//2.14
		if(id == 3087) return new DecorationSubArtifact(3087, currentX, currentY, 11, 12, 0, 9, 5, 1, 1, 2, "Artifact 2.14.1");
		if(id == 3088) return new DecorationSubArtifact(3088, currentX, currentY, 10, 12, 1, 4, 5, 1, 1, 2, "Artifact 2.14.2");
		if(id == 3089) return new DecorationSubArtifact(3089, currentX, currentY, 9, 12, 2, 5, 5, 1, 1, 2, "Artifact 2.14.3");
		
		//2.15
		if(id == 3090) return new DecorationSubArtifact(3090, currentX, currentY, 14, 4, 0, 6, 6, 1, 1, 2, "Artifact 2.15.1");
		if(id == 3091) return new DecorationSubArtifact(3091, currentX, currentY, 13, 4, 1, 12, 5, 1, 1, 2, "Artifact 2.15.2");
		if(id == 3092) return new DecorationSubArtifact(3092, currentX, currentY, 12, 4, 2, 6, 6, 1, 1, 2, "Artifact 2.15.3");
		
		//2.16
		if(id == 3093) return new DecorationSubArtifact(3093, currentX, currentY, 14, 13, 0, 3, 7, 1, 1, 2, "Artifact 2.16.1");
		if(id == 3094) return new DecorationSubArtifact(3094, currentX, currentY, 13, 13, 1, 5, 5, 1, 1, 2, "Artifact 2.16.2");
		if(id == 3095) return new DecorationSubArtifact(3095, currentX, currentY, 12, 13, 2, 4, 5, 1, 1, 2, "Artifact 2.16.3");
		if(id == 3096) return new DecorationSubArtifact(3096, currentX, currentY, 11, 13, 3, 8, 9, 1, 1, 2, "Artifact 2.16.4");
		if(id == 3097) return new DecorationSubArtifact(3097, currentX, currentY, 10, 13, 4, 5, 5, 1, 1, 2, "Artifact 2.16.5");
		
		
		return null;
	}
	
	
	public static void main(String[] args){
		Main levelEd = new Main();
	
		
		JFrame frame = new JFrame();
		frame.setSize(400, 100);
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ActionListener act = new ActionListener(){
			
			public void actionPerformed(ActionEvent action) {
				if(action.getSource().equals(confirm)){
					waiting = false;
					frame.dispose();
				}
			}
			
		};
		frame.add(nameLabel);
		frame.add(nameText);
		frame.add(loadLabel);
		frame.add(loadText);
		frame.add(widthLabel);
		frame.add(widthText);
		frame.add(heightLabel);
		frame.add(heightText);
		frame.add(confirm);
		
		confirm.addActionListener(act);
		
		frame.setVisible(true);
		
		loadText.setText("Level2-2");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2,dim.height / 2 - frame.getSize().height / 2);
		
		while(waiting){
			try{
				Thread.sleep(20);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		JFrame editorFrame = new JFrame();
		editorFrame.setResizable(false);
		editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editorFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		editorFrame.setUndecorated(true);
		editorFrame.setLayout(new BorderLayout());
		editorFrame.add(levelEd, BorderLayout.CENTER);
		editorFrame.setLocationRelativeTo(null);
		editorFrame.setVisible(true);
		
		levelEd.start();
	}
}
