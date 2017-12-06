package com.murdermaninc.decorations;

import java.util.ArrayList;

import com.murdermaninc.graphics.Animation;
import com.murdermaninc.graphics.Screen;

public class Water extends DeadlyDecoration{

	private Animation animation = new Animation();
	
	
	private String flow = "";
	
	public Water(int id, int x, int y, int xTile, int yTile, int spriteWidth, int spriteHeight, int render, String deathTag, int leftOffset, int rightOffset, int topOffset, int bottomOffset, String flow) {
		super(id, x, y, xTile, yTile, spriteWidth, spriteHeight, render, deathTag, leftOffset, rightOffset, topOffset, bottomOffset);
		this.flow = flow;
	}
	
	@Override
	public void render(Screen screen, float interpolation){
		if(AnimationData == null && flow.equals("stand")) AnimationData = animation.loadAnimationDataRectangle(screen, "Icons", 4, 5, xTile, yTile, spriteWidth, spriteHeight, 1, 6);
		
		if(AnimationData == null && flow.equals("vertFlow")) AnimationData = animation.loadAnimationDataRectangle(screen, "Icons", 4, 5, xTile, yTile, spriteWidth, spriteHeight, 1, 6);
		
		
		if(AnimationData == null && flow.equals("vertFlowBehind")) AnimationData = animation.loadAnimationDataRectangle(screen, "Icons", 4, 5, xTile, yTile, spriteWidth, spriteHeight, 6, 1);
		
		if(AnimationData == null && (flow.equals("horiFlowLeft") || flow.equals("horiFlowRight"))){
			
			int[] untextured;
			ArrayList<int[]> texture;
			
			if(flow.equals("horiFlowRight")){
				texture = animation.loadAnimationDataRectangle(screen, "Icons", 4, 5, 9, 10, spriteWidth, spriteHeight, 1, 6);
			}else{
				texture = animation.loadAnimationDataRectangle(screen, "Icons", 4, 5, 10, 10, spriteWidth, spriteHeight, 1, 6);
			}
			untextured = screen.loadData(xTile, yTile, 1, 1, 4, "Icons");
			
			AnimationData = new ArrayList<int[]>();
			
			for(int i = 0; i < texture.size(); i++){
				int[] texturedAnimation = new int[64 * 64];
				int[] currentAnimation = texture.get(i);
				for(int y = 0; y < 64; y++){
					for(int x = 0; x < 64; x++){
						if(untextured[x + y * 64] == -16777216){
							texturedAnimation[x + y * 64] = currentAnimation[x + y * 64];
						}else{
							texturedAnimation[x + y * 64] = untextured[x + y * 64];
						}
					}
				}
				AnimationData.add(texturedAnimation);
			}
		}
		
		
		
		
		
		
		if(flow.equals("stand")){
			animation.animateContinuous(screen, AnimationData, true, 5.0F, spriteWidth, spriteHeight, x, y, 8, 4, interpolation);
		}else{
			animation.animateContinuous(screen, AnimationData, false, 8.0F, spriteWidth, spriteHeight, x, y, 5, 4, interpolation);
		}
	}

}
