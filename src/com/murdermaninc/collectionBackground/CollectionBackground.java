package com.murdermaninc.collectionBackground;

import com.murdermaninc.decorations.artifacts.DecorationArtifact;
import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;


public class CollectionBackground {

	private Screen collectionScreen;
	
	public Font font = new Font();
	
	private Time time;
	private CompletionBar compBar;
	private Title title;
	private MainArtifactCanister mainArt;
	private SubArtifactCanister subArt;
	private SubArtifactShards[] shardArt;
	private Continue continueButton;
	
	private int tranpsarentColor;
	
	//TODO this class does not need a render or tick method as NOTHING is moving other than the continue button so maby actually keep it specifically for the continue button but everything else does not need
	//to be rendered in the render method it only needs to be rendered once.
	
	//TODO Maybe add titles for the artifact shard canisters.
	
	//Don't have to render all the background containers if it doesn't need updating and keep container animations synchronized.
	
	public CollectionBackground(int width, int height, float finishTime, float thresholdTime, int totalLevelArtifacts, int[][] subArtifacts, int[] completedSubArtifact, DecorationArtifact artifact, String currentLevel, LevelSequencing levelS){
		
		//HELLO if your reading this, how are you!!!
		
		//This screen is specifically created only for loading in the data of the completion screen
		//as the screen we render it to is the screenTiles and it would be inefficient to always have these spriteSheets
		//loaded in that screen object
		
		//Also all data loading occurs in the constructer of the objects.
		
		int alphaValue = 129;
		
		//This color is stored in twos complement as all other color values are
		this.tranpsarentColor = (~(alphaValue << 24) + 1);
		
		collectionScreen = new Screen(width, height, 0, 0);
		
		
		
		collectionScreen.loadSpriteSheet("/PoopyArtifactCell.png", "ArtifactCell");
		collectionScreen.loadSpriteSheet("/TitleBar.png", "TitleSprite");
		collectionScreen.loadSpriteSheet("/Time.png", "TimeSprite");
		collectionScreen.loadSpriteSheet("/CompletionBar.png", "CompletionBarSprite");
		collectionScreen.loadSpriteSheet("/ShardCanister.png", "ShardCanisters");
		collectionScreen.loadSpriteSheet("/SubArtifactCanister.png", "SubArtifactCanister");
		collectionScreen.loadSpriteSheet("/font.png", "font");
		collectionScreen.loadSpriteSheet("/ShardCanisterTitles.png", "shardTitles");
		collectionScreen.loadSpriteSheet("/ContinueButton.png", "continue");
		collectionScreen.loadSpriteSheet("/ShardCanisterLabel.png", "shardCanisterLabel");
		
		String currentLevelString = currentLevel.substring(0, 5) + ":" + currentLevel.substring(5, currentLevel.length());
		
		title = new Title(collectionScreen, currentLevelString, width);
		
		time = new Time(collectionScreen, levelS, finishTime, thresholdTime);
		
		compBar = new CompletionBar(collectionScreen, levelS, true, time.timeBonus, totalLevelArtifacts, subArtifacts.length);
		
		mainArt = new MainArtifactCanister(collectionScreen, artifact.xTile, artifact.yTile, artifact.widthPixels, artifact.heightPixels);
		
		
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
					shardArt[i] = new SubArtifactShards(collectionScreen, currentX + ((i * spacing) + (i * 232)), currentY + (384 / 2) - (332 / 2), subArtifacts[j], i);
					addNull = false;
				}
			}
			
			//Adds empty canister if the sub artifact was not listing in the found artifacts array or subArtifacts.
			if(addNull){
				shardArt[i] = new SubArtifactShards(collectionScreen, currentX + ((i * spacing) + (i * 232)), currentY + (384 / 2) - (332 / 2), null, i);
			}
			
			currentI = i;
		}
		currentI++;
		if(subArtifacts.length == totalLevelArtifacts){
			subArt = new SubArtifactCanister(collectionScreen, currentX + ((currentI * spacing) + (currentI * 232)), currentY, true, completedSubArtifact);
		}else{
			subArt = new SubArtifactCanister(collectionScreen, currentX + ((currentI * spacing) + (currentI * 232)), currentY, false, completedSubArtifact);
		}
		
		continueButton = new Continue(collectionScreen, width, height);
		
	}
	
	public void tick(){
		mainArt.tick();
		for(int i = 0; i < shardArt.length; i++){
			shardArt[i].tick();
		}
		subArt.tick();
	}
	
	//THIS SCREEN IS THE COLLECTION SCREEN
	
	public boolean firstTimeRender = true;
	
	public void render(Screen screen, float interpolation){
		
		if(firstTimeRender) {
			
			int a1 = (tranpsarentColor >> 24) & 0xFF;
			int r1 = (tranpsarentColor >> 16) & 0xFF;
			int g1 = (tranpsarentColor >> 8) & 0xFF;
			int b1 = (tranpsarentColor) & 0xFF;
			
			
			float rat1 = (float) a1 / 255;
			float rat2 = 1 - ((float) a1 / 255);

			for(int i = 0; i < screen.pixels.length; i++) {
				
				int color = screen.pixels[i];
				
				int r2 = (color >> 16) & 0xFF;
				int g2 = (color >> 8) & 0xFF;
				int b2 = (color) & 0xFF;
				
				int r = Math.round(Math.min((r1 * rat1) + (r2 * rat2), 255));
				int g = Math.round(Math.min((g1 * rat1) + (g2 * rat2), 255));
				int b = Math.round(Math.min((b1 * rat1) + (b2 * rat2), 255));
				
				screen.pixels[i] = (-1 << 24) | (r << 16) | (g << 8) | b;
				
			}
			
			title.render(screen);
			mainArt.render(screen);
			time.render(screen);
			compBar.render(screen);
			for(int i = 0; i < shardArt.length; i++){
				shardArt[i].render(screen);
			}
			subArt.render(screen);
			
			firstTimeRender = false;
		}
	
		continueButton.render(screen, interpolation);
		
	}

}

