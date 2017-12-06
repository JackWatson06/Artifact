package com.murdermaninc.decorations.mainShip;


import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.MainShipWorldManager;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;

public class Monitor extends DecorationShip{

	
	private Font font = new Font();
	
	private Animation animation = new Animation();
	
	public String currentLevel = new String("Level1-1");
	public int currentLevelNumber = 1;
	public int currentWorldNumber = 0;
	private String displayTime = new String("0.0s");
	private String levelTime = new String("0.0s");
	private String displayShards = new String("0");
	private String levelShards = new String("0");
	private String displayPercentage = new String("0%");
	
	private float displayTimeNumber = 0.0F;
	private float levelTimeNumber = 0.0F;
	private int displayShardsNumber = 0;
	private int levelShardsNumber = 0;
	private int displayPercentageNumber = 0;
	
	private Lever lever;
	private LevelSequencing levelS;
	
	private boolean displayLock = false;
	
	public Monitor(MainShipWorldManager manager, Lever lever, LevelSequencing levelS, int currentWorldNumber, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight) {
		
		super(manager, x, y, xTile, yTile, spriteWidth, spriteHeight);
		this.lever = lever;
		this.levelS = levelS;
		this.currentWorldNumber = currentWorldNumber;
		
		currentLevel = "Level" + currentWorldNumber + "-" + lever.leverNumber;
		if(!levelS.levelAvailability[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]){
			displayLock = true;
		}
		
		displayTime =  String.valueOf(levelS.levelTime[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) + "s";
		displayTimeNumber = levelS.levelTime[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
		
		levelTime = String.valueOf(levelS.getTime(currentLevelNumber, currentWorldNumber)) + "s";
		levelTimeNumber = levelS.getTime(currentLevelNumber, currentWorldNumber);
		
		displayShards = String.valueOf(levelS.artifactsCollected[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]);
		displayShardsNumber = levelS.artifactsCollected[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
		
		levelShards = String.valueOf(levelS.getLevelArtifacts(currentLevelNumber, currentWorldNumber));
		levelShardsNumber = levelS.getLevelArtifacts(currentLevelNumber, currentWorldNumber);
		
		displayPercentage = String.valueOf(levelS.levelPercentage[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) + "%";
		displayPercentageNumber = levelS.levelPercentage[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
	}
	
	@Override
	public void tick(){
		
		//Replace the second half with a check for if the level is unlocked or not
		if(currentLevelNumber != lever.leverNumber && levelS.levelAvailability[((currentWorldNumber - 1) * 16) + lever.leverNumber - 1]){
			currentLevelNumber = lever.leverNumber;
			
			currentLevel = "Level" + currentWorldNumber + "-" + lever.leverNumber;
			displayLock = false;
			
			displayTime =  String.valueOf(levelS.levelTime[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) + "s";
			displayTimeNumber = levelS.levelTime[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
			
			levelTime = String.valueOf(levelS.getTime(currentLevelNumber, currentWorldNumber)) + "s";
			levelTimeNumber = levelS.getTime(currentLevelNumber, currentWorldNumber);
			
			displayShards = String.valueOf(levelS.artifactsCollected[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]);
			displayShardsNumber = levelS.artifactsCollected[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
			
			levelShards = String.valueOf(levelS.getLevelArtifacts(currentLevelNumber, currentWorldNumber));
			levelShardsNumber = levelS.getLevelArtifacts(currentLevelNumber, currentWorldNumber);
			
			displayPercentage = String.valueOf(levelS.levelPercentage[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) + "%";
			displayPercentageNumber = levelS.levelPercentage[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
			
			
			
			
		}else if(currentLevelNumber != lever.leverNumber){
			currentLevelNumber = lever.leverNumber;
			
			currentLevel = "Level" + currentWorldNumber + "-" + lever.leverNumber;
			displayLock = true;
			
			displayTime =  String.valueOf(levelS.levelTime[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) + "s";
			displayTimeNumber = levelS.levelTime[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
			
			levelTime = String.valueOf(levelS.getTime(currentLevelNumber, currentWorldNumber)) + "s";
			levelTimeNumber = levelS.getTime(currentLevelNumber, currentWorldNumber);
			
			displayShards = String.valueOf(levelS.artifactsCollected[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]);
			displayShardsNumber = levelS.artifactsCollected[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
			
			levelShards = String.valueOf(levelS.getLevelArtifacts(currentLevelNumber, currentWorldNumber));
			levelShardsNumber = levelS.getLevelArtifacts(currentLevelNumber, currentWorldNumber);
			
			displayPercentage = String.valueOf(levelS.levelPercentage[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) + "%";
			displayPercentageNumber = levelS.levelPercentage[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1];
		}
		
		
		
	}
	
	
	public boolean getLevelAvailability() {
		//System.out.println(currentLevelNumber);
		if(levelS.levelAvailability[((currentWorldNumber - 1) * 16) + currentLevelNumber - 1]) {
			return true;
		}
		return false;
	}
	
	private int fontScale = 2;
	private int largeFontScale = 3;

	
	@Override
	public void render(Screen screen, float interpolation){
										//gap between
		int sizeOfTotalFont = (12 * 3) + (24 * 3) + (8 * 3) + (14 * 2) + (14 * 2) + (12 * 2) + (8 * 4) + (7 * 4) + (9 * 4);
		

		//The position is technically all incorrect as it did not take into account the top of the letters
		
		//if(animationData == null) animationData = animation.loadAnimationData(screen, "shipIcons", 4, 3, xTile, yTile, spriteWidth, spriteHeight);

		//animation.animateRandom(screen, animationData, false, 2.0F, spriteWidth, spriteHeight, 0.0009F, x, y, 3);
		
						//Offset from top	//Width of title	//height between data
		int yOffset = y + (360 / 2) - (sizeOfTotalFont / 2) + (12 * 3) + 24;
		
		if(!animation.random){
			screen.render(x, y, xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		}
		
		String levelDisplay = currentLevel.substring(0, 5) + " " + currentLevel.substring(7);																//This is the leftover at the top of font
		font.drawText(screen, levelDisplay, x + (308 / 2) - (font.getTextLength(levelDisplay, largeFontScale) / 2), y + (360 / 2) - (sizeOfTotalFont / 2) - (4 * 3), largeFontScale);
		
		//System.out.println(sizeOfTotalFont / 2);
		
		if(displayLock){
			
			screen.render( x + (308 / 2) + (font.getTextLength(levelDisplay, largeFontScale) / 2) + 6, y + (360 / 2) - (sizeOfTotalFont / 2) + ((12 * 3) / 2) - (33 / 2), 9, 0, 1, 1, 3, "shipIcons");
		}
		
		font.drawText(screen, displayTime, x + (308 / 2) - (font.getTextLength(displayTime, fontScale)) - (font.getTextLength("/", fontScale) / 2) - (int) (font.spacing * fontScale), yOffset - (2 * 2), fontScale);
		font.drawText(screen, "/", x + (308 / 2) - (font.getTextLength("/", fontScale) / 2), yOffset - (2 * 2), fontScale);
		font.drawText(screen, levelTime, x + (308 / 2) + (font.getTextLength("/", fontScale) / 2) + (int) (font.spacing * fontScale), yOffset - (2 * 2), fontScale);
		if(displayTimeNumber < levelTimeNumber && displayTimeNumber != 0.0F){
			screen.render(x + (308 / 2) - (32 / 2), yOffset + (14 * 2) + 8, 5, 0, 1, 1, 4, "shipIcons");
		}else{
			screen.render(x + (308 / 2) - (32 / 2), yOffset + (14 * 2) + 8, 5, 1, 1, 1, 4, "shipIcons");

		}
			            //letter height //Icon Offset    //Icons height 	//height between data
		yOffset = yOffset + (14 * 2)    +    8    +            32 +         24;
		
		font.drawText(screen, displayShards, x + (308 / 2) - (font.getTextLength(displayShards, fontScale)) - (font.getTextLength("/", fontScale) / 2) - (int) (font.spacing * fontScale), yOffset - (2 * 2), fontScale);
		font.drawText(screen, "/", x + (308 / 2) - (font.getTextLength("/", fontScale) / 2),yOffset - (2 * 2), fontScale);
		font.drawText(screen, levelShards, x + (308 / 2) + (font.getTextLength("/", fontScale) / 2) + (int) (font.spacing * fontScale), yOffset - (2 * 2), fontScale);
		if(displayShardsNumber == levelShardsNumber){
			screen.render(x + (308 / 2) - (36 / 2), yOffset + (14 * 2)    +    8, 6, 0, 1, 1, 4, "shipIcons");
		}else{
			screen.render(x + (308 / 2) - (36 / 2), yOffset + (14 * 2)    +    8, 6, 1, 1, 1, 4, "shipIcons");
		}
		
		yOffset = yOffset + (14 * 2)    +    8    +      28     +    24;
		
		font.drawText(screen, displayPercentage, x + (308 / 2) - (font.getTextLength(displayPercentage, 2) / 2), yOffset - (4 * 2), 2);
		
		if(displayPercentageNumber == 100){
			screen.render(x + (308 / 2) - (40 / 2), yOffset + (12 * 2)    +    8, 7, 0, 1, 1, 4, "shipIcons");
		}else{
			screen.render(x + (308 / 2) - (40 / 2), yOffset + (12 * 2)    +    8, 7, 1, 1, 1, 4, "shipIcons");
		}
	}

}

/*
 * 
 * //if(animationData == null) animationData = animation.loadAnimationData(screen, "shipIcons", 4, 3, xTile, yTile, spriteWidth, spriteHeight);

		//animation.animateRandom(screen, animationData, false, 2.0F, spriteWidth, spriteHeight, 0.0009F, x, y, 3);
		
						//Offset from top	//Width of title	//height between data
		int yOffset = y +      12 +             (16 * 3) +           20;
		
		if(!animation.random){
			screen.render(x, y, xTile, yTile, spriteWidth, spriteHeight, 4, "shipIcons");
		}
		
		String levelDisplay = currentLevel.substring(0, 5) + " " + currentLevel.substring(7);
		font.drawText(screen, levelDisplay, x + (308 / 2) - (font.getTextLength(levelDisplay, largeFontScale) / 2), y + 12, largeFontScale);
		
		if(displayLock){
			
			screen.render( x + (308 / 2) + (font.getTextLength(levelDisplay, largeFontScale) / 2) + 6, y + 12 + 6 + ((16 * 3) / 2) - (33 / 2), 9, 0, 1, 1, 3, "shipIcons");
		}
		
		font.drawText(screen, displayTime, x + (308 / 2) - (font.getTextLength(displayTime, fontScale)) - (font.getTextLength("/", fontScale) / 2) - (int) (font.spacing * fontScale), yOffset, fontScale);
		font.drawText(screen, "/", x + (308 / 2) - (font.getTextLength("/", fontScale) / 2), yOffset, fontScale);
		font.drawText(screen, levelTime, x + (308 / 2) + (font.getTextLength("/", fontScale) / 2) + (int) (font.spacing * fontScale), yOffset, fontScale);
		if(displayTimeNumber < levelTimeNumber && displayTimeNumber != 0.0F){
			screen.render(x + (308 / 2) - (32 / 2), yOffset + (16 * 2) + 8, 5, 0, 1, 1, 4, "shipIcons");
		}else{
			screen.render(x + (308 / 2) - (32 / 2), yOffset + (16 * 2) + 8, 5, 1, 1, 1, 4, "shipIcons");

		}
			            //letter height //Icon Offset    //Icons height 	//height between data
		yOffset = yOffset + (16 * 2)    +    8    +            32 +         20;
		
		font.drawText(screen, displayShards, x + (308 / 2) - (font.getTextLength(displayShards, fontScale)) - (font.getTextLength("/", fontScale) / 2) - (int) (font.spacing * fontScale), yOffset, fontScale);
		font.drawText(screen, "/", x + (308 / 2) - (font.getTextLength("/", fontScale) / 2),yOffset, fontScale);
		font.drawText(screen, levelShards, x + (308 / 2) + (font.getTextLength("/", fontScale) / 2) + (int) (font.spacing * fontScale), yOffset, fontScale);
		if(displayShardsNumber == levelShardsNumber){
			screen.render(x + (308 / 2) - (36 / 2), yOffset + (16 * 2)    +    8, 6, 0, 1, 1, 4, "shipIcons");
		}else{
			screen.render(x + (308 / 2) - (36 / 2), yOffset + (16 * 2)    +    8, 6, 1, 1, 1, 4, "shipIcons");
		}
		
		yOffset = yOffset + (16 * 2)    +    8    +      28     +    20;
		
		font.drawText(screen, displayPercentage, x + (308 / 2) - (font.getTextLength(displayPercentage, 2) / 2), yOffset, 2);
		
		if(displayPercentageNumber == 100){
			screen.render(x + (308 / 2) - (40 / 2), yOffset + (16 * 2)    +    8, 7, 0, 1, 1, 4, "shipIcons");
		}else{
			screen.render(x + (308 / 2) - (40 / 2), yOffset + (16 * 2)    +    8, 7, 1, 1, 1, 4, "shipIcons");
		}
*/