package com.murdermaninc.level;

import com.mudermaninc.entity.Entity;
import com.mudermaninc.entity.Player;
import com.murdermaninc.blocks.*;
import com.murdermaninc.decorations.*;
import com.murdermaninc.decorations.artifacts.*;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.main.InputManager;


public class Level {
	
	private LevelData LevelD;
	private Screen screen;
	public int levelWidth, levelHeight;
	private int[] levelData;
	private int[] decorationData;
	private int[] subArtifactData;
	private int[] collectedArtifacts;
	public int[][] subArtifactsXYTiles;
	public int[] completedSubArtifact;
	public int totalAmountOfSub;
	public String name;
	public int levelNumber;
	public int worldNumber;
	private LevelSequencing levelSequence;
	private SubArtifactBar artifactBar;
	
	public long levelTimer;
	public long endTime;
	public boolean deletedEntity = false;
	//private LightScreen lightScreen;
	
	
	public Level(String loadLevel, int levelNumber, int worldNumber, int width, int height, LevelSequencing levelS){
		LevelD = new LevelData(loadLevel);
		levelSequence = levelS;
		name = loadLevel;
		this.levelNumber = levelNumber;
		this.worldNumber = worldNumber;
		
		levelData = LevelD.getLevelData();
		levelWidth = LevelD.getLevelWidth();
		levelHeight = LevelD.getLevelHeight();
		
		//I don't know how efficient it is to create a screen that is the size of the entire level itself.
		screen = new Screen(width, height, 0, 0, 0);
		
		if(worldNumber == 1){
			if(levelNumber > 8 && levelNumber <= 12){
				screen.loadSpriteSheet("/iconsdarker.png", "Icons");
			}else{
				screen.loadSpriteSheet("/icons.png", "Icons");
			}
		}else if(worldNumber == 2){
			screen.loadSpriteSheet("/icons_Forest.png", "Icons");
			screen.loadSpriteSheet("/trees_Decoration.png", "Trees");
		}
		screen.loadSpriteSheet("/font.png", "font");
		screen.loadSpriteSheet("/PlayerSprites.png", "player");
		
		
		//lightScreen = new LightScreen(levelWidth, levelHeight, width, height);
		Block.blocks = new Block[levelWidth * levelHeight];
		
		decorationData = LevelD.getDecorationData();
		subArtifactData = LevelD.getSubArtifact();
		collectedArtifacts = LevelD.getCollectionArtifacts();
		subArtifactsXYTiles = new int[collectedArtifacts.length][];
		
		artifactBar = new SubArtifactBar(LevelD.getTotalAmountOfSub(), width);
		totalAmountOfSub = LevelD.getTotalAmountOfSub();
		
		Entity.entities.clear();
		levelS.setLevel(this);
		setBlocks();
		
		completedSubArtifact = getCompletedSubArtifact();
		//lightScreen.loadLight(false);
		levelTimer = System.currentTimeMillis();
		
	}
	
	
	public void setBlocks(){
		
		//NOTICE All xTile and yTiles are for the currentLevels icons spriteSheet so values can overlap
		
		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){
				
				//Sets up the ids for the blocks and the block type (id, x, y, xTile, yTile) for each new block type (a block type shares similar characteristics such as sound)
				//REMEMBER Blocks are specificaly for objects that will have collisions and also objects that cast shadows otherwise it is put in decorations
				if(levelData[x + y * levelWidth] == 0) Block.blocks[x + y * levelWidth] = new AirBlock(0, x, y);
				
				//GrassBlocks
				if(levelData[x + y * levelWidth] == 1) Block.blocks[x + y * levelWidth] = new GrassBlock(1, x, y, 1, 0);
				if(levelData[x + y * levelWidth] == 2) Block.blocks[x + y * levelWidth] = new GrassBlock(2, x, y, 0, 0);
				if(levelData[x + y * levelWidth] == 3) Block.blocks[x + y * levelWidth] = new GrassBlock(3, x, y, 2, 0);
				if(levelData[x + y * levelWidth] == 4) Block.blocks[x + y * levelWidth] = new GrassBlock(4, x, y, 3, 0);
				if(levelData[x + y * levelWidth] == 5) Block.blocks[x + y * levelWidth] = new GrassBlock(5, x, y, 4, 0);
				if(levelData[x + y * levelWidth] == 6) Block.blocks[x + y * levelWidth] = new GrassBlock(6, x, y, 5, 0);
				if(levelData[x + y * levelWidth] == 7) Block.blocks[x + y * levelWidth] = new GrassBlock(7, x, y, 6, 0);
				if(levelData[x + y * levelWidth] == 8) Block.blocks[x + y * levelWidth] = new GrassBlock(8, x, y, 7, 0);
				if(levelData[x + y * levelWidth] == 9) Block.blocks[x + y * levelWidth] = new GrassBlock(9, x, y, 0, 1);
				if(levelData[x + y * levelWidth] == 10) Block.blocks[x + y * levelWidth] = new GrassBlock(10, x, y, 1, 1);
				if(levelData[x + y * levelWidth] == 11) Block.blocks[x + y * levelWidth] = new GrassBlock(11, x, y, 2, 1);
				if(levelData[x + y * levelWidth] == 12) Block.blocks[x + y * levelWidth] = new GrassBlock(12, x, y, 3, 1);
				if(levelData[x + y * levelWidth] == 13) Block.blocks[x + y * levelWidth] = new GrassBlock(13, x, y, 4, 1);
				if(levelData[x + y * levelWidth] == 14) Block.blocks[x + y * levelWidth] = new GrassBlock(14, x, y, 5, 1);
				if(levelData[x + y * levelWidth] == 15) Block.blocks[x + y * levelWidth] = new GrassBlock(15, x, y, 6, 1);
				if(levelData[x + y * levelWidth] == 16) Block.blocks[x + y * levelWidth] = new GrassBlock(16, x, y, 7, 1);
				if(levelData[x + y * levelWidth] == 17) Block.blocks[x + y * levelWidth] = new GrassBlock(17, x, y, 0, 2);
				if(levelData[x + y * levelWidth] == 18) Block.blocks[x + y * levelWidth] = new GrassBlock(18, x, y, 1, 2);
				if(levelData[x + y * levelWidth] == 19) Block.blocks[x + y * levelWidth] = new GrassBlock(19, x, y, 2, 2);
				if(levelData[x + y * levelWidth] == 20) Block.blocks[x + y * levelWidth] = new GrassBlock(20, x, y, 3, 2);
				if(levelData[x + y * levelWidth] == 21) Block.blocks[x + y * levelWidth] = new GrassBlock(21, x, y, 4, 2);
				if(levelData[x + y * levelWidth] == 22) Block.blocks[x + y * levelWidth] = new GrassBlock(22, x, y, 5, 2);
				if(levelData[x + y * levelWidth] == 23) Block.blocks[x + y * levelWidth] = new GrassBlock(23, x, y, 6, 2);
				if(levelData[x + y * levelWidth] == 24) Block.blocks[x + y * levelWidth] = new GrassBlock(24, x, y, 7, 2);
				if(levelData[x + y * levelWidth] == 25) Block.blocks[x + y * levelWidth] = new GrassBlock(25, x, y, 0, 3);
				if(levelData[x + y * levelWidth] == 26) Block.blocks[x + y * levelWidth] = new GrassBlock(26, x, y, 1, 3);
				if(levelData[x + y * levelWidth] == 27) Block.blocks[x + y * levelWidth] = new GrassBlock(27, x, y, 2, 3);
				if(levelData[x + y * levelWidth] == 28) Block.blocks[x + y * levelWidth] = new GrassBlock(28, x, y, 3, 3);
				if(levelData[x + y * levelWidth] == 29) Block.blocks[x + y * levelWidth] = new GrassBlock(29, x, y, 4, 3);
				if(levelData[x + y * levelWidth] == 30) Block.blocks[x + y * levelWidth] = new GrassBlock(30, x, y, 5, 3);
				if(levelData[x + y * levelWidth] == 31) Block.blocks[x + y * levelWidth] = new GrassBlock(31, x, y, 1, 4);
				if(levelData[x + y * levelWidth] == 32) Block.blocks[x + y * levelWidth] = new GrassBlock(32, x, y, 0, 4);
				if(levelData[x + y * levelWidth] == 33) Block.blocks[x + y * levelWidth] = new GrassBlock(33, x, y, 2, 4);
				if(levelData[x + y * levelWidth] == 34) Block.blocks[x + y * levelWidth] = new GrassBlock(34, x, y, 3, 4);
				if(levelData[x + y * levelWidth] == 35) Block.blocks[x + y * levelWidth] = new GrassBlock(35, x, y, 4, 4);
				if(levelData[x + y * levelWidth] == 36) Block.blocks[x + y * levelWidth] = new GrassBlock(36, x, y, 5, 4);
				if(levelData[x + y * levelWidth] == 37) Block.blocks[x + y * levelWidth] = new GrassBlock(37, x, y, 6, 4);
				if(levelData[x + y * levelWidth] == 38) Block.blocks[x + y * levelWidth] = new RockBlock(38, x, y, 0, 5); 
				if(levelData[x + y * levelWidth] == 39) Block.blocks[x + y * levelWidth] = new DirtBlock(39, x, y, 6, 3); 
				if(levelData[x + y * levelWidth] == 40) Block.blocks[x + y * levelWidth] = new Block(40, x, y, 3, 5); 
				if(levelData[x + y * levelWidth] == 41) Block.blocks[x + y * levelWidth] = new Block(41, x, y, 1, 13); 
				if(levelData[x + y * levelWidth] == 42) Block.blocks[x + y * levelWidth] = new Block(42, x, y, 2, 13); 
				if(levelData[x + y * levelWidth] == 43) Block.blocks[x + y * levelWidth] = new Block(43, x, y, 3, 13); 
				if(levelData[x + y * levelWidth] == 44) Block.blocks[x + y * levelWidth] = new Block(44, x, y, 4, 13); 
				if(levelData[x + y * levelWidth] == 45) Block.blocks[x + y * levelWidth] = new Block(45, x, y, 5, 13); 
				if(levelData[x + y * levelWidth] == 46) Block.blocks[x + y * levelWidth] = new GrassBlock(46, x, y, 7, 4);
				
				
				if(levelData[x + y * levelWidth] == 47) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(47, x, y, 0, 0);
				if(levelData[x + y * levelWidth] == 48) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(48, x, y, 1, 0);
				if(levelData[x + y * levelWidth] == 49) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(49, x, y, 2, 0);
				if(levelData[x + y * levelWidth] == 50) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(50, x, y, 3, 0);
				if(levelData[x + y * levelWidth] == 51) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(51, x, y, 4, 0);
				if(levelData[x + y * levelWidth] == 52) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(52, x, y, 5, 0);
				if(levelData[x + y * levelWidth] == 53) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(53, x, y, 6, 0);
				if(levelData[x + y * levelWidth] == 54) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(54, x, y, 7, 0);
				if(levelData[x + y * levelWidth] == 55) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(55, x, y, 8, 0);
				if(levelData[x + y * levelWidth] == 56) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(56, x, y, 9, 0);
				if(levelData[x + y * levelWidth] == 57) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(57, x, y, 0, 1);
				if(levelData[x + y * levelWidth] == 58) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(58, x, y, 1, 1);
				if(levelData[x + y * levelWidth] == 59) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(59, x, y, 2, 1);
				if(levelData[x + y * levelWidth] == 60) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(60, x, y, 3, 1);
				if(levelData[x + y * levelWidth] == 61) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(61, x, y, 4, 1);
				if(levelData[x + y * levelWidth] == 62) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(62, x, y, 5, 1);
				if(levelData[x + y * levelWidth] == 63) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(63, x, y, 6, 1);
				if(levelData[x + y * levelWidth] == 64) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(64, x, y, 7, 1);
				if(levelData[x + y * levelWidth] == 65) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(65, x, y, 8, 1);
				if(levelData[x + y * levelWidth] == 66) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(66, x, y, 9, 1);
				if(levelData[x + y * levelWidth] == 67) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(67, x, y, 0, 2);
				if(levelData[x + y * levelWidth] == 68) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(68, x, y, 1, 2);
				if(levelData[x + y * levelWidth] == 69) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(69, x, y, 2, 2);
				if(levelData[x + y * levelWidth] == 70) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(70, x, y, 3, 2);
				if(levelData[x + y * levelWidth] == 71) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(71, x, y, 4, 2);
				if(levelData[x + y * levelWidth] == 72) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(72, x, y, 5, 2);
				if(levelData[x + y * levelWidth] == 73) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(73, x, y, 6, 2);
				if(levelData[x + y * levelWidth] == 74) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(74, x, y, 7, 2);
				if(levelData[x + y * levelWidth] == 75) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(75, x, y, 8, 2);
				if(levelData[x + y * levelWidth] == 76) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(76, x, y, 9, 2);
				if(levelData[x + y * levelWidth] == 77) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(77, x, y, 0, 3);
				if(levelData[x + y * levelWidth] == 78) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(78, x, y, 1, 3);
				if(levelData[x + y * levelWidth] == 79) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(79, x, y, 2, 3);
				if(levelData[x + y * levelWidth] == 80) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(80, x, y, 3, 3);
				if(levelData[x + y * levelWidth] == 81) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(81, x, y, 4, 3);
				if(levelData[x + y * levelWidth] == 82) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(82, x, y, 5, 3);
				if(levelData[x + y * levelWidth] == 83) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(83, x, y, 6, 3);
				if(levelData[x + y * levelWidth] == 84) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(84, x, y, 7, 3);
				if(levelData[x + y * levelWidth] == 85) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(85, x, y, 8, 3);
				if(levelData[x + y * levelWidth] == 86) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(86, x, y, 9, 3);
				if(levelData[x + y * levelWidth] == 87) Block.blocks[x + y * levelWidth] = new ForestGrassBlock(87, x, y, 0, 4);	
				
				
				if(levelData[x + y * levelWidth] == 88) Block.blocks[x + y * levelWidth] = new TempleBlock(88, x, y, 0, 9);
				if(levelData[x + y * levelWidth] == 89) Block.blocks[x + y * levelWidth] = new TempleBlock(89, x, y, 1, 9);
				if(levelData[x + y * levelWidth] == 90) Block.blocks[x + y * levelWidth] = new TempleBlock(90, x, y, 2, 9);
				if(levelData[x + y * levelWidth] == 91) Block.blocks[x + y * levelWidth] = new TempleBlock(91, x, y, 3, 9);
				if(levelData[x + y * levelWidth] == 92) Block.blocks[x + y * levelWidth] = new TempleBlock(92, x, y, 0, 10);
				if(levelData[x + y * levelWidth] == 93) Block.blocks[x + y * levelWidth] = new TempleBlock(93, x, y, 1, 10);
				if(levelData[x + y * levelWidth] == 94) Block.blocks[x + y * levelWidth] = new TempleBlock(94, x, y, 2, 10);
				if(levelData[x + y * levelWidth] == 95) Block.blocks[x + y * levelWidth] = new TempleBlock(95, x, y, 3, 10);
				if(levelData[x + y * levelWidth] == 96) Block.blocks[x + y * levelWidth] = new LeafBlock(96, x, y, 0, 8);
				if(levelData[x + y * levelWidth] == 97) Block.blocks[x + y * levelWidth] = new LeafBlock(97, x, y, 1, 8);
				if(levelData[x + y * levelWidth] == 98) Block.blocks[x + y * levelWidth] = new LeafBlock(98, x, y, 2, 8);
				if(levelData[x + y * levelWidth] == 99) Block.blocks[x + y * levelWidth] = new LeafBlock(99, x, y, 3, 8);
				
			}
		}
		
		for(int i = 0; i < Block.blocks.length; i++){
			Block.blocks[i].loadData(screen);
		}
		
		
		Decoration.decorations.clear();
		Decoration.decorationsAnimated.clear();
		for(int i = 0; i < decorationData.length; i+=3){
			//Sets the id for the decoration and also certain parameters  (id, x, y, leftMostXTile, topMostYTile, spriteWidth(tiles), spriteHeight(tiles),
			// render in front or behind blocks (true = front, false = behind), animated or not (true = animated, false = not animated), xTile (artifacts only), yTile (artifacts only),
			// name (artifacts only))
			//(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean render, boolean animated, boolean isDangerous)
			
			//REMEMBER Animations are added in the actual class as a replacement for the render method
			
			//Decorations ID's     2000 - 2750
			
			//public Decoration(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, boolean render){
			
			//render 1 = infront of all objects including player
			//render 2 = infront of player but behind blocks
			//render 3 = behind all objects including player
			
			int currentX = decorationData[i + 1];
			int currentY = decorationData[i + 2];
			
	
			if(decorationData[i] == 2000) Decoration.decorations.add(new Decoration(2000, currentX, currentY, 13, 0, 3, 3, 1)); //Ship 100 - 2005
			if(decorationData[i] == 2001) Decoration.decorations.add(new Decoration(2001, currentX, currentY, 1, 6, 1, 1, 2)); //ShortGrass  95 - 2000
			if(decorationData[i] == 2002) Decoration.decorations.add(new Decoration(2002, currentX, currentY, 0, 6, 1, 1, 2)); //LongGrass 96 - 2001
			if(decorationData[i] == 2003) Decoration.decorations.add(new Decoration(2003, currentX, currentY, 8, 6, 1, 1, 2)); //ShortGrass - 2 97 - 2002
			if(decorationData[i] == 2004) Decoration.decorations.add(new Decoration(2004, currentX, currentY, 3, 6, 1, 1, 2)); //LongGrass - 2 98 - 2003
			if(decorationData[i] == 2005) Decoration.decorations.add(new Decoration(2005, currentX, currentY, 8, 5, 1, 1, 2)); //Prickly Flower 99 - 2004
			if(decorationData[i] == 2006) Decoration.decorations.add(new Sunflower(2006, currentX, currentY, 0, 8, 1, 2, 1)); // 101 - 2006
			if(decorationData[i] == 2007) Decoration.decorations.add(new Decoration(2007, currentX, currentY, 2, 5, 1, 2, 1)); //TallGrass 102 - 2007
			if(decorationData[i] == 2008) Decoration.decorations.add(new Decoration(2008, currentX, currentY, 6, 5, 2, 2, 2)); //CurvedGrassLeft 103 - 2008
			if(decorationData[i] == 2009) Decoration.decorations.add(new Decoration(2009, currentX, currentY, 4, 5, 2, 2, 2)); //CurvedGrassRight 104 - 2009
			if(decorationData[i] == 2010) Decoration.decorations.add(new Decoration(2010, currentX, currentY, 9, 5, 2, 2, 1)); //MegaGrass 105 - 2010
			if(decorationData[i] == 2011) Decoration.decorations.add(new DeadlyDecoration(2011, currentX, currentY, 9, 1, 1, 1, 1, "deflat", 0, 0, 0, 0)); //BladeGrass 105 - 2010
			if(decorationData[i] == 2012) Decoration.decorations.add(new DeadlyDecoration(2012, currentX, currentY, 8, 4, 1, 1, 1, "deflat", 0, 0, 0, 0)); //BladeVine 105 - 2010
			if(decorationData[i] == 2013) Decoration.decorations.add(new DeadlyDecoration(2013, currentX, currentY, 9, 3, 1, 2, 1, "deflat", 0, 0, 0, 0)); //TallBladeGrass 105 - 2010
			if(decorationData[i] == 2014) Decoration.decorations.add(new Decoration(2014, currentX, currentY, 8, 1, 1, 1, 1)); //RegularVine 105 - 2010
			if(decorationData[i] == 2015) Decoration.decorations.add(new Decoration(2015, currentX, currentY, 8, 2, 1, 1, 1)); //RegularVineFlower 105 - 2010
			if(decorationData[i] == 2016) Decoration.decorations.add(new DeadlyDecoration(2016, currentX, currentY, 8, 3, 1, 1, 1, "deflat", 0, 0, 0, 0)); //TransitionVine 105 - 2010
			if(decorationData[i] == 2017) Decoration.decorations.add(new LumenFlower(2017, currentX, currentY, 10, 4, 1, 1, 1)); // 101 - 2006
			if(decorationData[i] == 2018) Decoration.decorations.add(new Water(2018, currentX, currentY, 0, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "stand"));
			if(decorationData[i] == 2019) Decoration.decorations.add(new Water(2019, currentX, currentY, 1, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2020) Decoration.decorations.add(new Water(2020, currentX, currentY, 2, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2021) Decoration.decorations.add(new Water(2021, currentX, currentY, 3, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2022) Decoration.decorations.add(new Water(2022, currentX, currentY, 4, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2023) Decoration.decorations.add(new Water(2023, currentX, currentY, 5, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2024) Decoration.decorations.add(new Water(2024, currentX, currentY, 6, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2025) Decoration.decorations.add(new Water(2025, currentX, currentY, 7, 10, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowRight"));
			if(decorationData[i] == 2026) Decoration.decorations.add(new Water(2026, currentX, currentY, 8, 10, 1, 1, 1, "deflat", 0, 0, 0, 0, "vertFlow"));
			if(decorationData[i] == 2027) Decoration.decorations.add(new Water(2027, currentX, currentY, 1, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2028) Decoration.decorations.add(new Water(2028, currentX, currentY, 2, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2029) Decoration.decorations.add(new Water(2029, currentX, currentY, 3, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2030) Decoration.decorations.add(new Water(2030, currentX, currentY, 4, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2031) Decoration.decorations.add(new Water(2031, currentX, currentY, 5, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2032) Decoration.decorations.add(new Water(2032, currentX, currentY, 6, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2033) Decoration.decorations.add(new Water(2033, currentX, currentY, 7, 11, 1, 1, 1, "deflat", 0, 0, 2, 0, "horiFlowLeft"));
			if(decorationData[i] == 2034) Decoration.decorations.add(new WaterBehind(2034, currentX, currentY, 1, 12, 1, 1, 3));
			if(decorationData[i] == 2035) Decoration.decorations.add(new Decoration(2035, currentX, currentY, 3, 15, 3, 3, 1));
			if(decorationData[i] == 2036) Decoration.decorations.add(new Decoration(2036, currentX, currentY, 6, 15, 2, 3, 1));
			if(decorationData[i] == 2037) Decoration.decorations.add(new MortemFlower(2037, currentX, currentY, 8, 16, 1, 2, 1, "deflat", 5, 5, 0, 0, false));
			if(decorationData[i] == 2038) Decoration.decorations.add(new MortemFlower(2038, currentX, currentY, 2, 16, 1, 2, 1, "deflat", 5, 5, 0, 0, true));
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
			if(decorationData[i] == 2052) Decoration.decorations.add(new ClimableVine(2052, currentX, currentY, 9, 17, 1, 1, 3));
			if(decorationData[i] == 2053) Decoration.decorations.add(new ClimableVine(2053, currentX, currentY, 10, 17, 1, 1, 3));
			if(decorationData[i] == 2054) Decoration.decorations.add(new ClimableVine(2054, currentX, currentY, 11, 17, 1, 1, 3));
			if(decorationData[i] == 2055) Decoration.decorations.add(new Decoration(2055, currentX, currentY, 12, 15, 1, 3, 3));
			if(decorationData[i] == 2056) Decoration.decorations.add(new DeadlyDecoration(2056, currentX, currentY, 3, 5, 1, 1, 1, "poison", 0, 1, 5, 0));
			if(decorationData[i] == 2057) Decoration.decorations.add(new Decoration(2057, currentX, currentY, 0, 11, 2, 1, 2));
			if(decorationData[i] == 2058) Decoration.decorations.add(new VerticalClimableVine(2058, currentX, currentY, 4, 6, 1, 8, 3));
			
			//Artifact ID's    2750 - 3000
			
			if(decorationData[i] == 2750) Decoration.decorations.add(new DecorationArtifact(2750, currentX, currentY, 8, 0, 10, 10, 1, 1, 2, "Arrow", levelSequence)); //150 - 2750
			if(decorationData[i] == 2751) Decoration.decorations.add(new DecorationArtifact(2751, currentX, currentY, 9, 0, 12, 12, 1, 1, 2, "Bow", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2752) Decoration.decorations.add(new DecorationArtifact(2752, currentX, currentY, 11, 0, 10, 10, 1, 1, 2, "Sword", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2753) Decoration.decorations.add(new DecorationArtifact(2753, currentX, currentY, 10, 0, 10, 10, 1, 1, 2, "Rusty Chestplate", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2754) Decoration.decorations.add(new DecorationArtifact(2754, currentX, currentY, 12, 0, 11, 11, 1, 1, 2, "Hammer", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2755) Decoration.decorations.add(new DecorationArtifact(2755, currentX, currentY, 10, 1, 12, 12, 1, 1, 2, "War Axe", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2756) Decoration.decorations.add(new DecorationArtifact(2756, currentX, currentY, 11, 1, 13, 13, 1, 1, 2, "Moldy Wheel", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2757) Decoration.decorations.add(new DecorationArtifact(2757, currentX, currentY, 12, 1, 10, 7, 1, 1, 2, "Dual Daggers", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2758) Decoration.decorations.add(new DecorationArtifact(2758, currentX, currentY, 13, 3, 11, 11, 1, 1, 2, "Paper", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2759) Decoration.decorations.add(new DecorationArtifact(2759, currentX, currentY, 11, 2, 10, 12, 1, 1, 2, "Bucket", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2760) Decoration.decorations.add(new DecorationArtifact(2760, currentX, currentY, 10, 2, 11, 14, 1, 1, 2, "Lantern", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2761) Decoration.decorations.add(new DecorationArtifact(2761, currentX, currentY, 12, 2, 8, 11,  1, 1, 2, "Boots", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2762) Decoration.decorations.add(new DecorationArtifact(2762, currentX, currentY, 10, 3, 9, 10, 1, 1, 2, "Shield", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2763) Decoration.decorations.add(new DecorationArtifact(2763, currentX, currentY, 11, 3, 13, 10, 1, 1, 2, "Scroll", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2764) Decoration.decorations.add(new DecorationArtifact(2764, currentX, currentY, 12, 3, 13, 13, 1, 1, 2, "Cauldron", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2765) Decoration.decorations.add(new DecorationArtifact(2765, currentX, currentY, 14, 3, 11, 11,  1, 1, 2, "Book", levelSequence)); // 151 - 2751
			
			if(decorationData[i] == 2766) Decoration.decorations.add(new DecorationArtifact(2766, currentX, currentY, 10, 0, 11, 10, 1, 1, 2, "Scythe", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2767) Decoration.decorations.add(new DecorationArtifact(2767, currentX, currentY, 11, 1, 10, 12, 1, 1, 2, "Hoe", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2768) Decoration.decorations.add(new DecorationArtifact(2768, currentX, currentY, 10, 2, 16, 15, 1, 1, 2, "Map", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2769) Decoration.decorations.add(new DecorationArtifact(2769, currentX, currentY, 12, 0, 16, 16, 1, 1, 2, "Calender", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2770) Decoration.decorations.add(new DecorationArtifact(2770, currentX, currentY, 11, 0, 14, 16, 1, 1, 2, "Piller", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2771) Decoration.decorations.add(new DecorationArtifact(2771, currentX, currentY, 10, 1, 15, 13, 1, 1, 2, "Alter", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2772) Decoration.decorations.add(new DecorationArtifact(2772, currentX, currentY, 12, 1, 11, 12, 1, 1, 2, "Clay Tablet", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2773) Decoration.decorations.add(new DecorationArtifact(2773, currentX, currentY, 11, 2, 15, 12, 1, 1, 2, "Statue", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2774) Decoration.decorations.add(new DecorationArtifact(2774, currentX, currentY, 12, 2, 11, 14, 1, 1, 2, "Painting", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2775) Decoration.decorations.add(new DecorationArtifact(2775, currentX, currentY, 11, 3, 12, 15, 1, 1, 2, "Rake", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2776) Decoration.decorations.add(new DecorationArtifact(2776, currentX, currentY, 9, 4, 10, 8, 1, 1, 2, "Axe", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2777) Decoration.decorations.add(new DecorationArtifact(2777, currentX, currentY, 9, 5, 13, 13, 1, 1, 2, "Cutters", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2778) Decoration.decorations.add(new DecorationArtifact(2778, currentX, currentY, 10, 4, 13, 13, 1, 1, 2, "Quill", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2779) Decoration.decorations.add(new DecorationArtifact(2779, currentX, currentY, 10, 5, 15, 15, 1, 1, 2, "Crystal Amulet", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2780) Decoration.decorations.add(new DecorationArtifact(2780, currentX, currentY, 11, 4, 15, 11, 1, 1, 2, "Fabric", levelSequence)); // 151 - 2751
			if(decorationData[i] == 2781) Decoration.decorations.add(new DecorationArtifact(2781, currentX, currentY, 10, 3, 11, 16, 1, 1, 2, "Carpet", levelSequence)); // 151 - 2751
		}
		
		for(int i = 0; i < subArtifactData.length; i += 3){
			
			int currentX = subArtifactData[i + 1];
			int currentY = subArtifactData[i + 2];
			
			
			//Sub Artifact ID's 3000 - infinity
			
			//(id, the x value, the y value, the xTile, the yTile, artifact bar position, width(pixels), height(pixels),
			//spriteWidth(!pixels), spriteHeight(!pixels), render in front or behind blocks,  the name, levelSequencing class)
			
			if(subArtifactData[i] == 3000) Decoration.decorations.add(new DecorationSubArtifact(3000, currentX, currentY, 13, 8, 0, 10, 9, 1, 1, 2, "Artifact 1.1.1", levelSequence));
			if(subArtifactData[i] == 3001) Decoration.decorations.add(new DecorationSubArtifact(3001, currentX, currentY, 14, 8, 1, 8, 7, 1, 1, 2, "Artifact 1.1.2", levelSequence));
			if(subArtifactData[i] == 3002) Decoration.decorations.add(new DecorationSubArtifact(3002, currentX, currentY, 11, 9, 0, 5, 5, 1, 1, 2,"Artifact 1.2.1", levelSequence));
			if(subArtifactData[i] == 3003) Decoration.decorations.add(new DecorationSubArtifact(3003, currentX, currentY, 12, 9, 1, 4, 5, 1, 1, 2, "Artifact 1.2.2", levelSequence));
			if(subArtifactData[i] == 3004) Decoration.decorations.add(new DecorationSubArtifact(3004, currentX, currentY, 13, 9, 2, 5, 5, 1, 1, 2, "Artifact 1.2.3", levelSequence));
			if(subArtifactData[i] == 3005) Decoration.decorations.add(new DecorationSubArtifact(3005, currentX, currentY, 14, 9, 3, 4, 5, 1, 1, 2, "Artifact 1.2.4", levelSequence));
			if(subArtifactData[i] == 3006) Decoration.decorations.add(new DecorationSubArtifact(3006, currentX, currentY, 13, 10, 0, 10, 9, 1, 1, 2, "Artifact 1.3.1", levelSequence));
			if(subArtifactData[i] == 3007) Decoration.decorations.add(new DecorationSubArtifact(3007, currentX, currentY, 14, 10, 1, 4, 4, 1, 1, 2, "Artifact 1.3.2", levelSequence));
			if(subArtifactData[i] == 3008) Decoration.decorations.add(new DecorationSubArtifact(3008, currentX, currentY, 14, 11, 0, 6, 3, 1, 1, 2, "Artifact 1.4.1", levelSequence));
			if(subArtifactData[i] == 3009) Decoration.decorations.add(new DecorationSubArtifact(3009, currentX, currentY, 13, 11, 1, 6, 5, 1, 1, 2, "Artifact 1.4.2", levelSequence));
			if(subArtifactData[i] == 3010) Decoration.decorations.add(new DecorationSubArtifact(3010, currentX, currentY, 12, 11, 2, 6, 2, 1, 1, 2, "Artifact 1.4.3", levelSequence));	
			if(subArtifactData[i] == 3011) Decoration.decorations.add(new DecorationSubArtifact(3011, currentX, currentY, 14, 13, 0, 5, 4, 1, 1, 2, "Artifact 1.5.1", levelSequence));
			if(subArtifactData[i] == 3012) Decoration.decorations.add(new DecorationSubArtifact(3012, currentX, currentY, 13, 13, 1, 5, 5, 1, 1, 2, "Artifact 1.5.2", levelSequence));
			if(subArtifactData[i] == 3013) Decoration.decorations.add(new DecorationSubArtifact(3013, currentX, currentY, 12, 13, 2, 4, 4, 1, 1, 2, "Artifact 1.5.3", levelSequence));
			if(subArtifactData[i] == 3014) Decoration.decorations.add(new DecorationSubArtifact(3014, currentX, currentY, 11, 13, 3, 4, 5, 1, 1, 2, "Artifact 1.5.4", levelSequence));
			if(subArtifactData[i] == 3015) Decoration.decorations.add(new DecorationSubArtifact(3015, currentX, currentY, 14, 12, 0, 5, 5, 1, 1, 2, "Artifact 1.6.1", levelSequence));
			if(subArtifactData[i] == 3016) Decoration.decorations.add(new DecorationSubArtifact(3016, currentX, currentY, 13, 12, 1, 5, 5, 1, 1, 2, "Artifact 1.6.2", levelSequence));
			if(subArtifactData[i] == 3017) Decoration.decorations.add(new DecorationSubArtifact(3017, currentX, currentY, 14, 14, 0, 9, 5, 1, 1, 2, "Artifact 1.7.1", levelSequence));
			if(subArtifactData[i] == 3018) Decoration.decorations.add(new DecorationSubArtifact(3018, currentX, currentY, 13, 14, 1, 4, 7, 1, 1, 2, "Artifact 1.7.2", levelSequence));	
			if(subArtifactData[i] == 3019) Decoration.decorations.add(new DecorationSubArtifact(3019, currentX, currentY, 12, 14, 2, 5, 7, 1, 1, 2, "Artifact 1.7.3", levelSequence));
			if(subArtifactData[i] == 3020) Decoration.decorations.add(new DecorationSubArtifact(3020, currentX, currentY, 14, 15, 0, 5, 5, 1, 1, 2, "Artifact 1.8.1", levelSequence));
			if(subArtifactData[i] == 3021) Decoration.decorations.add(new DecorationSubArtifact(3021, currentX, currentY, 13, 15, 1, 5, 5, 1, 1, 2, "Artifact 1.8.2", levelSequence));
			if(subArtifactData[i] == 3022) Decoration.decorations.add(new DecorationSubArtifact(3022, currentX, currentY, 12, 15, 2, 5, 10, 1, 1, 2, "Artifact 1.8.3", levelSequence));
			if(subArtifactData[i] == 3023) Decoration.decorations.add(new DecorationSubArtifact(3023, currentX, currentY, 14, 7, 0, 8, 5, 1, 1, 2, "Artifact 1.9.1", levelSequence));
			if(subArtifactData[i] == 3024) Decoration.decorations.add(new DecorationSubArtifact(3024, currentX, currentY, 13, 7, 1, 4, 4, 1, 1, 2, "Artifact 1.9.2", levelSequence));
			if(subArtifactData[i] == 3025) Decoration.decorations.add(new DecorationSubArtifact(3025, currentX, currentY, 12, 7, 2, 8, 7, 1, 1, 2, "Artifact 1.9.3", levelSequence));
			if(subArtifactData[i] == 3026) Decoration.decorations.add(new DecorationSubArtifact(3026, currentX, currentY, 14, 4, 0, 6, 4, 1, 1, 2, "Artifact 1.10.1", levelSequence));
			if(subArtifactData[i] == 3027) Decoration.decorations.add(new DecorationSubArtifact(3027, currentX, currentY, 13, 4, 1, 6, 6, 1, 1, 2, "Artifact 1.10.2", levelSequence));
			if(subArtifactData[i] == 3028) Decoration.decorations.add(new DecorationSubArtifact(3028, currentX, currentY, 14, 6, 0, 7, 6, 1, 1, 2, "Artifact 1.11.1", levelSequence));
			if(subArtifactData[i] == 3029) Decoration.decorations.add(new DecorationSubArtifact(3029, currentX, currentY, 13, 6, 1, 5, 6, 1, 1, 2, "Artifact 1.11.2", levelSequence));
			if(subArtifactData[i] == 3030) Decoration.decorations.add(new DecorationSubArtifact(3030, currentX, currentY, 12, 6, 2, 7, 6, 1, 1, 2, "Artifact 1.11.3", levelSequence));
			if(subArtifactData[i] == 3031) Decoration.decorations.add(new DecorationSubArtifact(3031, currentX, currentY, 11, 6, 3, 4, 6, 1, 1, 2, "Artifact 1.11.4", levelSequence));
			if(subArtifactData[i] == 3032) Decoration.decorations.add(new DecorationSubArtifact(3032, currentX, currentY, 14, 5, 0, 3, 7, 1, 1, 2, "Artifact 1.12.1", levelSequence));
			if(subArtifactData[i] == 3033) Decoration.decorations.add(new DecorationSubArtifact(3033, currentX, currentY, 13, 5, 1, 9, 4, 1, 1, 2, "Artifact 1.12.2", levelSequence));
			if(subArtifactData[i] == 3034) Decoration.decorations.add(new DecorationSubArtifact(3034, currentX, currentY, 12, 5, 2, 3, 6, 1, 1, 2, "Artifact 1.12.3", levelSequence));
			if(subArtifactData[i] == 3035) Decoration.decorations.add(new DecorationSubArtifact(3035, currentX, currentY, 14, 16, 0, 2, 4, 1, 1, 2, "Artifact 1.13.1", levelSequence));
			if(subArtifactData[i] == 3036) Decoration.decorations.add(new DecorationSubArtifact(3036, currentX, currentY, 13, 16, 1, 5, 4, 1, 1, 2, "Artifact 1.13.2", levelSequence));
			if(subArtifactData[i] == 3037) Decoration.decorations.add(new DecorationSubArtifact(3037, currentX, currentY, 12, 16, 2, 5, 3, 1, 1, 2, "Artifact 1.13.3", levelSequence));
			if(subArtifactData[i] == 3038) Decoration.decorations.add(new DecorationSubArtifact(3038, currentX, currentY, 14, 17, 0, 7, 7, 1, 1, 2, "Artifact 1.14.1", levelSequence));
			if(subArtifactData[i] == 3039) Decoration.decorations.add(new DecorationSubArtifact(3039, currentX, currentY, 13, 17, 1, 5, 5, 1, 1, 2, "Artifact 1.14.2", levelSequence));
			if(subArtifactData[i] == 3040) Decoration.decorations.add(new DecorationSubArtifact(3040, currentX, currentY, 12, 17, 2, 3, 3, 1, 1, 2, "Artifact 1.14.3", levelSequence));
			if(subArtifactData[i] == 3041) Decoration.decorations.add(new DecorationSubArtifact(3041, currentX, currentY, 10, 17, 0, 6, 8, 1, 1, 2, "Artifact 1.15.1", levelSequence));
			if(subArtifactData[i] == 3042) Decoration.decorations.add(new DecorationSubArtifact(3042, currentX, currentY, 9, 17, 1, 5, 8, 1, 1, 2, "Artifact 1.15.2", levelSequence));
			if(subArtifactData[i] == 3043) Decoration.decorations.add(new DecorationSubArtifact(3043, currentX, currentY, 6, 14, 0, 7, 5, 1, 1, 2, "Artifact 1.16.1", levelSequence));
			if(subArtifactData[i] == 3044) Decoration.decorations.add(new DecorationSubArtifact(3044, currentX, currentY, 5, 14, 1, 8, 8, 1, 1, 2, "Artifact 1.16.2", levelSequence));
			if(subArtifactData[i] == 3045) Decoration.decorations.add(new DecorationSubArtifact(3045, currentX, currentY, 4, 14, 2, 7, 8, 1, 1, 2, "Artifact 1.16.3", levelSequence));
			if(subArtifactData[i] == 3046) Decoration.decorations.add(new DecorationSubArtifact(3046, currentX, currentY, 3, 14, 3, 7, 7, 1, 1, 2, "Artifact 1.16.4", levelSequence));
			if(subArtifactData[i] == 3047) Decoration.decorations.add(new DecorationSubArtifact(3047, currentX, currentY, 2, 14, 4, 8, 7, 1, 1, 2, "Artifact 1.16.5", levelSequence));

			//2.1
			if(subArtifactData[i] == 3048) Decoration.decorations.add(new DecorationSubArtifact(3048, currentX, currentY, 14, 5, 0, 5, 5, 1, 1, 2, "Artifact 2.1.1", levelSequence));
			if(subArtifactData[i] == 3049) Decoration.decorations.add(new DecorationSubArtifact(3049, currentX, currentY, 13, 5, 1, 5, 4, 1, 1, 2, "Artifact 2.1.2", levelSequence));
			if(subArtifactData[i] == 3050) Decoration.decorations.add(new DecorationSubArtifact(3050, currentX, currentY, 12, 5, 2, 5, 4, 1, 1, 2, "Artifact 2.1.3", levelSequence));
			if(subArtifactData[i] == 3051) Decoration.decorations.add(new DecorationSubArtifact(3051, currentX, currentY, 11, 5, 3, 5, 5, 1, 1, 2, "Artifact 2.1.4", levelSequence));
			
			//2.2
			if(subArtifactData[i] == 3052) Decoration.decorations.add(new DecorationSubArtifact(3052, currentX, currentY, 14, 8, 0, 4, 4, 1, 1, 2, "Artifact 2.2.1", levelSequence));
			if(subArtifactData[i] == 3053) Decoration.decorations.add(new DecorationSubArtifact(3053, currentX, currentY, 13, 8, 1, 3, 3, 1, 1, 2, "Artifact 2.2.2", levelSequence));
			if(subArtifactData[i] == 3054) Decoration.decorations.add(new DecorationSubArtifact(3054, currentX, currentY, 12, 8, 2, 5, 5, 1, 1, 2, "Artifact 2.2.3", levelSequence));
			
			//2.3
			if(subArtifactData[i] == 3055) Decoration.decorations.add(new DecorationSubArtifact(3055, currentX, currentY, 14, 10, 0, 3, 6, 1, 1, 2, "Artifact 2.3.1", levelSequence));
			if(subArtifactData[i] == 3056) Decoration.decorations.add(new DecorationSubArtifact(3056, currentX, currentY, 13, 10, 1, 4, 6, 1, 1, 2, "Artifact 2.3.2", levelSequence));
			if(subArtifactData[i] == 3057) Decoration.decorations.add(new DecorationSubArtifact(3057, currentX, currentY, 12, 10, 2, 5, 5, 1, 1, 2, "Artifact 2.3.3", levelSequence));
		
			//2.4
			if(subArtifactData[i] == 3058) Decoration.decorations.add(new DecorationSubArtifact(3058, currentX, currentY, 9, 11, 0, 4, 8, 1, 1, 2, "Artifact 2.4.1", levelSequence));
			if(subArtifactData[i] == 3059) Decoration.decorations.add(new DecorationSubArtifact(3059, currentX, currentY, 8, 11, 1, 4, 8, 1, 1, 2, "Artifact 2.4.2", levelSequence));
			if(subArtifactData[i] == 3060) Decoration.decorations.add(new DecorationSubArtifact(3060, currentX, currentY, 7, 11, 2, 5, 8, 1, 1, 2, "Artifact 2.4.3", levelSequence));
			if(subArtifactData[i] == 3061) Decoration.decorations.add(new DecorationSubArtifact(3061, currentX, currentY, 6, 11, 3, 5, 8, 1, 1, 2, "Artifact 2.4.4", levelSequence));
			
			//2.5
			if(subArtifactData[i] == 3062) Decoration.decorations.add(new DecorationSubArtifact(3062, currentX, currentY, 14, 3, 0, 6, 6, 1, 1, 2, "Artifact 2.5.1", levelSequence));
			if(subArtifactData[i] == 3063) Decoration.decorations.add(new DecorationSubArtifact(3063, currentX, currentY, 13, 3, 1, 14, 4, 1, 1, 2, "Artifact 2.5.2", levelSequence));
			if(subArtifactData[i] == 3064) Decoration.decorations.add(new DecorationSubArtifact(3064, currentX, currentY, 12, 3, 2, 8, 6, 1, 1, 2, "Artifact 2.5.3", levelSequence));
			
			//2.6
			if(subArtifactData[i] == 3065) Decoration.decorations.add(new DecorationSubArtifact(3065, currentX, currentY, 14, 6, 0, 9, 6, 1, 1, 2, "Artifact 2.6.1", levelSequence));
			if(subArtifactData[i] == 3066) Decoration.decorations.add(new DecorationSubArtifact(3066, currentX, currentY, 13, 6, 1, 7, 6, 1, 1, 2, "Artifact 2.6.2", levelSequence));
			
			//2.7
			if(subArtifactData[i] == 3067) Decoration.decorations.add(new DecorationSubArtifact(3067, currentX, currentY, 14, 7, 0, 3, 6, 1, 1, 2, "Artifact 2.7.1", levelSequence));
			if(subArtifactData[i] == 3068) Decoration.decorations.add(new DecorationSubArtifact(3068, currentX, currentY, 13, 7, 1, 3, 6, 1, 1, 2, "Artifact 2.7.2", levelSequence));
			
			//2.8
			if(subArtifactData[i] == 3069) Decoration.decorations.add(new DecorationSubArtifact(3069, currentX, currentY, 14, 9, 0, 2, 5, 1, 1, 2, "Artifact 2.8.1", levelSequence));
			if(subArtifactData[i] == 3070) Decoration.decorations.add(new DecorationSubArtifact(3070, currentX, currentY, 13, 9, 1, 3, 5, 1, 1, 2, "Artifact 2.8.2", levelSequence));
			
			//2.9
			if(subArtifactData[i] == 3071) Decoration.decorations.add(new DecorationSubArtifact(3071, currentX, currentY, 14, 11, 0, 7, 4, 1, 1, 2, "Artifact 2.9.1", levelSequence));
			if(subArtifactData[i] == 3072) Decoration.decorations.add(new DecorationSubArtifact(3072, currentX, currentY, 13, 11, 1, 7, 4, 1, 1, 2, "Artifact 2.9.2", levelSequence));
			if(subArtifactData[i] == 3073) Decoration.decorations.add(new DecorationSubArtifact(3073, currentX, currentY, 12, 11, 2, 7, 3, 1, 1, 2, "Artifact 2.9.3", levelSequence));
			if(subArtifactData[i] == 3074) Decoration.decorations.add(new DecorationSubArtifact(3074, currentX, currentY, 11, 11, 3, 7, 3, 1, 1, 2, "Artifact 2.9.4", levelSequence));
			
			//2.10
			if(subArtifactData[i] == 3075) Decoration.decorations.add(new DecorationSubArtifact(3075, currentX, currentY, 14, 12, 0, 7, 7, 1, 1, 2, "Artifact 2.10.1", levelSequence));
			if(subArtifactData[i] == 3076) Decoration.decorations.add(new DecorationSubArtifact(3076, currentX, currentY, 13, 12, 1, 3, 4, 1, 1, 2, "Artifact 2.10.2", levelSequence));
	
			//2.11
			if(subArtifactData[i] == 3077) Decoration.decorations.add(new DecorationSubArtifact(3077, currentX, currentY, 8, 13, 0, 11, 5, 1, 1, 2, "Artifact 2.11.1", levelSequence));
			if(subArtifactData[i] == 3078) Decoration.decorations.add(new DecorationSubArtifact(3078, currentX, currentY, 7, 13, 1, 5, 5, 1, 1, 2, "Artifact 2.11.2", levelSequence));
			if(subArtifactData[i] == 3079) Decoration.decorations.add(new DecorationSubArtifact(3079, currentX, currentY, 6, 13, 2, 6, 5, 1, 1, 2, "Artifact 2.11.3", levelSequence));
			
			//2.12
			if(subArtifactData[i] == 3080) Decoration.decorations.add(new DecorationSubArtifact(3080, currentX, currentY, 10, 15, 0, 7, 4, 1, 1, 2, "Artifact 2.12.1", levelSequence));
			if(subArtifactData[i] == 3081) Decoration.decorations.add(new DecorationSubArtifact(3081, currentX, currentY, 9, 15, 1, 9, 5, 1, 1, 2, "Artifact 2.12.2", levelSequence));
			if(subArtifactData[i] == 3082) Decoration.decorations.add(new DecorationSubArtifact(3082, currentX, currentY, 9, 16, 2, 7, 5, 1, 1, 2, "Artifact 2.12.3", levelSequence));
			if(subArtifactData[i] == 3083) Decoration.decorations.add(new DecorationSubArtifact(3083, currentX, currentY, 10, 16, 3, 7, 5, 1, 1, 2, "Artifact 2.12.4", levelSequence));
			
			//2.13
			if(subArtifactData[i] == 3084) Decoration.decorations.add(new DecorationSubArtifact(3084, currentX, currentY, 10, 10, 0, 7, 7, 1, 1, 2, "Artifact 2.13.1", levelSequence));
			if(subArtifactData[i] == 3085) Decoration.decorations.add(new DecorationSubArtifact(3085, currentX, currentY, 9, 10, 1, 7, 5, 1, 1, 2, "Artifact 2.13.2", levelSequence));
			if(subArtifactData[i] == 3086) Decoration.decorations.add(new DecorationSubArtifact(3086, currentX, currentY, 8, 10, 2, 6, 7, 1, 1, 2, "Artifact 2.13.3", levelSequence));
			//2.14
			if(subArtifactData[i] == 3087) Decoration.decorations.add(new DecorationSubArtifact(3087, currentX, currentY, 11, 12, 0, 9, 5, 1, 1, 2, "Artifact 2.14.1", levelSequence));
			if(subArtifactData[i] == 3088) Decoration.decorations.add(new DecorationSubArtifact(3088, currentX, currentY, 10, 12, 1, 4, 5, 1, 1, 2, "Artifact 2.14.2", levelSequence));
			if(subArtifactData[i] == 3089) Decoration.decorations.add(new DecorationSubArtifact(3089, currentX, currentY, 9, 12, 2, 5, 5, 1, 1, 2, "Artifact 2.14.3", levelSequence));
			
			//2.15
			if(subArtifactData[i] == 3090) Decoration.decorations.add(new DecorationSubArtifact(3090, currentX, currentY, 14, 4, 0, 6, 6, 1, 1, 2, "Artifact 2.15.1", levelSequence));
			if(subArtifactData[i] == 3091) Decoration.decorations.add(new DecorationSubArtifact(3091, currentX, currentY, 13, 4, 1, 12, 5, 1, 1, 2, "Artifact 2.15.2", levelSequence));
			if(subArtifactData[i] == 3092) Decoration.decorations.add(new DecorationSubArtifact(3092, currentX, currentY, 12, 4, 2, 6, 6, 1, 1, 2, "Artifact 2.15.3", levelSequence));
			
			//2.16
			if(subArtifactData[i] == 3093) Decoration.decorations.add(new DecorationSubArtifact(3093, currentX, currentY, 14, 13, 0, 3, 7, 1, 1, 2, "Artifact 2.16.1", levelSequence));
			if(subArtifactData[i] == 3094) Decoration.decorations.add(new DecorationSubArtifact(3094, currentX, currentY, 13, 13, 1, 5, 5, 1, 1, 2, "Artifact 2.16.2", levelSequence));
			if(subArtifactData[i] == 3095) Decoration.decorations.add(new DecorationSubArtifact(3095, currentX, currentY, 12, 13, 2, 4, 5, 1, 1, 2, "Artifact 2.16.3", levelSequence));
			if(subArtifactData[i] == 3096) Decoration.decorations.add(new DecorationSubArtifact(3096, currentX, currentY, 11, 13, 3, 8, 9, 1, 1, 2, "Artifact 2.16.4", levelSequence));
			if(subArtifactData[i] == 3097) Decoration.decorations.add(new DecorationSubArtifact(3097, currentX, currentY, 10, 13, 4, 5, 5, 1, 1, 2, "Artifact 2.16.5", levelSequence));

		}
		
		for(int i =  0; i < collectedArtifacts.length; i++){
			
			//DONE Read through code and see if the subArtifactId is for a specific level or something else
			
			//First number is the xTile, second is the yTile, third is the subArtifact Id, fourth is the width of the actual subArtifact in pixels, 
			//and fifth is the height of the actual subArtifact in pixels.
			
			//COPY this over from the list directly above
			//(the xTile, the yTile, artifact bar position, width(pixels), height(pixels))
			
			if(collectedArtifacts[i] == 3000) subArtifactsXYTiles[i] = new int[] {13, 8, 0, 10, 9}; 
			if(collectedArtifacts[i] == 3001) subArtifactsXYTiles[i] = new int[] {14, 8, 1, 8, 7}; 
			if(collectedArtifacts[i] == 3002) subArtifactsXYTiles[i] = new int[] {11, 9, 0, 5, 5}; 
			if(collectedArtifacts[i] == 3003) subArtifactsXYTiles[i] = new int[] {12, 9, 1, 4, 5}; 
			if(collectedArtifacts[i] == 3004) subArtifactsXYTiles[i] = new int[] {13, 9, 2, 5, 5}; 
			if(collectedArtifacts[i] == 3005) subArtifactsXYTiles[i] = new int[] {14, 9, 3, 4, 5}; 
			if(collectedArtifacts[i] == 3006) subArtifactsXYTiles[i] = new int[] {13, 10, 0, 10, 9};
			if(collectedArtifacts[i] == 3007) subArtifactsXYTiles[i] = new int[] {14, 10, 1, 4, 4}; 
			if(collectedArtifacts[i] == 3008) subArtifactsXYTiles[i] = new int[] {14, 11, 0, 6, 3};
			if(collectedArtifacts[i] == 3009) subArtifactsXYTiles[i] = new int[] {13, 11, 1, 6, 5};
			if(collectedArtifacts[i] == 3010) subArtifactsXYTiles[i] = new int[] {12, 11, 2, 6, 2};
			if(collectedArtifacts[i] == 3011) subArtifactsXYTiles[i] = new int[] {14, 13, 0, 5, 4};
			if(collectedArtifacts[i] == 3012) subArtifactsXYTiles[i] = new int[] {13, 13, 1, 5, 5};
			if(collectedArtifacts[i] == 3013) subArtifactsXYTiles[i] = new int[] {12, 13, 2, 4, 4};
			if(collectedArtifacts[i] == 3014) subArtifactsXYTiles[i] = new int[] {11, 13, 3, 4, 5};
			if(collectedArtifacts[i] == 3015) subArtifactsXYTiles[i] = new int[] {14, 12, 0, 5, 5};
			if(collectedArtifacts[i] == 3016) subArtifactsXYTiles[i] = new int[] {13, 12, 1, 5, 5};
			if(collectedArtifacts[i] == 3017) subArtifactsXYTiles[i] = new int[] {14, 14, 0, 9, 5};
			if(collectedArtifacts[i] == 3018) subArtifactsXYTiles[i] = new int[] {13, 14, 1, 4, 7};	
			if(collectedArtifacts[i] == 3019) subArtifactsXYTiles[i] = new int[] {12, 14, 2, 5, 7};
			if(collectedArtifacts[i] == 3020) subArtifactsXYTiles[i] = new int[] {14, 15, 0, 5, 5};
			if(collectedArtifacts[i] == 3021) subArtifactsXYTiles[i] = new int[] {13, 15, 1, 5, 5};
			if(collectedArtifacts[i] == 3022) subArtifactsXYTiles[i] = new int[] {12, 15, 2, 5, 10};
			if(collectedArtifacts[i] == 3023) subArtifactsXYTiles[i] = new int[] {14, 7, 0, 8, 5};
			if(collectedArtifacts[i] == 3024) subArtifactsXYTiles[i] = new int[] {13, 7, 1, 4, 4};
			if(collectedArtifacts[i] == 3025) subArtifactsXYTiles[i] = new int[] {12, 7, 2, 8, 7};
			if(collectedArtifacts[i] == 3026) subArtifactsXYTiles[i] = new int[] {14, 4, 0, 6, 4};
			if(collectedArtifacts[i] == 3027) subArtifactsXYTiles[i] = new int[] {13, 4, 1, 6, 6};
			if(collectedArtifacts[i] == 3028) subArtifactsXYTiles[i] = new int[] {14, 6, 0, 7, 6};
			if(collectedArtifacts[i] == 3029) subArtifactsXYTiles[i] = new int[] {13, 6, 1, 5, 6};
			if(collectedArtifacts[i] == 3030) subArtifactsXYTiles[i] = new int[] {12, 6, 2, 7, 6};
			if(collectedArtifacts[i] == 3031) subArtifactsXYTiles[i] = new int[] {11, 6, 3, 4, 6};
			if(collectedArtifacts[i] == 3032) subArtifactsXYTiles[i] = new int[] {14, 5, 0, 3, 7};
			if(collectedArtifacts[i] == 3033) subArtifactsXYTiles[i] = new int[] {13, 5, 1, 9, 4};
			if(collectedArtifacts[i] == 3034) subArtifactsXYTiles[i] = new int[] {12, 5, 2, 3, 6};
			if(collectedArtifacts[i] == 3035) subArtifactsXYTiles[i] = new int[] {14, 16, 0, 2, 4};
			if(collectedArtifacts[i] == 3036) subArtifactsXYTiles[i] = new int[] {13, 16, 1, 5, 4};
			if(collectedArtifacts[i] == 3037) subArtifactsXYTiles[i] = new int[] {12, 16, 2, 5, 3};
			if(collectedArtifacts[i] == 3038) subArtifactsXYTiles[i] = new int[] {14, 17, 0, 7, 7};
			if(collectedArtifacts[i] == 3039) subArtifactsXYTiles[i] = new int[] {13, 17, 1, 5, 5};
			if(collectedArtifacts[i] == 3040) subArtifactsXYTiles[i] = new int[] {12, 17, 2, 3, 3};
			if(collectedArtifacts[i] == 3041) subArtifactsXYTiles[i] = new int[] {10, 17, 0, 6, 8};
			if(collectedArtifacts[i] == 3042) subArtifactsXYTiles[i] = new int[] {9, 17, 1, 5, 8};
			if(collectedArtifacts[i] == 3043) subArtifactsXYTiles[i] = new int[] {6, 14, 0, 7, 5};
			if(collectedArtifacts[i] == 3044) subArtifactsXYTiles[i] = new int[] {5, 14, 1, 8, 8};
			if(collectedArtifacts[i] == 3045) subArtifactsXYTiles[i] = new int[] {4, 14, 2, 7, 8};
			if(collectedArtifacts[i] == 3046) subArtifactsXYTiles[i] = new int[] {3, 14, 3, 7, 7};
			if(collectedArtifacts[i] == 3047) subArtifactsXYTiles[i] = new int[] {2, 14, 4, 8, 7};

			//2.1
			if(collectedArtifacts[i] == 3048) subArtifactsXYTiles[i] = new int[] {14, 5, 0, 5, 5};
			if(collectedArtifacts[i] == 3049) subArtifactsXYTiles[i] = new int[] {13, 5, 1, 5, 4};
			if(collectedArtifacts[i] == 3050) subArtifactsXYTiles[i] = new int[] {12, 5, 2, 5, 4};
			if(collectedArtifacts[i] == 3051) subArtifactsXYTiles[i] = new int[] {11, 5, 3, 5, 5};
			
			//2.2
			if(collectedArtifacts[i] == 3052) subArtifactsXYTiles[i] = new int[] {14, 8, 0, 4, 4};
			if(collectedArtifacts[i] == 3053) subArtifactsXYTiles[i] = new int[] {13, 8, 1, 3, 3};
			if(collectedArtifacts[i] == 3054) subArtifactsXYTiles[i] = new int[] {12, 8, 2, 5, 5};
			
			//2.3
			if(collectedArtifacts[i] == 3055) subArtifactsXYTiles[i] = new int[] {14, 10, 0, 3, 6};
			if(collectedArtifacts[i] == 3056) subArtifactsXYTiles[i] = new int[] {13, 10, 1, 4, 6};
			if(collectedArtifacts[i] == 3057) subArtifactsXYTiles[i] = new int[] {12, 10, 2, 5, 5};
		
			//2.4
			if(collectedArtifacts[i] == 3058) subArtifactsXYTiles[i] = new int[] {9, 11, 0, 4, 8};
			if(collectedArtifacts[i] == 3059) subArtifactsXYTiles[i] = new int[] {8, 11, 1, 4, 8};
			if(collectedArtifacts[i] == 3060) subArtifactsXYTiles[i] = new int[] {7, 11, 2, 5, 8};
			if(collectedArtifacts[i] == 3061) subArtifactsXYTiles[i] = new int[] {6, 11, 3, 5, 8};
			
			//2.5
			if(collectedArtifacts[i] == 3062) subArtifactsXYTiles[i] = new int[] {14, 3, 0, 6, 6};
			if(collectedArtifacts[i] == 3063) subArtifactsXYTiles[i] = new int[] {13, 3, 1, 14, 4};
			if(collectedArtifacts[i] == 3064) subArtifactsXYTiles[i] = new int[] {12, 3, 2, 8, 6};
			
			//2.6
			if(collectedArtifacts[i] == 3065) subArtifactsXYTiles[i] = new int[] {14, 6, 0, 9, 6};
			if(collectedArtifacts[i] == 3066) subArtifactsXYTiles[i] = new int[] {13, 6, 1, 7, 6};
			
			//2.7
			if(collectedArtifacts[i] == 3067) subArtifactsXYTiles[i] = new int[] {14, 7, 0, 3, 6};
			if(collectedArtifacts[i] == 3068) subArtifactsXYTiles[i] = new int[] {13, 7, 1, 3, 6};
			
			//2.8
			if(collectedArtifacts[i] == 3069) subArtifactsXYTiles[i] = new int[] {14, 9, 0, 2, 5};
			if(collectedArtifacts[i] == 3070) subArtifactsXYTiles[i] = new int[] {13, 9, 1, 3, 5};
			
			//2.9
			if(collectedArtifacts[i] == 3071) subArtifactsXYTiles[i] = new int[] {14, 11, 0, 7, 4};
			if(collectedArtifacts[i] == 3072) subArtifactsXYTiles[i] = new int[] {13, 11, 1, 7, 4};
			if(collectedArtifacts[i] == 3073) subArtifactsXYTiles[i] = new int[] {12, 11, 2, 7, 3};
			if(collectedArtifacts[i] == 3074) subArtifactsXYTiles[i] = new int[] {11, 11, 3, 7, 3};
			
			//2.10
			if(collectedArtifacts[i] == 3075) subArtifactsXYTiles[i] = new int[] {14, 12, 0, 7, 7};
			if(collectedArtifacts[i] == 3076) subArtifactsXYTiles[i] = new int[] {13, 12, 1, 3, 4};
	
			//2.11
			if(collectedArtifacts[i] == 3077) subArtifactsXYTiles[i] = new int[] {8, 13, 0, 11, 5};
			if(collectedArtifacts[i] == 3078) subArtifactsXYTiles[i] = new int[] {7, 13, 1, 5, 5};
			if(collectedArtifacts[i] == 3079) subArtifactsXYTiles[i] = new int[] {6, 13, 2, 6, 5};
			
			//2.12
			if(collectedArtifacts[i] == 3080) subArtifactsXYTiles[i] = new int[] {10, 15, 0, 7, 4};
			if(collectedArtifacts[i] == 3081) subArtifactsXYTiles[i] = new int[] {9, 15, 1, 9, 5};
			if(collectedArtifacts[i] == 3082) subArtifactsXYTiles[i] = new int[] {9, 16, 2, 7, 5};
			if(collectedArtifacts[i] == 3083) subArtifactsXYTiles[i] = new int[] {10, 16, 3, 7, 5};
			
			//2.13
			if(collectedArtifacts[i] == 3084) subArtifactsXYTiles[i] = new int[] {10, 10, 0, 7, 7};
			if(collectedArtifacts[i] == 3085) subArtifactsXYTiles[i] = new int[] {9, 10, 1, 7, 5};
			if(collectedArtifacts[i] == 3086) subArtifactsXYTiles[i] = new int[] {8, 10, 2, 6, 7};
			//2.14
			if(collectedArtifacts[i] == 3087) subArtifactsXYTiles[i] = new int[] {11, 12, 0, 9, 5};
			if(collectedArtifacts[i] == 3088) subArtifactsXYTiles[i] = new int[] {10, 12, 1, 4, 5};
			if(collectedArtifacts[i] == 3089) subArtifactsXYTiles[i] = new int[] {9, 12, 2, 5, 5};
			
			//2.15
			if(collectedArtifacts[i] == 3090) subArtifactsXYTiles[i] = new int[] {14, 4, 0, 6, 6};
			if(collectedArtifacts[i] == 3091) subArtifactsXYTiles[i] = new int[] {13, 4, 1, 12, 5};
			if(collectedArtifacts[i] == 3092) subArtifactsXYTiles[i] = new int[] {12, 4, 2, 6, 6};
			
			//2.16
			if(collectedArtifacts[i] == 3093) subArtifactsXYTiles[i] = new int[] {14, 13, 0, 3, 7};
			if(collectedArtifacts[i] == 3094) subArtifactsXYTiles[i] = new int[] {13, 13, 1, 5, 5};
			if(collectedArtifacts[i] == 3095) subArtifactsXYTiles[i] = new int[] {12, 13, 2, 4, 5};
			if(collectedArtifacts[i] == 3096) subArtifactsXYTiles[i] = new int[] {11, 13, 3, 8, 9};
			if(collectedArtifacts[i] == 3097) subArtifactsXYTiles[i] = new int[] {10, 13, 4, 5, 5};
			
			}
		
				
	}
	
	public int[] getCompletedSubArtifact(){
		if(worldNumber == 1) {
			if(levelNumber == 1){ return new int[] {15, 8, 10, 10}; }
			if(levelNumber == 2){ return new int[] {15, 9, 10, 10}; }
			if(levelNumber == 3){ return new int[] {15, 10, 10, 12}; }
			if(levelNumber == 4){ return new int[] {15, 11, 12, 5}; }
			if(levelNumber == 5){ return new int[] {15, 13, 9, 9}; }
			if(levelNumber == 6){ return new int[] {15, 12, 10, 9}; }
			if(levelNumber == 7){ return new int[] {15, 14, 9, 12}; }
			if(levelNumber == 8){ return new int[] {15, 15, 10, 10}; }
			if(levelNumber == 9){ return new int[] {15, 7, 8, 12}; }
			if(levelNumber == 10){ return new int[] {15, 4, 9, 8}; }
			if(levelNumber == 11){ return new int[] {15, 6, 12, 12}; }
			if(levelNumber == 12){ return new int[] {15, 5, 13, 10}; }
			if(levelNumber == 13){ return new int[] {15, 16, 7, 7}; }
			if(levelNumber == 14){ return new int[] {15, 17, 11, 11}; }
			if(levelNumber == 15){ return new int[] {11, 17, 11, 8}; }
			if(levelNumber == 16){ return new int[] {7, 14, 15, 15}; }
		}else if(worldNumber == 2) {
			if(levelNumber == 1){ return new int[] {15, 5, 10, 9}; }
			if(levelNumber == 2){ return new int[] {15, 8, 9, 10}; }
			if(levelNumber == 3){ return new int[] {15, 10, 13, 8}; }
			if(levelNumber == 4){ return new int[] {9, 11, 10, 16}; }
			if(levelNumber == 5){ return new int[] {15, 3, 14, 10}; }
			if(levelNumber == 6){ return new int[] {15, 6, 9, 11}; }
			if(levelNumber == 7){ return new int[] {15, 7, 7, 8}; }
			if(levelNumber == 8){ return new int[] {15, 9, 5, 5}; }
			if(levelNumber == 9){ return new int[] {15, 11, 14, 7}; }
			if(levelNumber == 10){ return new int[] {15, 12, 9, 10}; }
			if(levelNumber == 11){ return new int[] {9, 13, 11, 10}; }
			if(levelNumber == 12){ return new int[] {11, 15, 14, 14}; }
			if(levelNumber == 13){ return new int[] {11, 10, 13, 12}; }
			if(levelNumber == 14){ return new int[] {12, 12, 9, 10}; }
			if(levelNumber == 15){ return new int[] {15, 4, 12, 11}; }
			if(levelNumber == 16){ return new int[] {15, 13, 14, 14}; }
		}
		return null;
	}
	
	public Player setPlayer(InputManager input){
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).id == 2000){
				Player player =  new Player("Player", input, this, Decoration.decorations.get(i).x + (17 * 4), Decoration.decorations.get(i).y + (15 * 4));
				if(levelNumber > 8 && levelNumber <= 12){
					player.setDarkOffset(6);
				}
				return player;
			}
		}
		return null;
	}
	
	public void tick(Player player){
		for(int i = 0; i < Decoration.decorations.size(); i++){
			Decoration.decorations.get(i).tick(player);
		}
		
		for(int i = 0; i < Entity.entities.size(); i++){
			Entity.entities.get(i).tick(this);
			if(deletedEntity){
				deletedEntity = false;
				i--;
			}
		}
	}
	
	//Main render area for the current level to be rendered
	
	public void render(float interpolation, float testInterpolation){

		
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).render == 2){
				Decoration.decorations.get(i).render(screen, interpolation);
			}
		}
		
		//Render Blocks
		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){
				Block.blocks[x + y * levelWidth].render(screen, this, x, y);
			}
		}
		
		
		//DONE Add Ability to render decorations before the blocks
		
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).render == 1){
				Decoration.decorations.get(i).render(screen, interpolation);
			}
		}
		
		for(int i = 0; i < Entity.entities.size(); i++){
			Entity.entities.get(i).render(screen, interpolation, testInterpolation);
		}
		artifactBar.render(screen, subArtifactsXYTiles);
		
		//lightScreen.renderLight(screen);

		
	}
	
	public void renderBeforePlayer(Screen screen, float interpolation){
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).render == 3){
				Decoration.decorations.get(i).render(screen, interpolation);
			}
		}
	}
	
	//public void loadLight(){
		//lightScreen.loadLight(true);
	//}
	
	public void updateBar(int xTile, int yTile, int artifactPosition, int widthPixels, int heightPixels){
		int[][] preSubArtifactsXYTiles = new int[subArtifactsXYTiles.length + 1][];
		for(int i = 0; i < subArtifactsXYTiles.length; i++){
			preSubArtifactsXYTiles[i] = subArtifactsXYTiles[i];
		}
		
		preSubArtifactsXYTiles[subArtifactsXYTiles.length] = new int[] {xTile, yTile, artifactPosition, widthPixels, heightPixels};
		subArtifactsXYTiles = preSubArtifactsXYTiles;
	}
	
	
	public Block getBlock(int x, int y){
		if(y < levelHeight && x < levelWidth && x >= 0 && y >= 0){
			
			int i = x + y * levelWidth;
			if(Block.blocks[i].x == x && Block.blocks[i].y == y){
				return Block.blocks[i];
			}


		}else{
			return new AirBlock(0, x, y);
		}
		
		return null;
	}
	
	public Decoration getDecoration(int decorationId){
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).id == decorationId){
				return Decoration.decorations.get(i);
			}
		}
		
		return null;
	}
	
	public boolean touchingArtifact(int playerX, int playerXR, int playerY, int playerYB){
		
		//THIS TRIGGERS EVEN AFTER THE SUB ARTIFACT IS GONE
		//!!!Does not cause any problems but just good to know!!!
		
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).id >= 2750){
				

				int decorationX = Decoration.decorations.get(i).x;
				int decorationY = Decoration.decorations.get(i).y;
				int decorationXR = Decoration.decorations.get(i).x + (Decoration.decorations.get(i).spriteWidth * 64 - 1);
				int decorationYB = Decoration.decorations.get(i).y + (Decoration.decorations.get(i).spriteHeight * 64 - 1);
				
				if(playerXR >= decorationX && playerY <= decorationYB && playerX <= decorationXR && playerYB >= decorationY){
					
					Decoration.decorations.get(i).pickUp = true;
					if(Decoration.decorations.get(i).id < 3000){
						endTime = System.currentTimeMillis();
					}
				}
			}
		}
		return false;
	}
	
	public DecorationArtifact currentArtifact(){
		for(int i = 0; i < Decoration.decorations.size(); i++){
			if(Decoration.decorations.get(i).id >= 2750 && Decoration.decorations.get(i).id < 3000){
				return (DecorationArtifact) Decoration.decorations.get(i);
			}
		}
		
		return null;
	}
	
	public Screen getScreen(){
		return screen;
	}
	
	public void saveArtifacts(){
		
	}

}
