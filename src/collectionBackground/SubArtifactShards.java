package com.murdermaninc.collectionBackground;

import java.util.ArrayList;

import com.murdermaninc.graphics.Screen;

public class SubArtifactShards {

	//TODO add more than one animation for the shards to give it some variety
	
	
	private ArrayList<int[]> Data = new ArrayList<int[]>();
	private int[] shardData;
	
	private boolean testing = true;
	private int tickCounter = 0;
	
	private int x,y;
	private int xPixel, yPixel;
	private int shardXTile, shardYTile;
	private boolean renderShard = false;
	
	private boolean artifactFound;
	
	public SubArtifactShards(Screen screen, int x, int y, int[] subArtifacts, int shardLogo){
		this.x = x;
		this.y = y;
		
		if(subArtifacts != null){
			artifactFound = true;
			renderShard = true;
			shardXTile = subArtifacts[0];
			xPixel = subArtifacts[3];
			shardYTile = subArtifacts[1];
			yPixel = subArtifacts[4];
		}else{
			artifactFound = false;
		}
		
		if(testing){
			if(!artifactFound){
				Data.add(screen.loadData(0, 0, 4, 6, 4, "ShardCanisters"));
			}else{
				Data.add(screen.loadData(0, 6, 4, 6, 4, "ShardCanisters"));
			}
			testing = false;
		}
		if(shardData == null){
			shardData = screen.loadData(0, shardLogo, 3, 1, 4, "shardTitles");
		}
	}
	
	private boolean firstUpdate = true;
	private boolean reverse = true;
	
	public void tick(){
		
		if(renderShard){
			float seconds = 16F;
			int numberOfUpdates = 12;
			float updateSeconds = seconds / numberOfUpdates;
		
			if(firstUpdate){
				tickCounter = (int) (Math.random() * seconds) * 60;
				firstUpdate = false;
			}
		
			if(tickCounter >= seconds * 60){
				tickCounter = 0;
			}
		
			if(tickCounter % (int) (updateSeconds * 60) == 0){
				if(tickCounter <= (seconds * 60 / 2) - 1){
					if(reverse){
						//shardY += 5;
						reverse = !reverse;
					}else{
						//shardX += 2;
						reverse = !reverse;
					}
				}else{
					if(reverse){
						//shardY -= 5;
						reverse = !reverse;
					}else{
						//shardX -= 2;
						reverse = !reverse;
					}
				}
			}
		
			tickCounter++;
		}
	}
	
	public void render(Screen screen){
		
		screen.renderData(Data.get(0), x + screen.screenX, y + screen.screenY, 4, 6, 4);
		screen.renderData(shardData, x + 40 + screen.screenX, y + 12  + screen.screenY, 3, 1, 4);
		if(renderShard){
			//System.out.println(xPixel);
			//System.out.println(yPixel);
			screen.render(x  + (232 / 2) - (xPixel * 8 / 2)  + screen.screenX, y + (332 / 2) - (yPixel * 8 / 2) + screen.screenY, shardXTile, shardYTile, 1, 1, 8, "Icons");
		}
	}
}
