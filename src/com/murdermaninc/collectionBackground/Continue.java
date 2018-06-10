package com.murdermaninc.collectionBackground;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;

public class Continue {

	private ArrayList<int[]> Data;
	
	private Animation animation = new Animation();
	
	private int sprites = 8;
	
	private int width;
	private int height;
	
	public Continue(Screen screen, int width, int height){
		this.width = width;
		this.height = height;
		
		if(Data == null){
			Data = new ArrayList<int[]>();
			for(int i = 0; i < sprites; i++){
				Data.add(screen.loadData(0, 0 + i, 7, 1, 3, "continue"));
			}
		}
	}
	
	public void render(Screen screen, float interpolation){
		
		animation.animateContinuous(screen, Data, true, 5F, 7, 1, width - 312 - 40 + screen.screenX, height - 56 + screen.screenY, 14, 3, interpolation);

	}
	
}
