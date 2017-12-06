package com.murdermaninc.collectionBackground;

import com.murdermaninc.decorations.artifacts.DecorationArtifact;
import com.murdermaninc.graphics.Background;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;


public class CollectionBackground {

	private Background semiTransparentBackground;
	
	private Screen collectionScreen;
	
	public Font font = new Font();
	
	private Time time;
	private CompletionBar compBar;
	private Title title;
	private MainArtifactCanister mainArt;
	private SubArtifactCanister subArt;
	private SubArtifactShards[] shardArt;
	private Continue continueButton;
	
	//TODO this class does not need a render or tick method as NOTHING is moving other than the continue button so maby actually keep it specifically for the continue button but everything else does not need
	//to be rendered in the render method it only needs to be rendered once.
	
	//TODO Maybe add titles for the artifact shard canisters.
	
	//Don't have to render all the background containers if it doesn't need updating and keep container animations synchronized.
	
	public CollectionBackground(int width, int height, float finishTime, float thresholdTime, int totalLevelArtifacts, int[][] subArtifacts, int[] completedSubArtifact, DecorationArtifact artifact, String currentLevel, LevelSequencing levelS){
		
		//HELLO if your reading this, how are you!!!
		
		semiTransparentBackground = new Background("/CollectionBackground.png", true);
		semiTransparentBackground.scaleImage(4, width, height);
		
		collectionScreen = new Screen(width, height, 0, 0, 1);
		
		for(int i = 0; i < semiTransparentBackground.pixels.length; i++){
			collectionScreen.pixels[i] = semiTransparentBackground.pixels[i];
		}
		
		collectionScreen.loadSpriteSheet("/PoopyArtifactCell.png", "ArtifactCell");
		collectionScreen.loadSpriteSheet("/TitleBar.png", "TitleSprite");
		collectionScreen.loadSpriteSheet("/Time.png", "TimeSprite");
		collectionScreen.loadSpriteSheet("/CompletionBar.png", "CompletionBarSprite");
		collectionScreen.loadSpriteSheet("/ShardCanister.png", "ShardCanisters");
		collectionScreen.loadSpriteSheet("/SubArtifactCanister.png", "SubArtifactCanister");
		
		int currentLevelNumber = levelS.getCurrentLevel().levelNumber;
		
		if(currentLevelNumber < 9){
			collectionScreen.loadSpriteSheet("/icons.png", "Icons");
		}else if(currentLevelNumber >= 9 && currentLevelNumber <= 12){
			collectionScreen.loadSpriteSheet("/iconsdarker.png", "Icons");
		}else if(currentLevelNumber > 12 && currentLevelNumber <= 16){
			collectionScreen.loadSpriteSheet("/icons.png", "Icons");
		}
		
		
		collectionScreen.loadSpriteSheet("/font.png", "font");
		collectionScreen.loadSpriteSheet("/ShardCanisterTitles.png", "shardTitles");
		collectionScreen.loadSpriteSheet("/ContinueButton.png", "continue");
		collectionScreen.loadSpriteSheet("/ShardCanisterLabel.png", "shardCanisterLabel");
		
		String currentLevelString = currentLevel.substring(0, 5) + ":" + currentLevel.substring(5, currentLevel.length());
		
		title = new Title(currentLevelString, width);
		
		time = new Time(levelS, finishTime, thresholdTime);
		
		compBar = new CompletionBar(levelS, true, time.timeBonus, totalLevelArtifacts, subArtifacts.length);
		
		mainArt = new MainArtifactCanister(artifact.xTile, artifact.yTile, artifact.widthPixels, artifact.heightPixels);
		
		
		int spacing = 25;
		int currentX = (width / 2) - (((spacing * totalLevelArtifacts) + (totalLevelArtifacts * 236) + (288)) / 2);
		int currentY = 640;
		
		shardArt = new SubArtifactShards[totalLevelArtifacts];
		
		int currentI = 0;

		for(int i = 0; i < totalLevelArtifacts; i++){
			
			boolean addNull = true;
			
			//Scans the current found artifacts and if the current canister number is equal to the id of the found subArtifact then a canister is added with a shard.
			for(int j = 0; j < subArtifacts.length; j++){
				if(subArtifacts[j][2] == i){
					shardArt[i] = new SubArtifactShards(currentX + ((i * spacing) + (i * 232)), currentY + (384 / 2) - (332 / 2), subArtifacts[j], i);
					addNull = false;
				}
			}
			
			//Adds empty canister if the sub artifact was not listing in the found artifacts array or subArtifacts.
			if(addNull){
				shardArt[i] = new SubArtifactShards(currentX + ((i * spacing) + (i * 232)), currentY + (384 / 2) - (332 / 2), null, i);
			}
			
			currentI = i;
		}
		currentI++;
		if(subArtifacts.length == totalLevelArtifacts){
			subArt = new SubArtifactCanister(currentX + ((currentI * spacing) + (currentI * 232)), currentY, true, completedSubArtifact);
		}else{
			subArt = new SubArtifactCanister(currentX + ((currentI * spacing) + (currentI * 232)), currentY, false, completedSubArtifact);
		}
		
		continueButton = new Continue(width, height);
		
	}
	
	public void tick(){
		mainArt.tick();
		for(int i = 0; i < shardArt.length; i++){
			shardArt[i].tick();
		}
		subArt.tick();
	}
	
	//THIS SCREEN IS THE COLLECTION SCREEN
	
	public void render(Screen screen, float interpolation){
		
		title.render(screen);
		mainArt.render(screen);
		time.render(screen);
		compBar.render(screen);
		for(int i = 0; i < shardArt.length; i++){
			shardArt[i].render(screen);
		}
		subArt.render(screen);
		continueButton.render(screen, interpolation);
		
	}
	
	public Screen getScreen(){
		return collectionScreen;
	}
}

