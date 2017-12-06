package com.murdermaninc.collectionBackground;

import java.util.ArrayList;

import com.murdermaninc.graphics.Screen;

public class MainArtifactCanister {
	
	//TODO add label for the main artifact describing what the image is.
	
	
	private int artXTile, artYTile;
	private int artWidthPixels, artHeightPixels;
	
	private int x = 250;
	private int y = 100;
	
	private ArrayList<int[]> Data = new ArrayList<int []>();
	
	private int tickCounter = 0;
	
	private boolean testing = true;
	
	public MainArtifactCanister(int currentArtXTile, int currentArtYTile, int artWidthPixels, int artHeightPixels){
		artXTile = currentArtXTile;
		artYTile = currentArtYTile;
		this.artWidthPixels = artWidthPixels;
		this.artHeightPixels = artHeightPixels;
	}
	
	private boolean reverse = true;
	public void tick(){
		//numberOfUpdates / 2 should preferable be even to counteract the other movement as it is reversed everytime.
		
		float seconds = 16F;
		int numberOfUpdates = 8;
		float updateSeconds = seconds / numberOfUpdates;
		
		if(tickCounter >= seconds * 60){
			tickCounter = 0;
		}
		
		if(tickCounter % (int) (updateSeconds * 60) == 0){
			if(tickCounter <= (seconds * 60 / 2) - 1){
				if(reverse){
					//artY += 5;
					reverse = !reverse;
				}else{
					//artX += 2;
					reverse = !reverse;
				}
			}else{
				if(reverse){
					//artY -= 5;
					reverse = !reverse;
				}else{
					//artX -= 2;
					reverse = !reverse;
				}
			}
		}
		
		tickCounter++;
	}
	
	public void render(Screen screen){
		if(testing){
			Data.add(screen.loadData(0, 0, 7, 9, 4, "ArtifactCell"));
			testing = false;
		}
		
		screen.renderData(Data.get(0), x, y, 7, 9, 4);
		
		screen.render(x +  (392 / 2) - (artWidthPixels * 20 / 2), y + (536 / 2) - (artHeightPixels * 20 / 2), artXTile, artYTile, 1, 1, 20, "Icons");
	}

}
