package com.murdermaninc.collectionBackground;


import com.murdermaninc.graphics.Font;
import com.murdermaninc.graphics.Screen;
import com.murdermaninc.level.LevelSequencing;

public class CompletionBar {

	//Time Bonus counts for 33%, main artifact counts for 33% and completed sub Artifact counts for 33% and shards are counted out of that 33%.
	
	
	private int[] Data;
	private int[] completionColor;
	private Font font = new Font();
	private int x, y;
	
	private int completionPercentage = 0;
	
	public CompletionBar(LevelSequencing levelS, boolean mainArtifact, boolean timeBonus, int totalShardsInLevel, int numberOfShards){
		
		x = (1140 + ((6 * 64) / 2)) - ((125 * 4) / 2);
		y = 440;
		
		float score = 0;
		
		if(mainArtifact){
			score += 0.333333333F;
		}
		
		if(timeBonus){
			score += 0.333333333F;
		}
		
		if(totalShardsInLevel == numberOfShards){
			score += 0.333333333F;
		}else{
			float shardWorth = 0.3333333333F / totalShardsInLevel;
			System.out.println(shardWorth);
			score += numberOfShards * shardWorth;
		}
		
		completionPercentage = (int) (score * 100);
		
		levelS.savePercentage(completionPercentage);

	}
	
	public void render(Screen screen){
		if(Data == null){
			if(completionPercentage == 100){
				Data = screen.loadData(0, 0, 8, 2, 4, "CompletionBarSprite");
			}else{
				Data = screen.loadData(0, 2, 8, 2, 4, "CompletionBarSprite");
			}
		}
		
		if(completionColor == null){
			if(completionPercentage == 100){
				completionColor = screen.loadData(0, 4, 8, 1, 4, "CompletionBarSprite");
			}else if(completionPercentage >= 50){
				int cutOff = (int) ((123 * (completionPercentage / 100F)) * 4);
				completionColor = screen.loadData(0, 5, 8, 1, 4, "CompletionBarSprite");
				for(int y = 0; y < 16 * 4; y++){
					for(int x = 0; x < 128 * 4; x++){
						if(x > cutOff){
							completionColor[x + y * (128 * 4)] = -1086453;
						}
					}
				}
			}else{
				int cutOff = (int) ((123 * (completionPercentage / 100F)) * 4);
				completionColor = screen.loadData(0, 6, 8, 1, 4, "CompletionBarSprite");
				for(int y = 0; y < 16 * 4; y++){
					for(int x = 0; x < 128 * 4; x++){
						if(x > cutOff){
							completionColor[x + y * (128 * 4)] = -1086453;
						}
					}
				}
			}
		}
		
		//(1140 + ((6 * 16) / 2)) - ((125 * 4) / 2)
		
		screen.renderData(Data, x, y, 8, 2, 4);
		screen.renderData(completionColor, x + 4, y + 56, 8, 1, 4);
		
		//(1140 + ((6 * 64) / 2)) - ((125 * 4) / 2) = 1082
		
		//CHANGE THE PNG AS CURRENTLY THE SECOION FOR THE TITLE CONTAINS AN ODD NUMBER OF PIXELS
		
		font.drawText(screen, "Completion:" + completionPercentage + "%", (1082 + ((125 * 4) / 2)) - (font.getTextLength("Completion:" + completionPercentage + "%", 2 ) / 2), 440 + ((14 / 2) * 4) - (6 * 2) - (4 * 2) , 2);
	}
	
}
