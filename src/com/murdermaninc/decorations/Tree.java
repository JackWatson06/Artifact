package com.murdermaninc.decorations;

import com.mudermaninc.entity.Entity;
import com.mudermaninc.entity.FriendlyLeaf;
import com.mudermaninc.entity.Player;
import com.murdermaninc.graphics.Screen;

public class Tree extends Decoration{
	
	private int[] Leaves;
	private int[] UpperLeaves;
	private int[] Trunk;
	private int[] Stump;
	
	
	public Tree(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render);

	}
	
	@Override
	public void tick(Player player){
		float spawnChance = (float) Math.random();
		
		if(spawnChance <= 0.0008){
			Entity.entities.add(new FriendlyLeaf(0, (int) (x + 64 + (Math.random() * 532)), y + 56, 1, 1, 1, 1));
			
		}
	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(UpperLeaves == null) UpperLeaves = screen.loadData(13, 12, 11, 1, 4, "Trees");
		if(Leaves == null) Leaves = screen.loadData(0, 0, 11, 1, 4, "Trees");
		if(Trunk == null){
			if(id == 2039){
				Trunk = screen.loadData(13, 0, 4, 12, 4, "Trees");
			}else if(id == 2040){
				Trunk = screen.loadData(17, 0, 4, 12, 4, "Trees");
			}else{
				Trunk = screen.loadData(21, 0, 3, 12, 4, "Trees");
			}	
		}
		if(Stump == null){
			if(id == 2039){
				Stump = screen.loadData(0, 1, 7, 6, 4, "Trees");
			}else if(id == 2040){
				Stump = screen.loadData(7, 1, 6, 6, 4, "Trees");
			}else{
				Stump = screen.loadData(0, 7, 7, 6, 4, "Trees");
			}	
		}
		
		
		if(id == 2039){
			screen.renderData(Leaves, x, y, 11, 1, 4);
			screen.renderData(UpperLeaves, x, y - 60, 11, 1, 4);
			screen.renderData(Trunk, x + 236, y + 56, 4, 12, 4);
			screen.renderData(Stump, x + 140, y + 748 + 28, 7, 6, 4);
		}else if(id == 2040){
			screen.renderData(Leaves, x, y, 11, 1, 4);
			screen.renderData(UpperLeaves, x, y - 60, 11, 1, 4);
			screen.renderData(Trunk, x + 220, y + 56, 4, 12, 4);
			screen.renderData(Stump, x + 128, y + 784 + 28, 6, 6, 4);
		}else{
			screen.renderData(Leaves, x, y, 11, 1, 4);
			screen.renderData(UpperLeaves, x, y - 60, 11, 1, 4);
			screen.renderData(Trunk, x + 244, y + 56, 3, 12, 4);
			screen.renderData(Stump, x + 164, y + 784 + 28, 7, 6, 4);
		}
	}

	
	
}
