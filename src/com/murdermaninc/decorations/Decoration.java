package com.murdermaninc.decorations;

import java.util.ArrayList;

import com.mudermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;

public class Decoration {

	public static ArrayList<Decoration> decorations = new ArrayList<Decoration>();
	public static ArrayList<Decoration> decorationsAnimated = new ArrayList<Decoration>();
	public static ArrayList<Decoration> decorationsSubArtifacts = new ArrayList<Decoration>();
	public int render;
	public int animationCounter = 0;
	
	public int x,y;
	public int xTile, yTile;
	public int spriteWidth, spriteHeight;
	public int id;
	
	public boolean animated;
	public boolean pickUp;
	
	protected int Data[];
	protected ArrayList<int[]> AnimationData;
	
	public Decoration(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render){
		this.id = id;
		this.x = x;
		this.y = y;
		this.xTile = xTile;
		this.yTile = yTile;
		this.render = render;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.pickUp = false;
	}
	public void tick(Player player){
	}
	
	protected void loadData(Screen screen, int spriteAmount){
		AnimationData = new ArrayList<int[]>();
		for(int i = 0; i < spriteAmount; i++){
			AnimationData.add(screen.loadData(xTile + i, yTile, spriteWidth, spriteHeight, 4, "Icons"));
		}
	}
	
	public void render(Screen screen, float interpolation){
		if(Data == null){
			Data = screen.loadData(xTile, yTile, spriteWidth, spriteHeight, 4, "Icons");
		}
		screen.renderData(Data, x, y, spriteWidth, spriteHeight, 4);
	}
	
}
