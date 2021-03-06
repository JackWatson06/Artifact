package com.murdermaninc.collectionBackground;


import com.murdermaninc.graphics.Screen;

public class SubArtifactCanister {
	
	
	private int[] Data;
	private int[] label;
	
	
	private int x,y;
	private int xPixel, yPixel;
	private boolean completeArtifact;
	private int xTile, yTile;
	
	public SubArtifactCanister(Screen screen, int x, int y, boolean completeArtifact, int[] completedSubArtifactData){
		this.x = x;
		this.y = y;
		this.completeArtifact = completeArtifact;
		if(completedSubArtifactData != null){
			this.xTile = completedSubArtifactData[0];
			this.yTile = completedSubArtifactData[1];
			this.xPixel = completedSubArtifactData[2];
			this.yPixel = completedSubArtifactData[3];
		}
		if(Data == null){
			if(!completeArtifact){
				Data = screen.loadData(0, 0, 5, 6, 4, "SubArtifactCanister");
			}else{
				Data = screen.loadData(0, 6, 5, 6, 4, "SubArtifactCanister");
			}
		}
		
		if(label == null){
			label = screen.loadData(0, 0, 4, 1, 4, "shardCanisterLabel");
		}
		
	}
	
	public void tick(){
		
	}
	
	public void render(Screen screen){
		
		screen.renderData(Data, x + screen.screenX, y + screen.screenY, 5, 6, 4);
		screen.renderData(label, x + 48 + screen.screenX, y + 8 + screen.screenY, 4, 1, 4);
		if(completeArtifact){
			screen.render(x  + (288 / 2) - (xPixel * 8 / 2) + screen.screenX, y + (384 / 2) - (yPixel * 8 / 2) + screen.screenY, xTile, yTile, 1, 1, 8, "Icons");
		}
	}

}
