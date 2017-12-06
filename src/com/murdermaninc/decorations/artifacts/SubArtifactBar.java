package com.murdermaninc.decorations.artifacts;

import com.murdermaninc.graphics.Screen;

public class SubArtifactBar {


	private int totalArtifacts; //TOTAL ARTIFACTS FOR CURRENT LEVEL
	private int width;
	
	private int scale = 4;
	private int spacing = 12 * scale;
	
	public SubArtifactBar(int totalArtifacts, int width){
		this.totalArtifacts = totalArtifacts;
		this.width = width;
	}
	
	
	public void render(Screen screen, int[][] xYTiles){
		
		int[] artifactsMarks = new int[totalArtifacts];
		
		for(int i = 0; i < totalArtifacts; i++){
			for(int j = 0; j < xYTiles.length; j++){
				if(xYTiles[j][2] == i){
					artifactsMarks[i] = 1;
				}
			}
		}
		
		int widthOfBar = widthOfBar();
		int x = (width / 2) - (widthOfBar / 2);
		int y = 10;
		for(int i = 0; i < totalArtifacts + 2; i++){
			if(i == 0){
				if(xYTiles.length != totalArtifacts){
					screen.render(screen.screenX + x + (i * spacing), screen.screenY + y, 8, 9, 1, 1, scale, "Icons");
				}else{
					screen.render(screen.screenX + x + (i * spacing), screen.screenY + y, 8, 8, 1, 1, scale, "Icons");
				}
			}else if(i > 0 && i < totalArtifacts + 1 && artifactsMarks[i - 1] == 0){
					screen.render(screen.screenX + x + (i * spacing) + 4, screen.screenY + y, 9, 9, 1, 1, scale, "Icons");
			}else if(i == totalArtifacts + 1){
				if(xYTiles.length != totalArtifacts){
					screen.render(screen.screenX + x + (i * spacing), screen.screenY + y, 10, 9, 1, 1, scale, "Icons");
				}else{
					screen.render(screen.screenX + x + (i * spacing), screen.screenY + y, 10, 8, 1, 1, scale, "Icons");
				}
			}
		}
		
		for(int i = 0; i < totalArtifacts; i++){
			for(int j = 0; j < xYTiles.length; j++){
				if(xYTiles[j][2] == i){
					screen.render(screen.screenX + x + (i * spacing) + spacing + ((64 - (xYTiles[j][3] * scale)) / 2), screen.screenY + y + 12 + ((spacing - (xYTiles[j][4] * scale)) / 2), xYTiles[j][0], xYTiles[j][1], 1, 1, scale, "Icons");
				}
			}
		}
		
		
	}
	
	private int widthOfBar(){
		return (totalArtifacts + 2 - 1) * (spacing) + (16 * scale);
	}
}
